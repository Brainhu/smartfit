package com.zhite.solr;

import lombok.extern.slf4j.Slf4j;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;

/**
 * Created by Brainhu on 15/7/18.
 */
@Slf4j
public class SolrHelper {

    /**
     * 查询总入口
     *
     * @param fields
     *            查询字段
     * @param values
     *            查询key值 field:key
     * @param start
     *            起始位置
     * @param count
     *            读取总数
     * @param sortfields
     *            排序字段
     * @param flags
     *            排序标志
     * @param fecteField 分面统计字段
     * @return QueryResponse
     */

    public static QueryResponse search(String[] fields, String[] values,
                                       String[] fqs, String[] fqValues, int start, int count,
                                       String[] sortfields, Boolean[] flags,String[]fecteField) {
        HttpSolrClient solr = new SolrCreator().solrClient;
        // 检测输入是否合法
        if (null == fields || null == values || fields.length != values.length) {
            return null;
        }
        if (null == sortfields || null == flags
                || sortfields.length != flags.length) {
            return null;
        }
        SolrQuery query = null;
        try {
            // 初始化查询对象
            query = new SolrQuery();
            query.setQuery(fields[0] + ":" + values[0]);
            // 设置起始位置与返回结果数
            if (start!=0) {
                query.setStart(start);
            }
            if (count!=0) {
                query.setRows(count);
            }
            if (null!=fecteField) {
                query.setFacet(true);
                query.setFacetLimit(20);
                query.setFacetMinCount(1);
                query.addFacetField(fecteField);
            }
            boolean isFq = false;
            if (fqs != null && fqs.length > 0) {
                if (fqs.length == fqValues.length) {
                    isFq = true;
                }
            }
            if (isFq) {
                for (int i = 0; i < flags.length; i++) {
                    String fq = fqs[i] + ":" + fqValues[i];
                    query.setFilterQueries(fq);
                }
            }
            // 设置排序
            for (int i = 0; i < sortfields.length; i++) {
                if (flags[i]) {
                    query.addSort(sortfields[i], SolrQuery.ORDER.asc);
                } else {
                    query.addSort(sortfields[i], SolrQuery.ORDER.desc);
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        QueryResponse rsp = null;
        try {
            rsp = solr.query(query);
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
        // 返回查询结果
        return rsp;
    }
}
