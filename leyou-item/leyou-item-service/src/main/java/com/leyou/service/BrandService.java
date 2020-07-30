package com.leyou.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.leyou.common.vo.PageResult;
import com.leyou.mapper.BrandMapper;
import com.leyou.pojo.Brand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.List;
@SuppressWarnings("ALL")
@Service
public class BrandService {
    @Autowired
    private BrandMapper brandMapper;

    public PageResult<Brand> queryBrandByPage(Integer page, Integer rows, String sortBy, Boolean desc, String key) {
        PageHelper.startPage(page,rows);
        Example example = new Example(Brand.class);
        if(key!=null) {
            example.createCriteria().orLike("name","%"+key+"%").orEqualTo("letter",key.toUpperCase());
        }

        List<Brand> list = brandMapper.selectByExample(example);

        PageInfo<Brand> brandPageInfo = new PageInfo<Brand>(list);

        return new PageResult<>(brandPageInfo.getTotal(),list);
    }
    @Transactional
    public void saveBrand(Brand brand, List<Long> cids) {
        int flag = brandMapper.insert(brand);
        if(flag!=1)
        {
            System.out.println("品牌添加失败");
        }

        for(Long cid:cids)
        {
            brandMapper.insertBrand_Category(cid,brand.getId());
        }
    }

    public Brand queryBrandByBid(Long brandId) {
        Brand brand = brandMapper.selectByPrimaryKey(brandId);
        return brand;
    }

    public List<Brand> queryBranByCid(Long cid) {
        List<Brand> list = brandMapper.selectBrandByCid(cid);
        return list;
    }
}
