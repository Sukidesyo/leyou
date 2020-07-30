package com.leyou.web;

import com.leyou.pojo.Category;
import com.leyou.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @RequestMapping("/list")
    public ResponseEntity<List<Category>> queryCategoryListByPid(@RequestParam(value = "pid", defaultValue = "0") Long pid) {
        List<Category> categories = categoryService.queryCategoryListByPid(pid);
        if (CollectionUtils.isEmpty(categories)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(categories);

    }
    @GetMapping("/list/h")
    public ResponseEntity<List<Category>> queryNamesById(@RequestParam("ids")List<Long> ids)
    {
        List<Category> categories = categoryService.queryByIds(ids);
        return ResponseEntity.ok().body(categories);
    }
    @GetMapping("/list/ids")
    public ResponseEntity<List<String>> queryNamesListByIds(@RequestParam("ids")List<Long> ids)
    {
        List<String> names = categoryService.queryNamesListByIds(ids);
        return ResponseEntity.ok(names);
    }
}
