package jd.dao;

import java.util.List;

import jd.bean.ProductModel;
import jd.bean.QueryVo;

public interface SolrDao {
	public List<ProductModel> getResultModelFromSolr(QueryVo vo) throws Exception;
}
