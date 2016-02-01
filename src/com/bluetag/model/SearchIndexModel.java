package com.bluetag.model;

public class SearchIndexModel {
	
	private String _id = "_design/info";
	private ViewModel views = new ViewModel();
	private String language = "javascript";
	private IndexesModel indexes = new IndexesModel();
	
	public class ViewModel {
	}

	public class IndexesModel {
		private NameSearchModel nameSearch = new NameSearchModel();
	}

	public class NameSearchModel {
		private String analyzer = "standard";
		private String index = "function (doc) {\n  index(\"name\", doc.name, {\"store\": true, \"index\": true, \"facet\": true});\n}";
	}

}





