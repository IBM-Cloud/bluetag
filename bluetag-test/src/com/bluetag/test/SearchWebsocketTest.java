package com.bluetag.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.net.URI;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

//import javax.json.JsonArray;
//import javax.json.JsonNumber;
//import javax.json.JsonObject;
//import javax.json.JsonString;
//import javax.json.JsonValue;
//import javax.json.JsonValue.ValueType;
import javax.websocket.ClientEndpoint;
import javax.websocket.ContainerProvider;
import javax.websocket.OnError;
import javax.websocket.OnOpen;
import javax.websocket.OnMessage;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;

import org.junit.Before;
import org.junit.Test;
import org.junit.runners.Parameterized;


/**
 * Derived from https://github.com/WASdev/sample.async.websockets.git 
 * 
 * <p>This class uses the {@link Parameterized} JUnit runner to test various websocket. 
 * Subclasses must:</p>
 * <ul>
 * <li>Provide websocket relative to <code>http://localhost:{port}/websocket/</code> via a <code>@Parameters</code> annotated method</li>
 * <li>Implement the {@link #assertResponseStringCorrect(String)} template method to check the response string from a given websocket</li>
 * </ul>
 */
@ClientEndpoint
public class SearchWebsocketTest {

    private Session session;
    private static String response = null;
    private static String error = null;
    
    // We need a no-arg constructor because Jetty attempts to create an instance of this class using reflection.
    //public SearchWebsocketTest() {
    	
    //}
    
    @Before
    public void cleanTestVars() {
    	// Reset the static variables we use to test that the responses are correct.
    	SearchWebsocketTest.response = null;
    	SearchWebsocketTest.error = null;
    }
    
    @Test
    public void testSearchWebSocket() throws Exception {
    	//search for names starting with m
    	testWebsocket("SearchWS","name:m*", "muneeb", false);
    }
//    @Test
//    public void testSimpleAnnotatedWebSocket() throws Exception {
//    	testWebsocket("SimpleAnnotated", "server received:  This is a test from SimpleAnnotated", false);
//    }
//    
//    @Test
//    public void testProgrammaticEndpointWebSocket() throws Exception {
//    	testWebsocket("ProgrammaticEndpoint", "server received:  This is a test from ProgrammaticEndpoint", false);
//    }
//    
//    @Test
//    public void testEchoEndpointWebSocket() throws Exception {
//    	testWebsocket("EchoEndpoint", "[ep=3, msg=0]: This is a test from EchoEndpoint", false);
//    }
//    
//    @Test
//    public void testEchoAsyncEndpointWebSocket() throws Exception {
//    	testWebsocket("EchoAsyncEndpoint", "[ep=2, msg=0]: This is a test from EchoAsyncEndpoint", false);
//    }
//    
//    @Test
//    public void testEchoEncoderEndpointWebSocket() throws Exception {
//    	testWebsocket("EchoEncoderEndpoint", "{\"count\":0,\"content\":\"This is a test from EchoEncoderEndpoint\"}", true);
//    }
    
    /**
     * The main test method. This accepts the websocket endpoint to connect to, and sends a request through to the server.
     * It then waits for a response and check that it is the correct response.
     * @param websocketEndpoint - The websocket endpoint name. This is constructed into a full ws://... url
     * @param textToSend - text to send over websocket
     * @param expectedResponse - subset of the message we expect back from the websocket
     * @param sendObject - A boolean indicating whether we need to put the request String in JSON or not.
     * @throws Exception
     */
    public void testWebsocket(String websocketEndpoint, String textToSend, String expectedResponse, boolean sendObject) throws Exception {
    	try {
    		// Create a WebSocket client, and build up the websocket URL to use.
    		WebSocketContainer c = ContainerProvider.getWebSocketContainer();
			String port = System.getProperty("liberty.test.port");//TODO use this instead of hardcode
	        String websocketURL = "ws://localhost:" +"9081" + "/" + websocketEndpoint;
	        URI uriServerEP = URI.create(websocketURL);
	        System.out.println("Websocket URI:" + uriServerEP.toString() );
			// Connect to the websocket
	        session = c.connectToServer(SearchWebsocketTest.class, uriServerEP);
			
	        // Set text to send. If we need to send an object, create the JSON String instead.
	        String text = textToSend;
			if (sendObject) {
				text = "{\"q=t*\": \"This is a test from " + websocketEndpoint + "\"}";//TODO parse json object
				text = "{\"content\": \"This is a test from " + websocketEndpoint + "\"}";
			} 
    		
			// Send the text string to the server
			session.getAsyncRemote().sendText(text);//TODO this was getBasicRemote().  Not sure which makes more sense
			
			// Now wait for up to 5 secs to get the response back.
    		int count = 0;
    		while (SearchWebsocketTest.response == null && count < 10) {
    			count++;
    			Thread.sleep(500);
    		}
    		
    		// Run the Test asserts to ensure we have received the correct string, and that we haven't hit any errors.
    		System.out.println("Response: "+ SearchWebsocketTest.response);
    		assertTrue(SearchWebsocketTest.response.contains(expectedResponse));
    		//assertEquals("Message sent from the server doesn't match expected String", expectedResponse, SearchWebsocketTest.response);
    		//assertNull("There was an unexpected error during test: " + SearchWebsocketTest.error, SearchWebsocketTest.error);
    		
    	} finally {
    		// Finally if we have created a session, close it off.
    		if (session != null) {
    			try {
    				session.close();
    			} catch(Exception ioe) {
    				ioe.printStackTrace();
    			}
    		}
    	}
    }
    
    @OnOpen
    public void onOpen(Session userSession) {
        System.out.println("OnOpen: opening websocket");
        //this.userSession = userSession;
    }
	/**
	 * This method is triggered when the server sends a message across the websocket. We store this away in a static var
	 * so that we can check that the response was as expected.
	 * @param message - A String returned from the server.
	 * @param session - The session that we are using for the connection.
	 * @throws IOException
	 */
	@OnMessage
	public void receiveMessage(String message, Session session) throws IOException {
		System.out.println("OnMessage received: " + message);
		if (!message.startsWith("Connection Establish")) {//TODO is this a good way to filter out the Connection Established message?
			System.out.println("OnMessage ignored message: " + message);
			SearchWebsocketTest.response = message;
		}
	}
	
	/**
	 * This method is triggered when the server sends an error across the websocket. We store this away in a static var
	 * so that we can check whether it was expected or not.
	 * @param t - A Throwable object
	 */
	@OnError
	public void onError(Throwable t) {
		System.out.println("OnError gets exception: " + t.getMessage());
		SearchWebsocketTest.error = t.getMessage();
	}
}
