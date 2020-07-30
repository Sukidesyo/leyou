package com.leyou.web;

import com.leyou.common.vo.PageResult;
import com.leyou.pojo.Sku;
import com.leyou.pojo.Spu;
import com.leyou.pojo.SpuDetail;
import com.leyou.service.GoodsService;
import com.leyou.service.SkuService;
import com.leyou.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@SuppressWarnings("ALL")
@RestController
public class GoodsController {
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private SkuService skuService;
    @Autowired
    private StockService stockService;
    @GetMapping("/spu/page")
    public ResponseEntity<PageResult<Spu>> querySpu(
            @RequestParam(value = "page",defaultValue = "1")Integer page,
            @RequestParam(value = "rows",defaultValue = "5")Integer rows,
            @RequestParam(value = "saleable",required = false)Boolean saleabe,
            @RequestParam(value = "key",required = false)String key
    )
    {
        return ResponseEntity.ok().body(goodsService.querySpu(page,rows,saleabe,key));
    }
    @PostMapping("/goods")
    public ResponseEntity<Void> saveGoods(@RequestBody Spu spu)
    {
        System.out.println("spu=="+spu);
        goodsService.saveGoods(spu);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    @GetMapping("/spu/detail/{spuId}")
    public ResponseEntity<SpuDetail> querySpuBySpuId(@PathVariable Long spuId)
    {
        SpuDetail spuDetail = goodsService.querySpuDetailBySpuId(spuId);
        return ResponseEntity.status(HttpStatus.OK).body(spuDetail);
    }
    @GetMapping("sku/list")
    public ResponseEntity<List<Sku>> querySkuBySpuId(@RequestParam("id")Long spuId)
    {
        List<Sku> skus = goodsService.querySkuBySpuId(spuId);
        return ResponseEntity.ok().body(skus);
    }
    @PutMapping("/goods")
    public ResponseEntity<Void> updateGoods(@RequestBody Spu spu)
    {
    goodsService.updateGoods(spu);
    return ResponseEntity.ok().build();
    }
    @GetMapping("spu/{id}")
    ResponseEntity<Spu> queruSpuById(@PathVariable("id")Long id)
    {
        return ResponseEntity.ok(goodsService.querySpuById(id));
    }
    @GetMapping("sku/list/ids")
    public ResponseEntity<List<Sku>> querySkusToCart(@RequestParam("ids")List<Long> ids)
    {
        List<Sku> list=goodsService.querySkusToCart(ids);
        return ResponseEntity.ok(list);
    }
}
