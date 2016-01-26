#bluetag-location service

This service is responsible for interacting with the frontend device to process the location data of a user over a websocket connection.

For more implementation details please refer to the [design documention](../../../bluetag-docs/blob/master/bluetag-backend-implementation-details.md).  For an overview of the Bluetag application, please refer to the [overview documentaion](../../../bluetag/blob/master/README.md).

#APIs exposed by service

bluetag-location exposes a REST service that can be used to pass the location data of a user and store it in a database.

Service details:

```
method: PUT
url (REST): <bluetag-location service url>/api/location
url (WS)  : ws://<bluetag-location service url>/wsLocationResource

example url: bluetag-location.mybluemix.net/api/location

payload: 
	{
		"_id": "username",
		"longitude": a number (no quotes),
		"latitude": a number (no quotes),
		"altitude": a number (no quotes)
	}
	
response if success:
	{
		"result": "success"
	}
response if failure:
	{	
		"result": "something has gone horribly wrong. Please try again"
	}
```