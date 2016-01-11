#BluetagSearch Web Application

This project contains the WebApp that implements the Bluetag user search service.

BlueMix needs to know the Liberty server.xml and the WAR that is being deployed.
We provide a ready to use server.xml and /apps directory within the Eclipse project.
The app name must be myapp.war to match the default server.xml.

Directory structure:
```
	BluetagSearch/
		/defaultServer/
			server.xml
			/apps/myapp.war
```
##  First time user:
You may need to install Cloud Foundry command line or login.
See (https://www.ng.bluemix.net/docs/starters/install_cli.html)


##Create the WAR:

	From eclipse: File -> export -> WAR
		- uncheck "optimize for a specific server runtime"
		WAR destination:   BluetagSearch/defaultServer/apps/myapp.war

##Deploy to BlueMix:

Use Cloud Foundry cf command to push the app to your service instance. In the example below our service is called "BluetagSearch1".

Do the following:

```
cd BluetagSearch
cf push BluetagSearch1 -p defaultServer/
```

You should see output similar to this:

```
Updating app BluetagSearch1 in org arshadmu@us.ibm.com / space dev as seelbach@us.ibm.com...
OK

Uploading BluetagSearch1...
Uploading app files from: defaultServer/
Uploading 1.4M, 3 files
Done uploading               
OK

Stopping app BluetagSearch1 in org arshadmu@us.ibm.com / space dev as seelbach@us.ibm.com...
OK

Starting app BluetagSearch1 in org arshadmu@us.ibm.com / space dev as seelbach@us.ibm.com...
-----> Downloaded app package (1.4M)
-----> Downloaded app buildpack cache (4.0K)
-----> Liberty Buildpack Version: v2.1-20151006-0912
-----> Retrieving IBM 1.8.0_20150828 JRE (ibm-java-jre-8.0-1.11-pxa6480sr1fp11-20150828_01-cloud.tgz) ... (0.0s)
         Expanding JRE to .java ... (0.8s)
-----> Retrieving App Management 1.12.0_20151001-1521 (app-mgmt_v1.12-20151001-1521.zip) ... (0.0s)
         Expanding App Management to .app-management (0.1s)
-----> Retrieving com.ibm.ws.liberty-2015.10.0.0-201510060912.tar.gz ... (0.0s)
         Installing archive ... (0.8s)
-----> Retrieving com.ibm.ws.liberty.ext-2015.10.0.0-201510060912.tar.gz ... (0.0s)
         Installing archive ... (1.1s)
-----> Liberty buildpack is done creating the droplet

-----> Uploading droplet (184M)

0 of 1 instances running, 1 starting
1 of 1 instances running

App started
OK

App BluetagSearch1 was started using this command `.liberty/initial_startup.rb`

Showing health and status for app BluetagSearch1 in org arshadmu@us.ibm.com / space dev as seelbach@us.ibm.com...
OK

requested state: started
instances: 1/1
usage: 512M x 1 instances
urls: bluetagsearch1.mybluemix.net
last uploaded: Tue Oct 20 21:45:01 UTC 2015
stack: cflinuxfs2
buildpack: Liberty for Java(TM) (SVR-DIR, liberty-2015.10.0_0, ibmjdk-1.8.0_20150828, env)

     state     since                    cpu    memory           disk           details   
 0   running   2015-10-20 05:46:20 PM   0.0%   193.4M of 512M   244.9M of 1G 
```

##To tail the Liberty log:

```
cf logs BluetagSearch
```

##Running local
Set the following environment variables to allow a service running locally to be able to communicate with a remote Cloudant Database running in Bluemix. 

	dbUsername=(Cloudant username)
	dbPassword=(Cloudant password)
	dbURI=(Cloudant URL)

Deploy a Cloudant instance in Bluemix and bind to a running service. Click on the service to which Cloudant is bound and select 'Environment Variables' from the left hand panel. Under VCAP_SERVICES, look at the JSON section labeled 'cloudantNoSQLDB'. You should see fields named "username", "password" and "url". Assign these values as local environment variables to "dbUsername", "dbPassword" and "dbURI" - no parentheses. Your local service should now be able to communicate with your Cloudant instance.
