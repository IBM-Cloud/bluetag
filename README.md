#bluetag-search service

This service is responsible for processing the search queries that a user initiates from the frontend. It takes the user query, does the search in the database, and returns the result back to the frontend.

For more implementation details please refer to the [design documention](../../../bluetag-docs/blob/master/bluetag-backend-implementation-details.md).  For an overview of the Bluetag application and instructions on how to download the source code, build, and deploy this service, please refer to the [overview documentaion](../../../bluetag/blob/master/README.md).

#APIs exposed by service

bluetag-search exposes a service to query the database for specifc entries.

```
Search API: Lookup a user in the database.

URL (WebSocket connection): ws://<bluetag-search service url>/SearchWS
Example URL: ws://bluetag-search.mybluemix.net/SearchWS

Response: 
	{
		"processedSearchResults”:[“user1”,”user2”,”…”]
	}
```