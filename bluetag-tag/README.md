#bluetag-tag service

This service is responsible for handling the tag aspects of the application.  This includes the tagging of both users and locations as well as retrieving the list of tagged users and locations.

For an overview of the Bluetag application, instructions on building and deploying the application, and accessing a live demo please refer to the [overview documentation](../../../blob/master/README.md). 

#APIs exposed by service

bluetag-tag exposes REST services that can be used to tag and retrive users and locations.

```
QUERY TAGGED
Returns a list of users the user has tagged.

Method: GET
URL: <bluetag-search service url>/api/query/tagged/{username}
Example URL: bluetag-register.mybluemix.net/api/query/tagged/John

Response:
	{
		"_id": "username",
		"_rev": "revision code",
		"tagged": ["user1", "user2", ...]
	}
"_rev" should be ignored by the client side. It is used for synchronization. 
```

```
UPDATE TAGGED
Adds a person to a user's tagged list.

url: bluetagtag.mybluemix.net/api/tag
method: PUT
payload: 
	{
		"_id": "username",
		"username": "username of person to tag"
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

```
GET TAGGED
Gets a list of users tagged by the specified user.

url: bluetagtag.mybluemix.net/api/tagged/{username}
method: GET
response: 
	{
  		"_id": “username”,
  		"_rev": “rev”,
  		"tagged": [
  		  “user1”,
   		  “user2”,
		  “…”
  		]
	}
```