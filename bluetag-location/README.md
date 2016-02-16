#bluetag-location service

This service is responsible for interacting with the frontend device to process the location data of a user over a websocket connection.

For implementation and design details, please refer to the [design documention](../../../bluetag-docs/blob/master/bluetag-backend-implementation-details.md).  For an overview of the Bluetag application, instructions on building and deploying the application, and accessing a live demo please refer to the [overview documentation](../../../bluetag/blob/master/README.md).

#APIs exposed by service

bluetag-location exposes a REST service that can be used to pass the location data of a user and store it in a database.

```
Location API: API to update the location of a user in the database.

Method: PUT
URL (REST): <bluetag-location service url>/api/location
URL (WS)  : ws://<bluetag-location service url>/wsLocationResource
Example URL: bluetag-location.mybluemix.net/api/location

Payload: 
	{
		"_id": "username",
		"longitude": a number (no quotes),
		"latitude": a number (no quotes),
		"altitude": a number (no quotes)
	}
	
Response if success:
	{
		"result": "success"
	}
Response if failure:
	{	
		"result": "something has gone horribly wrong. Please try again"
	}
```