package com.leyou.web;

import com.leyou.pojo.SpecGroup;
import com.leyou.pojo.SpecParam;
import com.leyou.service.SpecGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@SuppressWarnings("ALL")
@RestController
@RequestMapping("/spec")
public class SpecGroupController {
    @Autowired
    private SpecGroupService specGroupService;
    @GetMapping("/groups/{cid}")
    public ResponseEntity<List<SpecGroup>> queryGroupByCid(@PathVariable("cid")Long cid)
    {
        List<SpecGroup> list = specGroupService.queryGroupByCid(cid);
        System.out.println("list=="+list);
        return ResponseEntity.ok().body(list);
    }
    @GetMapping("/params")
    public ResponseEntity<List<SpecParam>> queryParamsByGId(
            @RequestParam(value = "gid",required = false)Long gid,
            @RequestParam(value = "cid",required = false)Long cid,
            @RequestParam(value = "searching",required = false)Boolean searching)
    {
        List<SpecParam> list = specGroupService.queryParamsByGid(gid,cid,searching);
        return ResponseEntity.ok().body(list);
    }
    @GetMapping("/group")
    ResponseEntity<List<SpecGroup>> queryGroupListByCid(@RequestParam("cid")Long cid)
    {
        return ResponseEntity.ok(specGroupService.queryGroupListByCid(cid));
    }


}
