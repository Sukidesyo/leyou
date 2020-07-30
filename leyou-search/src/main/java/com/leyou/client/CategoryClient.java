package com.leyou.client;

import com.leyou.api.CategoryApi;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient("item-Service")
public interface CategoryClient extends CategoryApi {
}
