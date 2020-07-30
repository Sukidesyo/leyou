package com.leyou.controller;

import com.leyou.common.vo.PageResult;
import com.leyou.pojo.Goods;
import com.leyou.pojo.SearchRequest;
import com.leyou.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class searchController {
    @Autowired
    private SearchService searchService;
    @PostMapping("/page")
    public ResponseEntity<PageResult<Goods>> searchGoods(@RequestBody SearchRequest searchRequest)
    {
        return ResponseEntity.ok().body(searchService.searchGoods(searchRequest));
    }
}
