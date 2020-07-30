package com.leyou.api;

import com.leyou.common.vo.PageResult;
import com.leyou.pojo.Brand;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/brand")
public interface BrandApi {
    @GetMapping("/page")
    PageResult<Brand> queryBrandByPage(
            @RequestParam(value = "page",defaultValue = "1")Integer page,
            @RequestParam(value = "rows",defaultValue = "5")Integer rows,
            @RequestParam(value = "sortBy",required = false)String sortBy,
            @RequestParam(value = "desc",defaultValue = "false")Boolean desc,
            @RequestParam(value = "key",required = false)String key
    );
    @PostMapping
    Void saveBrand(Brand brand, @RequestParam(name="cids",required = true) List<Long> cids);
    @GetMapping("/cid/{cid}")
    List<Brand> queryBrandByCid(@PathVariable("cid")Long cid);
    @GetMapping("{id}")
    Brand queryBrandById(@PathVariable("id") Long id);

}
