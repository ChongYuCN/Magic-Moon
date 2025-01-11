package com.chongyu.magicmoon.config;

import com.chongyu.magicmoon.MagicMoon;
import com.google.gson.JsonObject;

public class CommonConfig {
    public boolean isCustomModel = false;
    public int startDay = 4;
    public int perDay = 2;

    public CommonConfig(){
    }

    //序列化配置文件
    public JsonObject serialize(){
        JsonObject root = new JsonObject();//父类
        JsonObject entry = new JsonObject();//子条目
        JsonObject entry_custom = new JsonObject();//子条目

        //参数一：条目名称     参数二：值
        entry.addProperty("desc:", "是否开启自定义模式");
        entry.addProperty("isCustomModel", isCustomModel);

        entry_custom.addProperty("desc1:", "开始天数>=4");
        entry_custom.addProperty("startDay", startDay);

        entry_custom.addProperty("desc2:", "间隔天数>=2");
        entry_custom.addProperty("perDay", perDay);

        entry.add("custom_model",entry_custom);

        root.add("magic_moon", entry);//创建父类条目名称，并把子条目添加进去

        return root;
    }

    //反序列化
    public void deserialize(JsonObject data) {
        if (data == null) {
            MagicMoon.LOGGER.error("Magic Moon Config file was empty!");
        } else {
            try {
                isCustomModel = data.get("magic_moon").getAsJsonObject().get("isCustomModel").getAsBoolean();
                startDay  = data.get("magic_moon").getAsJsonObject().getAsJsonObject("custom_model").get("startDay").getAsInt();
                perDay  = data.get("magic_moon").getAsJsonObject().getAsJsonObject("custom_model").get("perDay").getAsInt();

                if(startDay < 4 ){
                    startDay = 4;
                }
                if(perDay < 2 ){
                    perDay = 2;
                }

            } catch (Exception var3) {
                MagicMoon.LOGGER.error("Magic Moon Config Could not parse config file", var3);
            }

        }
    }
}
