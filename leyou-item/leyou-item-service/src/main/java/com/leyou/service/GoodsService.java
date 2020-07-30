package com.leyou.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.leyou.common.vo.PageResult;
import com.leyou.mapper.SkuMapper;
import com.leyou.mapper.SpuDetailMapper;
import com.leyou.mapper.SpuMapper;
import com.leyou.mapper.StockMapper;
import com.leyou.pojo.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@SuppressWarnings("ALL")
@Service
public class GoodsService {
    @Autowired
    private SpuMapper spuMapper;
    @Autowired
    private SpuDetailMapper spuDetailMapper;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private BrandService brandService;
    @Autowired
    private SkuMapper skuMapper;
    @Autowired
    private StockMapper stockMapper;
    @Autowired
    private AmqpTemplate amqpTemplate;
    public PageResult<Spu> querySpu(Integer page, Integer rows, Boolean saleabe, String key) {
        PageHelper.startPage(page,rows);
        Example example = new Example(Spu.class);
        if(key!=null)
        {
            example.createCriteria().orLike("title","%"+key+"%");
        }
        if(saleabe!=null)
        {
            example.createCriteria().andEqualTo("saleable",saleabe);
        }
        List<Spu> spus = spuMapper.selectByExample(example);
        loadCategoryAndBrandName(spus);
        PageInfo<Spu> spuPageInfo = new PageInfo<>(spus);
        return new PageResult<Spu>(spuPageInfo.getTotal(),spus);
    }
    private void loadCategoryAndBrandName(List<Spu> spus)
    {
        for (Spu spu : spus) {
            List<String> names = categoryService.queryByIds(Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3()))
                    .stream().map(Category::getName).collect(Collectors.toList());
            spu.setCname(StringUtils.join(names,"/"));
            spu.setBname(brandService.queryBrandByBid(spu.getBrandId()).getName());
        }
    }

    public void saveGoods(Spu spu) {
        saveGood(spu);
    }

    private void saveGood(Spu spu) {
        spu.setId(null);
        spu.setCreateTime(new Date());
        spu.setLastUpdateTime(spu.getCreateTime());
        spu.setSaleable(true);
        spu.setValid(true);
        int insert = spuMapper.insert(spu);
        if(insert!=1)
        {
            System.out.println("新增失败");
        }
        SpuDetail spuDetail = spu.getSpuDetail();
        spuDetail.setSpuId(spu.getId());

        System.out.println("spudetail=="+spuDetail.toString());
        spuDetailMapper.insert(spuDetail);

        List<Sku> skus = spu.getSkus();
        Stock stock = new Stock();
        for (Sku sku : skus) {
            sku.setCreateTime(new Date());
            sku.setLastUpdateTime(sku.getCreateTime());
            sku.setSpuId(spu.getId());
            skuMapper.insert(sku);

            stock.setSkuId(sku.getId());
            stock.setStock(sku.getStock());
            stockMapper.insert(stock);
            amqpTemplate.convertAndSend("ly.item.exchange","item.insert",spu.getId());
        }
    }

    public SpuDetail querySpuDetailBySpuId(Long spuId) {
        SpuDetail spuDetail = spuDetailMapper.selectByPrimaryKey(spuId);
        return spuDetail;

    }

    public List<Sku> querySkuBySpuId(Long spuId) {
        Sku sku = new Sku();
        sku.setSpuId(spuId);
        List<Sku> list = skuMapper.select(sku);
        for (Sku skus : list) {
            Long id = skus.getId();
            Stock stock = stockMapper.selectByPrimaryKey(id);
            skus.setStock(stock.getStock());
        }
        return list;
    }

    @Transactional
    public void updateGoods(Spu spu) {
        Sku sku = new Sku();
        sku.setSpuId(spu.getId());
        List<Sku> skuList = skuMapper.select(sku);
        for (Sku sku1 : skuList) {
            stockMapper.deleteByPrimaryKey(sku1.getId());
        }
        skuMapper.delete(sku);
//        新增SKU和Stock
        saveGood(spu);
        spu.setValid(null);
        spu.setSaleable(null);
        spu.setCreateTime(null);
        spu.setLastUpdateTime(new Date());
        spuMapper.updateByPrimaryKeySelective(spu);

        spuDetailMapper.updateByPrimaryKeySelective(spu.getSpuDetail());
        amqpTemplate.convertAndSend("item.update",spu.getId());
    }

    public Spu querySpuById(Long id) {
        Spu spu = spuMapper.selectByPrimaryKey(id);
        spu.setSkus(querySkuBySpuId(spu.getId()));
        spu.setSpuDetail(querySpuDetailBySpuId(spu.getId()));
        return spu;
    }
    private void fillStock(List<Long> ids, List<Sku> skus) {
        //批量查询库存
        List<Stock> stocks = stockMapper.selectByIdList(ids);
        if (CollectionUtils.isEmpty(stocks)) {
            return;
        }
        //首先将库存转换为map，key为sku的ID
        Map<Long, Integer> map = stocks.stream().collect(Collectors.toMap(s -> s.getSkuId(), s -> s.getStock()));

        //遍历skus，并填充库存
        for (Sku sku : skus) {
            sku.setStock(map.get(sku.getId()));
        }
    }
    public List<Sku> querySkusToCart(List<Long> ids) {
        List<Sku> skus = skuMapper.selectByIdList(ids);
        fillStock(ids,skus);
        return skus;
    }
}
