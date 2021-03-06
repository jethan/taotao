package com.taotao.web.service;

import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.taotao.common.service.ApiService;
import com.taotao.web.bean.Cart;

@Service
public class CartService {

    @Autowired
    private ApiService apiService;

    @Value("${TAOTAO_CART_UTL}")
    private String TAOTAO_CART_UTL;

    private static final ObjectMapper MAPPER = new ObjectMapper();

    /**
     * @MethodName queryCartListByUserId
     * @Author volc
     * @Description 根据用户ID查询购物侧商品列表
     * @Date 2017年3月3日 上午12:41:19
     * @url http://cart.taotao.com/service/cart?userId=7 访问首先经过cart.taotao项目的拦截器
     * 在拦截器中是获取不到cookie的，原因是www.taotao.com(二级域名)访问不到cart.taotao.com(二级域名)
     * 二级域名可以将cookie写入到主域名下，a.taotao.com -> taotao.com
     * 二级域名之间不能互相写入，a.taotao.com 不能写入到 b.taotao.com
     * 需要将userId传递过去，从表中查询user信息，并不是从cookie中查询用户信息
     */
    public List<Cart> queryCartListByUserId(Long userId) {
	try {
	    String url = TAOTAO_CART_UTL + "/service/cart?userId=" + userId;
	    String jsonData = this.apiService.doGet(url);
	    if (StringUtils.isEmpty(jsonData)) {
		return null;
	    }
	    return MAPPER.readValue(jsonData, MAPPER.getTypeFactory().constructCollectionType(List.class, Cart.class));
	} catch (Exception e) {
	    e.printStackTrace();
	}
	return null;
    }

}
