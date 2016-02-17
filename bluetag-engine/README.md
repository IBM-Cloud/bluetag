#bluetag-engine service

This service is responsible for monitoring the location of all the users and calculating the distance between the users.  The UI queries this service to get the list of taggable users everytime a user's location changes.

For an overview of the Bluetag application, instructions on building and deploying the application, and accessing a live demo please refer to the [overview documentation](../../../blob/master/README.md).

#APIs exposed by service

bluetag-engine exposes a REST service to get a list of people who are in a 10 meter radius of the user passed in.

```
Taggable API: Get a list of taggable users for a specified user.

Method: GET
URL: <bluetag-engine service url>/api/taggable/{username}
Example URL: bluetag-engine.mybluemix.net/api/taggable/John

Response:
	{
		"taggable": ["user1", "user2", …],
		“distances”:[“user1distance”, “user2distance”, …]
	}
```