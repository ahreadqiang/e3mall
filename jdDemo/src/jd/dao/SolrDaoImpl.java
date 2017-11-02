package jd.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrQuery.ORDER;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import jd.bean.ProductModel;
import jd.bean.QueryVo;

@Repository
public class SolrDaoImpl implements SolrDao{
	
	//获得配置好的solrService远程连接索引库对象
	@Autowired
	private SolrServer solrServer;
	//获得页面查询的结果
	public List<ProductModel> getResultModelFromSolr(QueryVo vo) throws Exception {
		//创建查询对象
		SolrQuery solrQuery = new SolrQuery();
		// 关键词
		solrQuery.setQuery(vo.getQueryString());
		// 过滤条件  分类
		if(null != vo.getCatalog_name() && !"".equals(vo.getCatalog_name())){
			solrQuery.set("fq", "product_catalog_name:" + vo.getCatalog_name());
			//相当于
		}
		//价格
		if(null != vo.getPrice() && !"".equals(vo.getPrice())){
			String[] p = vo.getPrice().split("-");
			solrQuery.set("fq", "product_price:[" + p[0] + " TO " + p[1] + "]");
			//相当于 solrQuery.addFilterQuery("product_price:[" + split[0] + " TO " + split[1] + "]");
		}
		//排序
		if("1".equals(vo.getSort())){
			solrQuery.addSort("product_price", ORDER.desc);
		}else{
			solrQuery.addSort("product_price", ORDER.asc);
		}
		// 分页
		solrQuery.setStart(0);
		solrQuery.setRows(16);
		// 默认域
		solrQuery.set("df", "product_keywords");
		// 只查询指定域
		solrQuery.set("fl", "id,product_name,product_price,product_picture");
		//相当于	//solrQuery.setFields("id,product_name,product_price,product_picture");
		// 高亮
		// 打开开关
		solrQuery.setHighlight(true);
		// 指定高亮域
		solrQuery.addHighlightField("product_name");
		// 前缀
		solrQuery.setHighlightSimplePre("<span style='color:red'>");
		solrQuery.setHighlightSimplePost("</span>");
		QueryResponse response = solrServer.query(solrQuery);
		SolrDocumentList docs = response.getResults();
		Map<String, Map<String, List<String>>> highlighting = response.getHighlighting();
		long numFound = docs.getNumFound();
		List<ProductModel> productModels = new ArrayList<ProductModel>();
		for (SolrDocument doc : docs) {
			ProductModel productModel = new ProductModel();
			productModel.setPid((String) doc.get("id"));
			productModel.setPrice((Float) doc.get("product_price"));
			productModel.setPicture((String) doc.get("product_picture"));
			Map<String, List<String>> map = highlighting.get((String) doc.get("id"));
			List<String> list = map.get("product_name");
			if(list !=null){
				productModel.setName(list.get(0));
			}
			productModels.add(productModel);
		}
		return productModels;
	}
	
}
