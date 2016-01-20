# Overview


BlueTag is a mobile application that aims to connect people based on skills and geolocation.  
The app can be used at conferences, work places, and meetups to help a user locate people based on common skills and geo-location.  

As a sample, Bluetag showcases how you can build a set of backend micro-services coupled with an HTML5 mobile enabled front-end application.
The backend is comprised of a set of micro-services that run on node.js and Liberty Java runtimes that interact with the front-end application and a Cloudant datbase via REST and Web Socket APIs. 
The frontend is based on HTML5 Web Components and will run in any modern browser, in a laptop, or on a mobile device.  
It can also be deployed as a native Android or iOS app using Apache Cordova.

This sample demonstrates how Bluemix allows you to easily bring together and deploy many modern technologies like Cordova and Polymer for mobile development, Java and Node.js runtimes in the cloud, Web sockets, REST services, micro services, NoSQL databases, location services and many more technologies.



## How it Works - Live demo service

1. Go to the [live Bluetag demo][BluetagURL] and enter any name to get started. 

2. The map should display with a marker on your current location.  As you move around, the marker and map dynamically adjust to your location.

3. You can use the navigation links on the left to do the following:

**Near me:**  Lists users who are within 10 meters of you.  Press TAG and the user will be added to your contacts.

**Contacts:**  These are people that you have either tagged when they were near you, or that you added via Search.  You can click the button to show them on your map.

**Places:**  When you press "Mark it" on the map, the location is added on your places.   You can click the link to show this place on your map.

**Search:**  Start typing and Bluetag dynamically searches for user names that start with what you type.   You can select users to add to your contacts.

**Settings:**  You can prevent location updates from sending updates to the location service and you can toggle logging in the debug console.


### Architecture Diagram

<img src="./bluetag-services.png" width="650px"><br>This an architectural overview of the components that make this app run.<br>

## Running the app on Bluemix

1. Create a Bluemix Account

    [Sign up][bluemix_signup_url] for Bluemix, or use an existing account.

2. Download and install the [Cloud-foundry CLI][cloud_foundry_url] tool

3. Clone the app to your local environment. Since Bluetag is a micro-service sample, all the services have their own github repositories.  All the services are defined as submodules of bluetag. 
From your terminal using the following commands:


  ```
  mkdir myBluetag
  cd myBluetag
  git clone --recursive git@github.ibm.com:Bluetag/bluetag.git
  ```
  
4. Connect to Bluemix in the command line tool and follow the prompts to log in.

  ```
  $ cf api https://api.ng.bluemix.net
  $ cf login
  ```
  
5. Create a Cloudant NoSQL DB service in Bluemix.

  ```
  $ cf create-service cloudantNoSQLDB Standard bluetag-cloudant
  ```

6. Go into the bluetag-frontend/www directory and edit the `manifest.yml` file and change the `name` and `host` attributes to something unique.

  ```
  applications:
  - name: <prefix>-bluetag
    framework: node
    runtime: node12
    memory: 128M
    instances: 1
    host: <prefix>-bluetag
  ```
  The host you use will determinate your application url initially, e.g. `<host>.mybluemix.net`.
  
7. Push the node application to Bluemix.  

   ```
   $ cf push
   ```

6. Push the Java applications to Bluemix.  Replace the prefix to give your application a globally uniqiue name and host.

   ```
   $ cd ../bluetag-register
   $ cf push <prefix>-bluetag-register -p defaultServer/

   $ cd ../bluetag-location
   $ cf push <prefix>-bluetag-location -p defaultServer/

   $ cd ../bluetag-engine
   $ cf push <prefix>-bluetag-engine -p defaultServer/

   $ cd ../bluetag-tag
   $ cf push <prefix>-bluetag-tag -p defaultServer/
   
   $ cd ../bluetag-search
   $ cf push <prefix>-bluetag-search -p defaultServer/
   ```
   
7. Bind the Java applications with the Cloudant service.

  ```
  $ cf bind-service <prefix>-bluetag-register bluetag-cloudant
  $ cf bind-service <prefix>-bluetag-location bluetag-cloudant
  $ cf bind-service <prefix>-bluetag-engine bluetag-cloudant
  $ cf bind-service <prefix>-bluetag-tag bluetag-cloudant
  $ cf bind-service <prefix>-bluetag-search bluetag-cloudant  
  ```
  
8. Restage the applications to pick up the cloudant environment variables.

   ```
   $ cf restage <prefix>-bluetag-register  
   $ cf restage <prefix>-bluetag-location
   $ cf restage <prefix>-bluetag-engine
   $ cf restage <prefix>-bluetag-tag
   $ cf restage <prefix>-bluetag-search
   ```
   
Congratulations! You now have a live instance of the Bluetag application running in your Bluemix account!

### Troubleshooting

To troubleshoot your Bluemix app the main useful source of information is the logs. To see them, run:

  ```
  $ cf logs <application-name> --recent
  ```

[BluetagURL]: http://bluetag.mybluemix.net/
[bluemix_signup_url]: https://console.ng.bluemix.net/?cm_mmc=Display-GitHubReadMe-_-BluemixSampleApp-PersonalityBox-_-Node-Box-_-BM-DevAd
[cloud_foundry_url]: https://github.com/cloudfoundry/cli
[download_node_url]: https://nodejs.org/download/