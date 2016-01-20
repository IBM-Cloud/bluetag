#Introduction to Bluetag

BlueTag is a fun mobile application that aims to connect professionals based on skills and geolocation.  The app can be used at conferences, work places, and meetups to identify people with the skills you are interested in and connect with them.  The application is built using a host of modern development technologies and tools.

The frontend of the application was developed using the Polymer Web Components Framework.  The Polymer framework's web components are used to create a simple and consistent UI.  The application is structured to work Apache Cordova.  Cordova enables mobile applications to be written using HTML, Javascript, and can use Cordova API's to access native functions.  Cordova lets developers write mobile applications for almost any mobile platform using a single code base and significantly reduces the cost and development time of mobile applications.  The backend services are broken up as microservices running on the WebSphere Liberty Profile on Bluemix and rely on a Cloudant nosql database also deployed via Bluemix.

What makes this application a good sample?  This sample demonstrates how to bring together many modern technologies like Cordova and Polymer for mobile development, Java runtimes in the cloud, Web sockets, REST services, microservices, NoSQL databases, location services and many more.  The current sample is the alpha version of our vision for the BlueTag application.

The source code for both the frontend and backend are hosted on GitHub.  We would love for you to join the project and contribute!

# Download the BlueTag code

Clone the app to your local environment.  Since Bluetag is a micro-service sample, all the services have their own github repositories.  All the services are defined as submodules of bluetag. 
From your terminal using the following commands:

  ```
  mkdir myBluetag
  cd myBluetag
  git clone --recursive git@github.ibm.com:Bluetag/bluetag.git
  ```

This will pull in the master bluetag repository and a bunch of submodules that contain the code for the frontend, backend microservices, and shared code.

# Setting up a dev environment for the microservices in Eclipse

1. In Eclipse, import the cloned projects into your workspace.
	- Goto File > Import and select General > Existing Projects into Workspace.
	- Select the bluetag directory and it will import all the submodules as projects to your workspace.
	
2. To get the projects to build, we have to update the build paths.
	- Right click on each project and select "Build Path" > "Configure Build Path".
	- In the panel that pops up (Java Build Path), switch to the "Libraries" tab and double click on "JRE System Library".
	- Update it to point the "Workspace default JRE" option.

3. Next we need to install a local instance of the Liberty app server.  We need this to resolve some build dependencies and will also be using this to test our microservices locally before pushing to Bluemix.  Download the latest JavaEE version of the Liberty appserver from wasdev.net (Inlucde link here).
	- In the Server view, right click and select New > Server.
	- Select IBM > WebSphere Application Server Liberty and click Next.
	- In the "Choose an existing installation" selection, Browse and point to the wlp directory of the install you downloaded earlier.
	- In the next panel, click "New" and create a server with a name like BlueTag and hit Finish.
	- Clean and rebuild all the projects.
	
You should have a workspace that's ready to build the BlueTag backend services now!

# Building and testing the frontend

1. The bluetag-frontend project is structured to work with Cordova and is written using HTML5 and javascript.

2. To test the code locally from the bluetag-frontend/www directory run the following command to download the gulp node modules

   ```
   npm install
   ```
   
3. To build and start a webserver to host the application locally run the following command

   ```
   gulp serve
   ```
   
   This should produce output similar to this and list the URL the application can be accessed from:
   ```
	arshadmus-MacBook-Pro:www arshadmu$ gulp serve
	[15:15:09] Using gulpfile ~/Desktop/BT/www/gulpfile.js
	[15:15:09] Starting 'styles'...
	[15:15:10] Starting 'elements'...
	[15:15:10] Finished 'elements' after 20 ms
	[15:15:10] styles all files 98 B
	[15:15:10] Finished 'styles' after 1 s
	[15:15:10] Starting 'serve'...
	[15:15:10] Finished 'serve' after 569 ms
	[PSK] Access URLs:
	 -----------------------------------
		   Local: http://localhost:5000
		External: http://12.34.56.78:5000
	 -----------------------------------
			  UI: http://localhost:3001
	 UI External: http://12.34.56.78:3001
	 -----------------------------------
	[PSK] Serving files from: .tmp
	[PSK] Serving files from: app
	```

You are now ready to tweak the bluetag frontend code and test it!