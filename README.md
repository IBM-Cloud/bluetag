# Overview


BlueTag is a mobile application that aims to connect people based on skills and geolocation.  
The app can be used at conferences, work places, and meetups to find and connect with people based on  skills and geo-location.  

As a sample,Bluetag showcases how you can build a set of backend micro-services coupled with an HTML5 mobile enabled front-end application.
The backend is a set of micro-services running in node.js, Liberty Java, and Cloudant DB connected via REST and Web Socket APIs. 
The frontend is based on HTML5 Web Components and will run in any modern browser, in a laptop or on a mobile device.  
It can also be deployed as a native Android or iOS app using  Apache Cordova.

This sample demonstrates how Bluemix allows you to easily bring together and deploy many modern technologies
like Cordova and Polymer for mobile development, Java runtimes in the cloud, Web sockets, REST services, micro services, NoSQL databases, location services and many more  



## How it Works - Live demo service

1. Go to the [live Bluetag demo][BluetagURL] and enter any name to get started. 

2. The map should display with a marker on your current location.  As you move around the marker and map dynamically adjust to your location.

3. You can use the navigation links on the left to do the following:

**Near me:**  lists users who are within 10 meters of you.  Press TAG and the user will be added to your contacts.

**Contacts:**  These are people that you have either tagged when they were near you, or that you added via Search.  You can click the button to show them on your map.

**Places:**  When you press "Mark it" on the map, the location is added on your places.   You can click the link to show this place on your map.

**Search:**  Start typing and Bluetag dynamically searches for user names that start with what you type.   You can select users to add to contacts.

**Settings:**  You can prevent location updates from sending to the location service, and you can toggle logging in the debug console.


### Architecture Diagram

<img src="./bluetag-services.png" width="650px"><br>This an architectural overview of the components that make this app run.<br>

## Running the app on Bluemix

1. Create a Bluemix Account

    [Sign up][bluemix_signup_url] for Bluemix, or use an existing account.

2. Download and install the [Cloud-foundry CLI][cloud_foundry_url] tool

3. Clone the app to your local environment. Since Bluetag is a micro-service sample, all the services have their own githup repositories.  
From your terminal using the following commands:


  ```
  mkdir myBluetag
  cd myBluetag
  git clone https://github.ibm.com/arshadmu/bluetag.git
  git clone https://github.ibm.com/arshadmu/bluetagFrontend.git
  ```

4. cd into this newly created directory

5. Edit the `manifest.yml` file and change the `<application-name>` and `<application-host>` to something unique.

  ```
  applications:
  - name: box-sample-app-test
    framework: node
    runtime: node12
    memory: 128M
    instances: 1
    host: box-sample-app-test
  ```
  The host you use will determinate your application url initially, e.g. `<application-host>.mybluemix.net`.

6. Connect to Bluemix in the command line tool and follow the prompts to log in.

  ```
  $ cf api https://api.ng.bluemix.net
  $ cf login
  ```

7. Create the Personality Insights service in Bluemix.

  ```
  $ cf create-service personality_insights standard personality-insights-box
  ```

8. Push it to Bluemix. We need to perform additional steps once it is deployed, so we will add the option --no-start argument

  ```
  $ cf push --no-start
  ```

9. Next, you need to sign up for a Box developer account if you do not have one already. You can do this [here][box_dev_signup_url].

10. Once you have created an account, select 'Create a Box Application' from the side panel. Name your app, select the Box Content API, and click 'Create Application'. On the next page you will find your API key and your app's client_id and client_secret, which you will need for the following step.

11. Using the credentials you received in step 9, we will create a user-provided service in Bluemix so that our app can leverage them.

  ```
  $ cf cups box -p '{"url":"https://api.box.com","apikey":"BOX_API_KEY","clientId":"BOX_CLIENT_ID","clientSecret":"BOX_CLIENT_SECRET"}'
  ```
Now bind the service to your app.

  ```
  $ cf bind-service APP_NAME box
  ```

12. Finally, we need to restage our app to ensure these env variables changes took effect.

  ```
  $ cf restage APP_NAME
  ```

And voila! You now have your very own instance of Personality Box running on Bluemix.

## Running the app locally

1. Create a Bluemix Account. You will need this to create a Personality Insights service and grab the credentials later on.

    [Sign up][bluemix_signup_url] in Bluemix, or use an existing account.

2. If you have not already, [download node.js][download_node_url] and install it on your local machine.

3. Clone the app to your local environment from your terminal using the following command

  ```
  git clone https://github.com/IBM-Bluemix/box-watson.git
  ```

4. cd into this newly created directory

5. Install the required npm and bower packages using the following command

  ```
  npm install
  ```

6. Next, you need to sign up for a Box developer account if you do not have one already. You can do this [here][box_dev_signup_url].

7. Once you have created an account, select 'Create a Box Application' from the side panel. Name your app, select the Box Content API, and click 'Create Application'. On the next page you will find your app's client_id and client_secret, which you will need for the following step.

8. Copy the file ```vcap-local-example.json``` to ``` vcap-local.json```. Using the credentials you received in step 7, replace the default Box configs in ```vcap-local.json```. After you have done that, create a Personality Insights service using your Bluemix account and replace the corresponding credentials in vcap-local.json.

9. Start your app locally with the following command.

  ```
  npm start
  ```

Your app will be automatically assigned to a port which will be logged to your terminal. To access the app, go to localhost:PORT in your browser. Happy developing!

### Troubleshooting

To troubleshoot your Bluemix app the main useful source of information is the logs. To see them, run:

  ```
  $ cf logs <application-name> --recent
  ```

[BluetagURL]: http://bluetag.mybluemix.net/
[bluemix_signup_url]: https://console.ng.bluemix.net/?cm_mmc=Display-GitHubReadMe-_-BluemixSampleApp-PersonalityBox-_-Node-Box-_-BM-DevAd
[box_signup_url]: https://app.box.com/signup/personal
[box_dev_signup_url]: https://app.box.com/signup/o/default_developer_offer
[cloud_foundry_url]: https://github.com/cloudfoundry/cli
[download_node_url]: https://nodejs.org/download/
[deploy_track_url]: https://github.com/cloudant-labs/deployment-tracker
