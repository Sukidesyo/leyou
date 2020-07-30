package com.leyou.Service;

import com.leyou.client.BrandClient;
import com.leyou.client.CategoryClient;
import com.leyou.client.GoodsClient;
import com.leyou.client.SpecClient;
import com.leyou.pojo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.File;
import java.io.PrintWriter;
import java.util.*;
@Service
public class PageService {
    @Autowired
    private BrandClient brandClient;

    @Autowired
    private CategoryClient categoryClient;

    @Autowired
    private GoodsClient goodsClient;
    @Autowired
    private SpecClient specClient;
    @Autowired
    private TemplateEngine templateEngine;
    public Map<String,Object> loadModel(Long spuId)
    {
        Map<String, Object> model = new HashMap<>();
        Spu spu = goodsClient.querySpuBySpuId(spuId);

        //上架未上架，则不应该查询到商品详情信息，抛出异常
        SpuDetail detail = spu.getSpuDetail();
        List<Sku> skus = spu.getSkus();
        Brand brand = brandClient.queryBrandById(spu.getBrandId());
        //查询三级分类
        List<Category> categories = categoryClient.queryNamesById(Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3()));

        List<SpecGroup> specs = specClient.queryGroupsListByCid(spu.getCid3());

        model.put("brand", brand);
        model.put("categories", categories);
        model.put("spu", spu);
        model.put("skus", skus);
        model.put("detail", detail);
        model.put("specs", specs);
        return model;
    }
    public void createHtml(Long spuId)
    {
        Context context = new Context();
        context.setVariables(loadModel(spuId));
        File dest=new File("D:/UserData/Desktop/html",spuId+".html");
        try (PrintWriter writer=new PrintWriter(dest,"UTF-8")){
            templateEngine.process("item",context,writer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
