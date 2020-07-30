package com.leyou.api;

import com.leyou.pojo.Category;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RequestMapping("/category")
public interface CategoryApi {
    @RequestMapping("list")
    List<Category> queryCategoryListByPid(@RequestParam(value = "pid", defaultValue = "0") Long pid);
    @GetMapping("/list/h")
    List<Category> queryNamesById(@RequestParam("ids")List<Long> ids);
    @GetMapping("/list/ids")
    List<String> queryNamesListByIds(@RequestParam("ids")List<Long> ids);

}
