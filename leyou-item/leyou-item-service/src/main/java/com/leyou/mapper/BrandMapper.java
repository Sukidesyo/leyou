package com.leyou.mapper;

import com.leyou.pojo.Brand;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface BrandMapper extends Mapper<Brand>{
    @Insert("insert into tb_category_brand(category_id,brand_id) values(#{cid},#{bid})")
    int insertBrand_Category(@Param("cid")Long cid,@Param("bid")Long bid);
    @Select("select * from tb_brand inner join tb_category_brand on tb_brand.id=tb_category_brand.brand_id where category_id=#{cid}")
    List<Brand> selectBrandByCid(@Param("cid")Long cid);
}
