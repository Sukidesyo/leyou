package com.leyou.api;

import com.leyou.common.vo.PageResult;
import com.leyou.pojo.Sku;
import com.leyou.pojo.Spu;
import com.leyou.pojo.SpuDetail;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public interface GoodsApi {
    @GetMapping("/spu/page")
    PageResult<Spu> querySpuForPage(
            @RequestParam(value = "page",defaultValue = "1")Integer page,
            @RequestParam(value = "rows",defaultValue = "5")Integer rows,
            @RequestParam(value = "saleable",required = false)Boolean saleabe,
            @RequestParam(value = "key",required = false)String key
    );
    @PostMapping("/goods")
    Void saveGoods(@RequestBody Spu spu);
    @GetMapping("/spu/detail/{spuId}")
    SpuDetail querySpuDetailBySpuId(@PathVariable Long spuId);
    @GetMapping("sku/list")
    List<Sku> querySkuListBySpuId(@RequestParam("id")Long spuId);
    @PutMapping("/goods")
    Void updateGoods(@RequestBody Spu spu);
    @GetMapping("spu/{id}")
    Spu querySpuBySpuId(@PathVariable("id")Long id);
}
