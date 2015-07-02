package example.nosql;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.cloudant.client.api.Database;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.internal.LinkedTreeMap;

import example.nosql.CloudantClientMgr;



@Path("/favorites")
/**
 * CRUD service of todo list table. It uses REST style.
 *
 */
public class ResourceServlet {



	public ResourceServlet() {

	}

	@POST @Path("/attach")
	@Consumes("multipart/form-data")
	public Response create(@FormParam("file") File theFile, @QueryParam("id")  Long id, @QueryParam("name")  String name, @QueryParam("value")  String value, @QueryParam("filename")  String fname) throws Exception {
		byte[] bytes = null;
		JsonObject resultObject = new JsonObject();
		String fileName = null;
		String contentType = null;
		
		
		 if (theFile.isFile()) {
	            try {
	                //bytes = fileParts.getBytes();
	            	
	                contentType = Files.probeContentType(theFile.toPath());
	                fileName = fname;            
	            }
	            catch (Exception e) {
	            	e.printStackTrace();
	            	return Response.status(javax.ws.rs.core.Response.Status.INTERNAL_SERVER_ERROR).build();
	            }
		 }
		
		
		/*
		InputStream fileInputStream = new ByteArrayInputStream(bytes);
		*/
		InputStream fileInputStream = new FileInputStream(theFile);
		
		Database db = null;
		try
		{
			db = getDB();
		}
		catch(Exception re)
		{
			re.printStackTrace();
			return Response.status(javax.ws.rs.core.Response.Status.INTERNAL_SERVER_ERROR).build();
		}

		//check if document exist
		HashMap<String, Object> obj = (id==-1?null:db.find(HashMap.class , id+""));

		if(obj==null)
		{ // if new document
			
			id = System.currentTimeMillis();
			
			//create a new document
			System.out.println("Creating new document with id : "+id);
			Map<String, Object> data = new HashMap<String, Object>();
			data.put("name", name);			
			data.put("_id", id+"");
			data.put("value", value);
			data.put("creation_date", new Date().toString());
			db.save(data);	
			
			//attach the attachment object
			obj = db.find(HashMap.class , id+"");
			db.saveAttachment(fileInputStream, fileName, contentType, id+"",  (String)obj.get("_rev"));
		}
		else
		{ // if existing document
			//attach the attachment object
			db.saveAttachment(fileInputStream, fileName, contentType, id+"",  (String)obj.get("_rev"));
			
			//update other fields in the document
			obj = db.find(HashMap.class , id+"");
			obj.put("name", name);
			obj.put("value", value);
			db.update(obj);
			
		}	
		
		
		fileInputStream.close();
		
						
		System.out.println("Upload completed....");
		
		//get attachments
		obj = db.find(HashMap.class , id+"");		
		LinkedTreeMap<String, Object> attachments = (LinkedTreeMap<String, Object>)obj.get("_attachments");
		
		if(attachments!=null && attachments.size()>0)
		{
			JsonArray attachmentList = getAttachmentList(attachments, id+"");
			resultObject.add("attachements", attachmentList);
		}
		resultObject.addProperty("id", id);
		resultObject.addProperty("name", name);	
		resultObject.addProperty("value", value);
		System.out.println("OK : " + resultObject.toString());
		return Response.ok(resultObject.toString()).build();
		
	}
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response get(@QueryParam("id") Long id, @QueryParam("cmd") String cmd ) throws Exception {

		
		Database db = null;
		try
		{
			db = getDB();
		}
		catch(Exception re)
		{
			re.printStackTrace();
			return Response.status(javax.ws.rs.core.Response.Status.INTERNAL_SERVER_ERROR).build();
		}
		
		JsonObject resultObject = new JsonObject();
		JsonArray jsonArray = new JsonArray();		
			
		if( id == null ){			
			try
			{
				//get all the document present in database
				List<HashMap> allDocs = db.view("_all_docs").query(HashMap.class); 
				
				if(allDocs.size()==0)
				{
					allDocs = initializeSampleData(db);
				}
				
				for(HashMap doc : allDocs)
				{
					
					HashMap<String, Object> obj = db.find(HashMap.class, doc.get("id")+"");
					JsonObject jsonObject = new JsonObject();					
					LinkedTreeMap<String, Object> attachments = (LinkedTreeMap<String, Object>) obj.get("_attachments");		
										
					if(attachments!=null && attachments.size()>0)
					{	
						JsonArray attachmentList = getAttachmentList(attachments, obj.get("_id")+"");
						jsonObject.addProperty("id", obj.get("_id")+"");
						jsonObject.addProperty("name", obj.get("name")+"");
						jsonObject.addProperty("value", obj.get("value")+"");
						jsonObject.add("attachements", attachmentList);
						
					}
					else
					{
						jsonObject.addProperty("id", obj.get("_id")+"");
						jsonObject.addProperty("name", obj.get("name")+"");
						jsonObject.addProperty("value", obj.get("value")+"");
					}
					
					jsonArray.add(jsonObject);
				}
			
			}
			catch(Exception dnfe)
			{
				System.out.println("Exception thrown : "+ dnfe.getMessage());
			}
			
			resultObject.addProperty("id", "all");
			resultObject.add("body", jsonArray);			
		
			return Response.ok(resultObject.toString()).build();
						
		}
		
		
		//check if document exists
		HashMap<String, Object> obj = db.find(HashMap.class , id+"");
				
				
		if(obj!=null)
		{
			JsonObject jsonObject = new JsonObject();			
			LinkedTreeMap<String, Object> attachments = (LinkedTreeMap<String, Object>)obj.get("_attachments");
			
			if(attachments!=null && attachments.size()>0)
			{
				JsonArray attachmentList = getAttachmentList(attachments, obj.get("_id")+"");
				jsonObject.add("attachements", attachmentList);
			}
			jsonObject.addProperty("id", obj.get("_id")+"");
			jsonObject.addProperty("name", obj.get("name")+"");
			jsonObject.addProperty("value", obj.get("value")+"");
			return Response.ok(jsonObject.toString()).build();
			
		}
		else
			return Response.status(javax.ws.rs.core.Response.Status.NOT_FOUND).build();
			
				
	
	}
	
	   @DELETE
	   public Response delete(@QueryParam("id") long id) {
			boolean recordFound = true;
			Database db = null;
			try
			{
				db = getDB();
			}
			catch(Exception re)
			{
				re.printStackTrace();
				return Response.status(javax.ws.rs.core.Response.Status.INTERNAL_SERVER_ERROR).build();
			}
			//check if document exist
			HashMap<String, Object> obj = db.find(HashMap.class , id+"");
			
			if(obj==null)
				recordFound = false;
			else
			db.remove(obj);
			System.out.println("Delete Successful...");
			
			
			if(recordFound){			
				return Response.ok("OK").build();
			} else
				return Response.status(javax.ws.rs.core.Response.Status.NOT_FOUND).build();
		}
		
		@PUT
		public Response update(@QueryParam("id")  long id, @QueryParam("name")  String name, @QueryParam("value")  String value) {
			boolean recordFound = true;
			Database db = null;
			try
			{
				db = getDB();
			}
			catch(Exception re)
			{
				re.printStackTrace();
				return Response.status(javax.ws.rs.core.Response.Status.INTERNAL_SERVER_ERROR).build();
			}
			
			//check if document exist
			HashMap<String, Object> obj = db.find(HashMap.class , id+"");
			
			if(obj==null)
				recordFound = false;
			else
			{
				obj.put("name", name);
				obj.put("value", value);
			}
			
			db.update(obj);
			System.out.println("Update Successful...");
			
					
				
			if(recordFound){			
				return Response.ok("OK").build();
			} else
				return Response.status(javax.ws.rs.core.Response.Status.NOT_FOUND).build();
		}
	    
	
	private JsonArray getAttachmentList(LinkedTreeMap<String, Object> attachmentList, String docID) throws Exception
	{
		
		JsonArray attachmentArray = new JsonArray();
		String URLTemplate = "http://"+CloudantClientMgr.getUser()+":"+CloudantClientMgr.getPassword()+"@"+CloudantClientMgr.getHost()+"/"+CloudantClientMgr.getDatabaseName()+"/";
		
		for(Object key : attachmentList.keySet())
		{
			LinkedTreeMap<String, Object> attach = (LinkedTreeMap<String, Object>)attachmentList.get(key);	
			
			JsonObject attachedObject = new JsonObject();
			//set the content type of the attachment
			attachedObject.addProperty("content_type", attach.get("content_type").toString());
			//append the document id and attachment key to the URL
			attachedObject.addProperty("url", URLTemplate+docID+"/"+key);
			//set the key of the attachment
			attachedObject.addProperty("key", key+"");
			
			//add the attachment object to the array
			attachmentArray.add(attachedObject);
		}
		
		return attachmentArray;
		
	}
	
	/*
	 * Create a document and Initialize with sample data/attachments
	 */
	private List<HashMap> initializeSampleData(Database db) throws Exception
	{
				
		long id = System.currentTimeMillis();
		String name = "Sample category";;
		String value = "List of sample files";
		
		//create a new document
		System.out.println("Creating new document with id : "+id);
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("name", name);			
		data.put("_id", id+"");
		data.put("value", value);
		data.put("creation_date", new Date().toString());
		db.save(data);	
		
		//attach the object
		HashMap<String, Object> obj = db.find(HashMap.class , id+"");		
		
		//attachment#1
		File file = new File("Sample.txt");
		file.createNewFile();		
		PrintWriter writer = new PrintWriter(file);
		writer.write("This is a sample file...");
		writer.flush();
		writer.close();
		FileInputStream fileInputStream = new FileInputStream(file);		
		db.saveAttachment(fileInputStream, file.getName(), "text/plain", id+"", (String)obj.get("_rev"));
		fileInputStream.close();
		
		List<HashMap> allDocs = db.view("_all_docs").query(HashMap.class); 
		return allDocs;
			
	}
	

	private Database getDB()
	{
		return CloudantClientMgr.getDB();
	}

}
