package com.taotao.web.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.taotao.common.service.ApiService;
import com.taotao.common.service.RedisService;
import com.taotao.manage.pojo.ItemDesc;
import com.taotao.web.bean.Item;

/**
 * @ClassName ItemService
 * @Author volc
 * @Description 商品详情操作
 * @Date 2017年2月27日 下午11:02:11
 */
@Service
public class ItemService {

    private static final ObjectMapper MAPPER = new ObjectMapper();
    public static final String REDIS_KEY = "TAOTAO_WEB_ITEM_DETAIL_";
    private static final Integer REDIS_TIME = 60 * 60 * 24;
    @Autowired
    private ApiService apiService;

    @Value("${TAOTAO_MANAGE_URL}")
    private String TAOTAO_MANAGE_URL;

    @Autowired
    private RedisService redisService;

    /**
     * @MethodName queryItemById
     * @Author volc
     * @Description 根据商品ID查询商品的详情
     * @Date 2017年2月27日 下午11:03:45
     */
    public Item queryItemById(Long itemId) {
	String key = REDIS_KEY + itemId;
	try {
	    // 先从缓存中命中，这里可以使用redis，原因是前台系统taotao-web项目中spring有redis的配置
	    String cacheData = this.redisService.get(key);
	    if (StringUtils.isNotEmpty(cacheData)) { // 已命中
		return MAPPER.readValue(cacheData, Item.class);
	    }
	} catch (Exception e1) {
	    e1.printStackTrace();
	}

	try {
	    String url = TAOTAO_MANAGE_URL + "/rest/item/" + itemId;
	    String jsonData = this.apiService.doGet(url);
	    if (StringUtils.isEmpty(jsonData)) {
		return null;
	    }
	    try { // 也要加上trycatch,目的是不能影响正常逻辑的执行
		// 将结果集写入到缓存
		this.redisService.set(key, jsonData, REDIS_TIME);
	    } catch (Exception e) {
		e.printStackTrace();
	    }
	    return MAPPER.readValue(jsonData, Item.class);
	} catch (Exception e) {
	    e.printStackTrace();
	}
	return null;
    }

    /**
     * @MethodName queryItemDescByItemId
     * @Author volc
     * @Description 根据商品的Id查询商品的描述
     * @Date 2017年2月28日 上午12:18:30
     */
    public ItemDesc queryItemDescByItemId(Long itemId) {
	try {
	    String url = TAOTAO_MANAGE_URL + "/rest/item/desc/" + itemId;
	    String jsonData = this.apiService.doGet(url);
	    if (StringUtils.isEmpty(jsonData)) {
		return null;
	    }
	    return MAPPER.readValue(jsonData, ItemDesc.class);
	} catch (Exception e) {
	    e.printStackTrace();
	}
	return null;
    }

    /**
     * @MethodName queryItemParamByItemId
     * @Author volc
     * @Description 商品的规格参数
     * @Date 2017年2月28日 上午12:25:29
     */
    public String queryItemParamByItemId(Long itemId) {
	try {
	    String url = TAOTAO_MANAGE_URL + "/rest/item/param/item/" + itemId;
	    String jsonData = this.apiService.doGet(url);
	    // 解析JSON
	    JsonNode jsonNode = MAPPER.readTree(jsonData);
	    ArrayNode paramData = (ArrayNode) MAPPER.readTree(jsonNode.get("paramData").asText());
	    StringBuilder sb = new StringBuilder();
	    sb.append("<table cellpadding=\"0\" cellspacing=\"1\" width=\"100%\" border=\"0\" class=\"Ptable\"><tbody>");
	    for (JsonNode param : paramData) {
		sb.append("<tr><th class=\"tdTitle\" colspan=\"2\">" + param.get("group").asText() + "</th></tr>");
		ArrayNode params = (ArrayNode) param.get("params");
		for (JsonNode p : params) {
		    sb.append("<tr><td class=\"tdTitle\">" + p.get("k").asText() + "</td><td>" + p.get("v").asText() + "</td></tr>");
		}
	    }
	    sb.append("</tbody></table>");
	    return sb.toString();
	} catch (Exception e) {
	    e.printStackTrace();
	}
	return null;
    }
}
