package com.leyou.service;

import com.leyou.mapper.SpecGroupMapper;
import com.leyou.mapper.SpecParamsMapper;
import com.leyou.pojo.SpecGroup;
import com.leyou.pojo.SpecParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("ALL")
@Service
public class SpecGroupService {
    @Autowired
    private SpecGroupMapper specGroupMapper;
    @Autowired
    private SpecParamsMapper specParamsMapper;
    public List<SpecGroup> queryGroupByCid(Long cid)
    {
        SpecGroup specGroup = new SpecGroup();
        specGroup.setCid(cid);
        List<SpecGroup> list = specGroupMapper.select(specGroup);
        return list;
    }

    public List<SpecParam> queryParamsByGid(Long gid, Long cid, Boolean searching) {
        SpecParam specParam = new SpecParam();
        specParam.setGroupId(gid);
        specParam.setCid(cid);
        specParam.setSearching(searching);
        List<SpecParam> list = specParamsMapper.select(specParam);
        return list;
    }

    public List<SpecGroup> queryGroupListByCid(Long cid) {
        List<SpecGroup> specGroups = queryGroupByCid(cid);
        List<SpecParam> specParams = queryParamsByGid(null, cid, null);
        Map<Long,List<SpecParam>> paramMap =new HashMap<>();
        for (SpecParam specParam : specParams) {
            if(!paramMap.containsKey(specParam.getGroupId()))
            {
                paramMap.put(specParam.getGroupId(),new ArrayList<>());
            }
            paramMap.get(specParam.getGroupId()).add(specParam);
        }
        for (SpecGroup specGroup : specGroups) {
            specGroup.setParams(paramMap.get(specGroup.getId()));
        }
        return specGroups;
    }
}
