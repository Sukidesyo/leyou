package com.leyou.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.leyou.client.BrandClient;
import com.leyou.client.CategoryClient;
import com.leyou.client.GoodsClient;
import com.leyou.client.SpecClient;
import com.leyou.common.vo.PageResult;
import com.leyou.pojo.*;
import com.leyou.repository.GoodsRepository;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.LongTerms;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.query.FetchSourceFilter;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SearchService {
    @Autowired
    private BrandClient brandClient;

    @Autowired
    private CategoryClient categoryClient;

    @Autowired
    private GoodsClient goodsClient;

    @Autowired
    private SpecClient specClient;
    @Autowired
    private GoodsRepository goodsRepository;
    @Autowired
    private ElasticsearchTemplate template;

    public Goods buildGoods(Spu spu) throws IOException {
        ObjectMapper MAPPER = new ObjectMapper();
        Goods goods = new Goods();
        //根据SPU的cid查询分类
        List<Category> categories = categoryClient.queryNamesById(Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3()));
        //将分类List中的分类名取出来
        List<String> names = categories.stream().map(Category::getName).collect(Collectors.toList());
        //根据SpuId查询Spu下的所有Sku
        List<Sku> skus = goodsClient.querySkuListBySpuId(spu.getId());

        //根据Spu的cid3查询所有的参数
        List<SpecParam> specParams = specClient.queryParamsByGId(null, spu.getCid3(), true);
        //根据Spuid查SpuDeatil
        SpuDetail spuDetail = goodsClient.querySpuDetailBySpuId(spu.getId());
        //查询品牌
        Brand brand = brandClient.queryBrandById(spu.getBrandId());
        List<Map<String, Object>> skuMapList = new ArrayList<>();
        List<Long> prices = new ArrayList<Long>();
        Map<String, Object> specs = new HashMap<String, Object>();
        for (Sku sku : skus) {
            prices.add(sku.getPrice());
            HashMap<String, Object> skuMap = new HashMap<>();
            skuMap.put("id", sku.getId());
            skuMap.put("title", sku.getTitle());
            skuMap.put("price", sku.getPrice());
            skuMap.put("image", StringUtils.isNotBlank(sku.getImages()) ? StringUtils.split(sku.getImages(), ",")[0] : "");
            skuMapList.add(skuMap);
        }
        Map<Long, Object> genericSpecMap = MAPPER.readValue(spuDetail.getGenericSpec(), new TypeReference<Map<Long, Object>>() {
        });
        // 获取特殊的规格参数
        Map<Long, List<Object>> specialSpecMap = MAPPER.readValue(spuDetail.getSpecialSpec(), new TypeReference<Map<Long, List<Object>>>() {
        });
        for (SpecParam specParam : specParams) {
            String key = specParam.getName();
            Object value = null;
            if (specParam.getGeneric()) {
                value = genericSpecMap.get(specParam.getId());
                if (specParam.getNumeric()) {
                    value = chooseSegment(value.toString(), specParam);
                }
            } else {
                value = specialSpecMap.get(specParam.getId());
            }
            specs.put(key, value);
        }

        goods.setId(spu.getId());
        goods.setBrandId(spu.getBrandId());
        goods.setCid1(spu.getCid1());
        goods.setCid2(spu.getCid2());
        goods.setCid3(spu.getCid3());
        goods.setCreateTime(spu.getCreateTime());
        goods.setSubTitle(spu.getSubTitle());
        goods.setAll(spu.getSubTitle() + StringUtils.join(names, " ") + brand.getName());
        goods.setPrice(prices);
        goods.setSkus(MAPPER.writeValueAsString(skuMapList));
        goods.setSpecs(specs);
        return goods;
    }

    private String chooseSegment(String value, SpecParam p) {
        double val = NumberUtils.toDouble(value);
        String result = "其它";
        // 保存数值段
        for (String segment : p.getSegments().split(",")) {
            String[] segs = segment.split("-");
            // 获取数值范围
            double begin = NumberUtils.toDouble(segs[0]);
            double end = Double.MAX_VALUE;
            if (segs.length == 2) {
                end = NumberUtils.toDouble(segs[1]);
            }
            // 判断是否在范围内
            if (val >= begin && val < end) {
                if (segs.length == 1) {
                    result = segs[0] + p.getUnit() + "以上";
                } else if (begin == 0) {
                    result = segs[1] + p.getUnit() + "以下";
                } else {
                    result = segment + p.getUnit();
                }
                break;
            }
        }
        return result;
    }

    public PageResult<Goods> searchGoods(SearchRequest searchRequest) {
        int page = searchRequest.getPage();
        int size = searchRequest.getSize();

        String key = searchRequest.getKey();

        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();

        if (key == null) {
            return null;
        }
        queryBuilder.withQuery(QueryBuilders.matchQuery("all", key).operator(Operator.AND));
        queryBuilder.withSourceFilter(new FetchSourceFilter(new String[]{"id", "skus", "subTitle"}, null));
        queryBuilder.withPageable(PageRequest.of(page - 1, size));

        String categoryAggName = "categoryAgg";
        String brandAggName = "brandAgg";
        queryBuilder.addAggregation(AggregationBuilders.terms(categoryAggName).field("cid3"));
        queryBuilder.addAggregation(AggregationBuilders.terms(brandAggName).field("brandId"));
        AggregatedPage<Goods> result = template.queryForPage(queryBuilder.build(), Goods.class);
//            Page<Goods> result = goodsRepository.search(queryBuilder.build());
        List<Goods> content = result.getContent();
        int totalPages = result.getTotalPages();
        long totalElements = result.getTotalElements();
        Aggregations aggs = result.getAggregations();
        List<Map<String, Object>> categories = getCategoryAggResult(aggs.get(categoryAggName));
        List<Brand> brands = getBrandAggResult(aggs.get(brandAggName));
        return new SearchResult(totalElements, totalPages, content, categories, brands);
    }

    private List<Brand> getBrandAggResult(Aggregation aggregation) {
        // 处理聚合结果集
        LongTerms terms = (LongTerms) aggregation;
        // 获取所有的品牌id桶
        List<LongTerms.Bucket> buckets = terms.getBuckets();
        // 定义一个品牌集合，搜集所有的品牌对象
        List<Brand> brands = new ArrayList<>();
        // 解析所有的id桶，查询品牌
        buckets.forEach(bucket -> {
            Brand brand = this.brandClient.queryBrandById(bucket.getKeyAsNumber().longValue());
            brands.add(brand);
        });
        return brands;
    }

    /**
     * 解析分类
     *
     * @param aggregation
     * @return
     */
    private List<Map<String, Object>> getCategoryAggResult(Aggregation aggregation) {
        // 处理聚合结果集
        LongTerms terms = (LongTerms) aggregation;
        // 获取所有的分类id桶
        List<LongTerms.Bucket> buckets = terms.getBuckets();
        // 定义一个品牌集合，搜集所有的品牌对象
        List<Map<String, Object>> categories = new ArrayList<>();
        List<Long> cids = new ArrayList<>();
        // 解析所有的id桶，查询品牌
        buckets.forEach(bucket -> {
            cids.add(bucket.getKeyAsNumber().longValue());
        });
        List<Category> names = this.categoryClient.queryNamesById(cids);
        for (int i = 0; i < cids.size(); i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", cids.get(i));
            map.put("name", names.get(i));
            categories.add(map);
        }
        return categories;
    }

    public void createOrUpdateIndex(Long spuId) {
        Spu spu = goodsClient.querySpuBySpuId(spuId);
        Goods goods = null;
        try {
            goods = buildGoods(spu);
        } catch (IOException e) {
            e.printStackTrace();
        }
        goodsRepository.save(goods);
    }
}
