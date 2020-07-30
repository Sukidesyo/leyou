//package com.leyou.repository;
//
//import com.leyou.client.GoodsClient;
//import com.leyou.common.vo.PageResult;
//import com.leyou.pojo.Goods;
//import com.leyou.pojo.Spu;
//import com.leyou.repository.GoodsRepository;
//import com.leyou.service.SearchService;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
//import org.springframework.test.context.junit4.SpringRunner;
//import org.springframework.util.CollectionUtils;
//
//import java.io.IOException;
//import java.util.List;
//import java.util.stream.Collectors;
//
//@RunWith(SpringRunner.class)
//@SpringBoot
//public class GoodsRepositoryTest {
//    @Autowired
//    private GoodsRepository goodsRepository;
//    @Autowired
//    private ElasticsearchTemplate template;
//    @Autowired
//    private GoodsClient goodsClient;
//    @Autowired
//    private SearchService searchService;
//    @Test
//    public void test()
//    {
//        template.createIndex(Goods.class);
//        template.putMapping(Goods.class);
//    }
//    @Test
//    public void loadData() throws IOException {
//        Integer page = 1;
//        Integer rows = 100;
//
//        do {
//            // 分批查询spuBo
//            PageResult<Spu> pageResult = goodsClient.querySpuForPage(page,rows,true,null);
//            System.out.println("page=="+pageResult);
//            // 遍历spubo集合转化为List<Goods>
//            List<Spu> spuList = pageResult.getItems();
//            if (CollectionUtils.isEmpty(spuList))
//            {
//                break;
//            }
//            System.out.println("list=="+spuList);
//            List<Goods> goodsList = pageResult.getItems().stream().map(spu -> {
//                try {
//                    return this.searchService.buildGoods((Spu) spu);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                return null;
//            }).collect(Collectors.toList());
//            goodsRepository.saveAll(goodsList);
//
//            // 获取当前页的数据条数，如果是最后一页，没有100条
//            rows = pageResult.getItems().size();
//            // 每次循环页码加1
//            page++;
//        } while (rows == 100);
//    }
//}