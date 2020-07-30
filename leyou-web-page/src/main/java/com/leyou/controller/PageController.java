package com.leyou.controller;

import com.leyou.Service.PageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;

@Controller

public class PageController {
    @Autowired
    private PageService pageService;
    @GetMapping("item/{id}.html")
    public String toItemPage( @PathVariable("id")Long id,Model model){
        Map<String, Object> stringObjectMap = pageService.loadModel(id);
        System.out.println("string=="+stringObjectMap);
        model.addAllAttributes(stringObjectMap);
        return "item";
    }
}
