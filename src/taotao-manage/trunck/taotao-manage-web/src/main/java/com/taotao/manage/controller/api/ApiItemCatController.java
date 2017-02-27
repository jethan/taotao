package com.taotao.manage.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.taotao.common.bean.ItemCatResult;
import com.taotao.manage.service.ItemCatService;

/**
 * @ClassName ApiItemCatController
 * @Description 
 * @Author youtanzhi
 * @Date 2017年2月14日 下午9:29:43
 * url: http://manage.taotao.com/rest/api/item/cat
 */
@RequestMapping("api/item/cat")
@Controller
public class ApiItemCatController {

    @Autowired
    private ItemCatService itemCatService;
    // private static final ObjectMapper MAPPER = new ObjectMapper();

    /**
     * @Title queryItemCat
     * @Description 对外提供查询类目借口数据
     * @return ResponseEntity<ItemCatResult>
     * @throws 
     */
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<ItemCatResult> queryItemCat(@RequestParam("callback") String callback) {
	try {
	    ItemCatResult itemCatResult = this.itemCatService.queryAllToTree();
	    return ResponseEntity.ok(itemCatResult);
	} catch (Exception e) {
	    e.printStackTrace();
	}
	return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
    // @RequestMapping(method = RequestMethod.GET)
    // public ResponseEntity<String> queryItemCat(@RequestParam("callback")
    // String callback) {
    // try {
    // ItemCatResult itemCatResult = this.itemCatService.queryAllToTree();
    // String json = MAPPER.writeValueAsString(itemCatResult);
    // if (callback.isEmpty()) {
    // return ResponseEntity.ok(json);
    // }
    // return ResponseEntity.ok(callback + "(" + json + ");");
    // } catch (Exception e) {
    // e.printStackTrace();
    // }
    // return
    // ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    // }
}
