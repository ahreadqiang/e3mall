package jd.service;

import java.util.List;

import jd.bean.ProductModel;
import jd.bean.QueryVo;

public interface ProductService {
	public List<ProductModel> getResultModelFormSolr(QueryVo vo) throws Exception;
}
