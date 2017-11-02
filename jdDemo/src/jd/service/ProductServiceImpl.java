package jd.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jd.bean.ProductModel;
import jd.bean.QueryVo;
import jd.dao.SolrDao;

@Service
public class ProductServiceImpl implements ProductService {
	@Autowired
	private SolrDao solrDao;
	
	public List<ProductModel> getResultModelFormSolr(QueryVo vo) throws Exception{
		return solrDao.getResultModelFromSolr(vo);
	}
}
