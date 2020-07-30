package com.leyou.client;

import com.leyou.api.SpecApi;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient("item-Service")
public interface SpecClient extends SpecApi {
}
