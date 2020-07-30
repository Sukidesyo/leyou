package com.leyou.pojo;

import com.leyou.common.vo.PageResult;
import lombok.Data;

import java.util.List;
import java.util.Map;
@Data
public class SearchResult extends PageResult<Goods> {
    
    private List<Map<String, Object>> categories;
    private List<Brand> brands;

    public SearchResult(Long total, Integer totalPage, List<Goods> items, List<Map<String, Object>> categories, List<Brand> brands) {
        super(total, totalPage, items);
        this.categories = categories;
        this.brands = brands;
    }
}