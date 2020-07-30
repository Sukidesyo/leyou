package com.leyou.client;

import com.leyou.api.BrandApi;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient("item-Service")
public interface BrandClient extends BrandApi {
}
