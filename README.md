#BluetagTest project

This project contains functional tests for Bluetag REST APIs.

Buildtime Dependencies:
  -  BluetagModels project - to get JSON/Cloudant models
  - Websocket API to compile websocket tests  
  TODO I pick this up from classpathentry kind="lib" path="/Users/tsee/bt2/liberty/dev/api/spec/com.ibm.ws.javaee.websocket.1.1_1.0.10.jar"
         which is not portable
  
Runtime Dependencies:
 - tests need to be run as JUnit
 - tests run against Bluetag REST APIs so a running Appserver target is needed, connected to a Cloudant instance
 
 - SearchWebsocket test needs a Java SE websocket client which is provided by the jars in the jetty/ directory.   Add these jars to the junit runner classpath
 
 To run tests:  select test, Eclipse -> run as... -> JUnit    
  
