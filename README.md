#bluetag-search service

This service is responsible for processing the search queries that a user initiates from the frontend. It takes the user query, does the search in the database, and returns the result back to the frontend.

For more implementation details please refer to the [design documention](../../../bluetag-docs/blob/master/bluetag-backend-implementation-details.md).  For an overview of the Bluetag application, please refer to the [overview documentaion](../../../bluetag/blob/master/README.md).

#APIs exposed by service

bluetag-search exposes a REST service to query the database for specifc entries.

Service details:

```
url (WS): ws://<bluetag-search service url>/SearchWS

example url: ws://bluetag-search.mybluemix.net/SearchWS

response: 
	{
		"processedSearchResults”:[“user1”,”user2”,”…”]
	}
```