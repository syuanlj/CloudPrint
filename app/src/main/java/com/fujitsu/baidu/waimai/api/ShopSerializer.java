package com.fujitsu.baidu.waimai.api;

import java.lang.reflect.Type;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class ShopSerializer implements JsonSerializer<Shop>{
	@Override
    public JsonElement serialize(Shop shop, Type type, JsonSerializationContext context) {
        JsonObject object = new JsonObject();
        object.add("baidu_shop_id", context.serialize(shop.getbaiduShopId()));
        object.add("shop_id", context.serialize(shop.getShopId()));
        return object;
    }
}
