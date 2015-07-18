package com.zhite.solr;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;

import java.io.IOException;

public class SolrClient {
	
	public static final String urlString = "http://localhost:8983/solr/smartfit";
	public static HttpSolrClient solrClient = new HttpSolrClient(urlString);
	
	public SolrClient() {
		
	}
	
	public static String query(String params)throws SolrServerException,IOException{
		QueryResponse response = null;
		String result ="";
		SolrQuery parameters = new SolrQuery();
		parameters.set("q", params);
		//parameters.set("qt", "/spellCheckCompRH");
		
		response = solrClient.query(parameters);
		if(response!=null){
			SolrDocumentList list =response.getResults();
			result = list.get(0).get("name").toString();
		}
		return result;
	}

	public static void solrOrder() throws SolrServerException,IOException{
		SolrQuery query = new SolrQuery();
		query.setQuery("*:*");
		query.addSort("id", SolrQuery.ORDER.asc);
		QueryResponse rsp = solrClient.query(query);

	}

	public static void addDoc(){
		SolrInputDocument document = new SolrInputDocument();
		document.addField("id", "552199");
		document.addField("name", "Gouda cheese wheel");
		document.addField("price", "49.99");
		try {
			UpdateResponse response = solrClient.add(document);
			if(response.getStatus()>0){
				//
			}
			// Remember to commit your changes!
			solrClient.commit();
		} catch (SolrServerException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			document.clear();
		}
	}

	public static void likeQuery() throws SolrServerException,IOException{
		SolrQuery query = new SolrQuery();
		query.setQuery("name:*天天*");
		QueryResponse rsp = solrClient.query(query);
	}

	public static void pageQuery() throws SolrServerException,IOException{
		SolrQuery query = new SolrQuery();
		query.setQuery("name:*天天*");
		query.setStart(0);
		query.setRows(10);
		QueryResponse rsp = solrClient.query(query);
	}


	public static void multipleQuery1() throws SolrServerException,IOException{
		SolrQuery query = new SolrQuery();
		query.setQuery("artist:*Tencent* name:*天天*");// 多条件使用空格分隔
		query.setFields("name", "id_in_appstore", "artist");
		QueryResponse rsp = solrClient.query(query);
	}

	public static void multipleQuery2() throws SolrServerException,IOException{
		SolrQuery query = new SolrQuery();
		query.setQuery("name:*天天*");// 多条件使用空格分隔
		query.setFilterQueries("artist:*Tencent*");
		query.setFields("name", "id_in_appstore", "artist");
		QueryResponse rsp = solrClient.query(query);
	}

	public static void main(String[] args)throws Exception{
		//SolrClient solrTest = new SolrClient();
		query("552199");
		//addDoc();
	}
}
