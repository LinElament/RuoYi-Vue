package com.ruoyi.web.landpage;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.file.LandPageUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.nio.file.Paths;

import static com.ruoyi.common.constant.Constants.DOMAIN_DATA;
import static com.ruoyi.common.constant.Constants.PUBLIC_DATA;

public class Test {
    public static void main(String[] args) {

        LandPageUtils landPageUtils = new LandPageUtils();
//        try {
//            JSONArray jsonArray = new JSONArray();
//            AjaxResult ajax;
//            JSONObject jsonObject;
//            String file;
//            for (String href : landPageUtils.scanFolderHtml(DOMAIN_DATA, "liela", false)) {
//                file = href.replaceAll("index.html", "") + "config.json";
//                jsonObject = landPageUtils.readJsonFile(file);
//                ajax = new AjaxResult();
//                ajax.put("file", file);
//                ajax.put("href", landPageUtils.convertToUrl(Paths.get(href)));
//                ajax.put("lament", jsonObject.get("lament"));
//                jsonArray.add(ajax);
//            }
//            System.out.println(AjaxResult.success(jsonArray));
//        } catch (Exception e) {
//            System.out.println(AjaxResult.error(e.getMessage()));
//        }


        try {
            JSONArray jsonArray = new JSONArray();
            AjaxResult ajax;
            String url;
            for (String href : landPageUtils.scanFolderHtml(DOMAIN_DATA, null, false)) {
                ajax = new AjaxResult();
                url = landPageUtils.convertToUrl(Paths.get(href));
                ajax.put("file", href);
                ajax.put("href", url);
                jsonArray.add(ajax);
            }
            System.out.println(AjaxResult.success(jsonArray));
        } catch (Exception e) {
            System.out.println(AjaxResult.error(e.getMessage()));
        }
    }
}
