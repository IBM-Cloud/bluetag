#BluetagLocation Service - Handle Location Updates#

###Available APIs###



###Running local###

Set the following environment variables to allow a service running locally to be able to communicate with a remote Cloudant Database running in Bluemix. 

	dbUsername=(Cloudant username)
	dbPassword=(Cloudant password)
	dbURI=(Cloudant URL)

Deploy a Cloudant instance in Bluemix and bind to a running service. Click on the service to which Cloudant is bound and select 'Environment Variables' from the left hand panel. Under VCAP_SERVICES, look at the JSON section labeled 'cloudantNoSQLDB'. You should see fields named "username", "password" and "url". Assign these values as local environment variables to "dbUsername", "dbPassword" and "dbURI" - no parentheses. Your local service should now be able to communicate with your Cloudant instance.
