package com.leyou.service;

import com.leyou.mapper.CategoryMapper;
import com.leyou.pojo.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("ALL")
@Service
public class CategoryService {
    @Autowired
    private CategoryMapper categoryMapper;

    public List<Category> queryCategoryListByPid(long pid) {
        Category category = new Category();
        category.setParentId(pid);
        List<Category> list = categoryMapper.select(category);
        if(CollectionUtils.isEmpty(list))
        {
            System.out.println("list==null");
            return null;
        }
        return list;
    }
    public List<Category> queryByIds(List<Long> Ids)
    {
        List<Category> list = categoryMapper.selectByIdList(Ids);
        if(CollectionUtils.isEmpty(list))
        {
            System.out.println("list==null");
            return null;
        }
        return list;
    }

    public List<String> queryNamesListByIds(List<Long> ids) {
        List<Category> categories = categoryMapper.selectByIdList(ids);
        List<String> names=new ArrayList<>();
        for (Category category : categories) {
         names.add(category.getName());
        }
        return names;
    }
}
