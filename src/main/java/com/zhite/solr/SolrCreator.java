package com.zhite.solr;

import org.apache.solr.client.solrj.impl.HttpSolrClient;

/**
 * Created by Brainhu on 15/7/18.
 */
public class SolrCreator {

    public static final String urlString = "http://localhost:8983/solr/smartfit";
    public static HttpSolrClient solrClient = null;

    SolrCreator(){
        new HttpSolrClient(urlString);
    }

    SolrCreator(String url){
        new HttpSolrClient(url);
    }

}
