package jd.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import jd.bean.ProductModel;
import jd.bean.QueryVo;
import jd.service.ProductService;

@Controller
public class ProductController {
	
	@Autowired
	private ProductService productService;
	
	
	@RequestMapping(value="/list.action")
	public String list(QueryVo vo,Model model) throws Exception{
		
		//取出结果集
		List<ProductModel> list = productService.getResultModelFormSolr(vo);
		model.addAttribute("productList",list);
		//对页面进行回显 假若不重写关键字的情况下
		model.addAttribute("catalog_name", vo.getCatalog_name());
		model.addAttribute("price", vo.getPrice());
		model.addAttribute("sort", vo.getSort());
		model.addAttribute("queryString", vo.getQueryString());
		//model.addAttribute("picture", vo.getPicture());
		return "product_list";
	}
	
}
