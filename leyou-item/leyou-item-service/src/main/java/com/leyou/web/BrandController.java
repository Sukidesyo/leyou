package com.leyou.web;

import com.leyou.common.vo.PageResult;
import com.leyou.pojo.Brand;
import com.leyou.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@SuppressWarnings("ALL")
@RestController
@RequestMapping("/brand")
public class BrandController {
    @Autowired
    private BrandService brandService;
    @GetMapping("/page")
    public ResponseEntity<PageResult<Brand>> queryBrandByPage(
            @RequestParam(value = "page",defaultValue = "1")Integer page,
            @RequestParam(value = "rows",defaultValue = "5")Integer rows,
            @RequestParam(value = "sortBy",required = false)String sortBy,
            @RequestParam(value = "desc",defaultValue = "false")Boolean desc,
            @RequestParam(value = "key",required = false)String key
    )
    {
        PageResult<Brand> brandPageResult = brandService.queryBrandByPage(page, rows, sortBy, desc, key);

        return ResponseEntity.ok(brandPageResult);
    }

    @PostMapping
    public ResponseEntity<Void> saveBrand(Brand brand, @RequestParam(name="cids",required = true)List<Long> cids)
    {
        brandService.saveBrand(brand,cids);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    @GetMapping("/cid/{cid}")
    public ResponseEntity<List<Brand>> queryBrandByCid(@PathVariable("cid")Long cid)
    {
        List<Brand> list = brandService.queryBranByCid(cid);
        return ResponseEntity.ok().body(list);
    }
    @GetMapping("{id}")
    public ResponseEntity<Brand> queryBrandById(@PathVariable("id") Long id)
    {
        return ResponseEntity.ok().body(brandService.queryBrandByBid(id));
    }

}
