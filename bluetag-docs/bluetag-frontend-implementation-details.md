#Developing a mobile / web application using Polymer & Cordova

The BlueTag client application is a hybrid mobile application that is built on Cordova.  Polymer web components are used to assemble the UI and make REST service calls to the backend services deployed to Bluemix.

Let's start by examining the contents of the application we pulled in earlier from GitHub.  The www folder under BlueTagFrontEnd contains the majority of the application code.  The rest of the files under BlueTagFrontEnd are Cordova artifacts.  Let’s look at a couple of other key files unde www.

*index.html - As the name would imply, this is the starting point of the application.  This file is invoked when the application is started.
*app.js – This contains the js scripts that are run in the application.  One of the key aspects of this application, location data management, is handled in this file.
*bower_components - This folder contains the polymer elements that were downloaded using bower that are used by the application.  <MUNEEB: can you talk about how you got these here?>
*elements - This folder contains custom elements that are created by configuring and assembling a set of core Polymer elements.  We will discuss how to create one of these custom elements further in the article.

Those are the main parts of the application.  Now let's talk about the design and the implementation of the application a bit more.  

The BlueTag application starts with a simple registration / login screen.  When a user first launches the application, they see a panel with a text field where they can type in a user id that is either used to register them or if it's an already existing user id pull up their records.  The goal is to build on this to use a proper authentication service, but to get started this does the job.  The text field and submit button are driven by these two lines in the index.html file:
```
	<paper-input floatingLabel name="name" label="enter a name to get started" value="{{username}}" id="userinput" required />
	<br>
	<div align="center"> <get-username  id="getusername" value="{{username}}"></get-username></div>
```
paper-input is a core Polymer element that is used to gather input.  get-username, which displays the Submit button, is a custom element that displays a button and drives an AJAX request to a backend service to process the user input.

Let's switch to www\elements\get-username\get-username.html to look at how to write a custom element.  The custom element is created by defining a dom-element with the name of the custom element.  The custom element contains three sections; a style section, a template section, and a script section.  The style section contains CSS attributes to define the look of the element. Any HTML inside the <template> tags will define what the dom of this element will look like. The script section details the actions of the elements when an event is triggered.

For this particular element, the script section reads in the user input and then uses the Polymer element, iron-ajax, to send an AJAX request to a backend REST service running on Liberty.   It receives a response back from the service and the application is routed to the “home” page.

Next let’s look at how we manage the location service.  This is handled in the app.js file.

A series of event listeners are set inside app.js; the core of location update logic is done inside the ‘locupdate’ event listener function.

```
	document.querySelector(‘my-map’).addEventListener('locupdate', function(e) {
	…}
```
	
After a websocket connection has been established with the BluetagLocation service the navigator.geolocation api is invoked using its watchPosition method. This method is used to poll the current position of the device and enters a callback function, which takes a Position parameter. In this case this Position is a coordinate representation of the device location, which can be accessed via Position.coords.latitude and Position.coords.longitude.
On each location update the coordinates are passed to the map, marker, and mark-it elements and used to update the marker position the user sees and re-center the map. The coordinates are made available to the mark-it element incase the user would like to mark that location. Instances where the coordinates are printed to the screen can be done by setting innerHTML for the desired div, for example: 

```
	document.querySelector('#latdiv').innerHTML = latitude;
	document.querySelector('#londiv').innerHTML = longitude;
```

Elements with coordinate properties such as the google-map and google-marker elements can be updated similarly:

```
	document.querySelector('#g-map').latitude = latitude;
	document.querySelector('#g-map').longitude = longitude;
```

After updating the user location where needed in app, the information needs to be sent to a back-end location service. Since position is updated frequently we can open a web-socket to the liberty service once on app startup and continuously push location updates whenever they occur. The BluetagLocation service is configured to accept socket connections; the front-end can open a connection like this:

```
	locationSocket = new WebSocket("ws://bluetaglocation1.mybluemix.net/wsLocationResource");
	locationSocket.onopen = function(msg) {console.log("Socket Open")};
	locationSocket.onmessage = function(msg) {console.log("Server says: " + JSON.stringify(msg) )};
	locationSocket.send(JSON.stringify(userloc));
```

The onopen and onmessage callbacks are used to log when the socket opens successfully and when the server sends a message back to the front-end application. At this point the application has done everything required after a location changes; the new location information is displayed to the user and updated in app as well as sent to the Location service for further processing.

Finally, let’s talk about how we how we manage the menu for this application.  The scaffolding is extended mainly from what is made available by the Polymer Starter Kit – a pre-stitched together number of polymer elements to create an extendable user menu. Paper-toolbar and paper-drawer are used to create the top and left hand side panels while paper-menu and iron-pages are used to populate the drawer panel and define the main content area, respectively. The starter kit includes a number of commonly used paper and iron elements; additional elements can be added at any time using the bower commands found on the corresponding element catalog page (https://elements.polymer-project.org/). The starter kit zip can be downloaded from https://developers.google.com/web/tools/polymer-starter-kit/?hl=en

After downloading the starter kit, it can be ran as is through any http server. For this project we used the python SimpleHTTPServer. Simply navigate to the www directory inside the starter kit project and run

```	
	python –m SimpleHTTPServer
```
	
Use any browser to navigate to the host/port provided and see the sample app (like http://localhost:5000). To instantly test changes to the starter-kit, leave this server running, save changes to the index.html and refresh the browser to see the changes made.
