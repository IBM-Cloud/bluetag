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

3. Clone the app to your local environment. Since Bluetag is a micro-service sample, all the services have their own github repositories.  
From your terminal using the following commands:


  ```
  mkdir myBluetag
  cd myBluetag
  git clone --recursive git@github.ibm.com:Bluetag/bluetag.git
  ```

4. cd into this newly created directory

### Troubleshooting

To troubleshoot your Bluemix app the main useful source of information is the logs. To see them, run:

  ```
  $ cf logs <application-name> --recent
  ```

[BluetagURL]: http://bluetag.mybluemix.net/
[bluemix_signup_url]: https://console.ng.bluemix.net/?cm_mmc=Display-GitHubReadMe-_-BluemixSampleApp-PersonalityBox-_-Node-Box-_-BM-DevAd
[cloud_foundry_url]: https://github.com/cloudfoundry/cli
[download_node_url]: https://nodejs.org/download/