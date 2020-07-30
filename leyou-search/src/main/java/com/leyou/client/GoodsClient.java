package com.leyou.client;

import com.leyou.api.GoodsApi;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient("item-Service")
public interface GoodsClient extends GoodsApi {
}
