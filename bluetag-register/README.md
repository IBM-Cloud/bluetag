#bluetag-register service

This service is responsible for handling the user registration / login aspect of the application.

For implementation and design details, please refer to the [design documention](../../../bluetag-docs/blob/master/bluetag-backend-implementation-details.md).  For an overview of the Bluetag application, instructions on building and deploying the application, and accessing a live demo please refer to the [overview documentation](../../../blob/master/README.md).

#APIs exposed by service

bluetag-register exposes a few APIs to handle user registration and looking up details of a user.

```
Registration API: Check if the user passed in already exists in the database. If not, create an entry for that user. 

Method: POST
URL: <bluetag-search service url>/api/register
Example URL: bluetag-register.mybluemix.net/api/register

Payload:
	{
		"_id": "username"
		"name": "Actual Name"
	}
Response if success:
	{
		"result": "success"
	}
Response if user already exists:
	{
		"result": "user already exists"
	}
Response if other failure:
	{	
		"result": "something has gone horribly wrong. Please try again"
	}	
```

```
User query API: Get info about the user from the database.

Method: GET
URL: <bluetag-search service url>/api/query/{username}
Example URL: bluetag-register.mybluemix.net/api/query/John

Response: 
	{
		"_id": "username",
		"_rev": "revision code",
		"name": "Actual Name"
	}
	
"_rev" should be ignored by the client side. It is used for synchronization. 
```
