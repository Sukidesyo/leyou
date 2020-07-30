package com.leyou.api;

import com.leyou.pojo.SpecGroup;
import com.leyou.pojo.SpecParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RequestMapping("/spec")
public interface SpecApi {
    @GetMapping("/groups/{cid}")
    List<SpecGroup> queryGroupByCid(@PathVariable("cid")Long cid);
    @GetMapping("/params")
    List<SpecParam> queryParamsByGId(
            @RequestParam(value = "gid",required = false)Long gid,
            @RequestParam(value = "cid",required = false)Long cid,
            @RequestParam(value = "searching",required = false)Boolean searching);
    @GetMapping("/group")
    List<SpecGroup> queryGroupsListByCid(@RequestParam("cid")Long cid);
}
