package com.ruoyi.web.landpage;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.file.LandPageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.ruoyi.common.core.domain.AjaxResult;
import springfox.documentation.spring.web.json.Json;
import com.ruoyi.common.utils.file.FileUtils;

import javax.servlet.http.HttpServletResponse;
import java.nio.file.Paths;
import java.util.*;

import static com.ruoyi.common.constant.Constants.*;


@RestController
@RequestMapping("/land_page")
public class LandPageController {

    @Autowired
    private LandPageUtils landPageUtils;

    /**
     * 查询 落地页
     *
     * @return src 图片 href 地址
     */
    @Log(title = "落地页页面", businessType = BusinessType.OTHER)
    @GetMapping("/list")
    public AjaxResult pageList() {
        JSONArray jsonArray = new JSONArray();
        AjaxResult ajax;
        for (String href : landPageUtils.scanFolderHtml(PUBLIC_DATA, null, true)) {
            ajax = new AjaxResult();
            ajax.put("src", href.substring(0, 81) + "/saved_image.jpg");
            ajax.put("href", href);
            jsonArray.add(ajax);
        }
        return AjaxResult.success(jsonArray);
    }


    /**
     * 用户配置页
     *
     * @param name 需要下载的落地页链接
     * @return 返回本系统新增落地页链接
     */
    @Log(title = "查看用户配置页面", businessType = BusinessType.OTHER)
    @PostMapping("/config")
    public AjaxResult configList(@Validated @RequestBody String name) {
        JSONArray jsonArray = new JSONArray();
        AjaxResult ajax;
        JSONObject jsonObject;
        for (String href : landPageUtils.scanFolderHtml(USER_DATA, name, false)) {
            jsonObject = landPageUtils.readJsonFile(href.replaceAll("index.html", "") + "config.json");
            ajax = new AjaxResult();
            ajax.put("file", href);
            ajax.put("href", landPageUtils.convertToUrl(Paths.get(href)));
            ajax.put("link", jsonObject.get("link"));
            ajax.put("xid", jsonObject.get("xsid"));
            ajax.put("whitelist", jsonObject.get("whitelist"));
            ajax.put("targetCountry", jsonObject.get("targetCountry"));
            ajax.put("targetLink", jsonObject.get("targetLink"));
            ajax.put("lament", jsonObject.get("lament"));
//            修改ip api服务，暂时不提供
//            ajax.put(jsonObject.get("ipInfoUrl"));
            jsonArray.add(ajax);
        }
        return AjaxResult.success(jsonArray);
    }

    /**
     * 增加落地页
     *
     * @param url 需要下载的落地页链接
     * @return 返回本系统新增落地页链接
     */
    @Log(title = "增加落地页", businessType = BusinessType.OTHER)
    @PostMapping("/add_page")
    public AjaxResult addPage(@Validated @RequestBody String url) {
        return AjaxResult.success(landPageUtils.downloadWebsite(url));
    }

    /**
     * 添加用户配置页
     *
     * @param name 用户名
     * @return 返回用户此次添加用户配置页链接
     */
    @Log(title = "增加用户配置", businessType = BusinessType.OTHER)
    @PostMapping("/add_config")
    public AjaxResult addConfig(@Validated @RequestBody String name) {
        return AjaxResult.success(landPageUtils.configPageAdd(name));
    }

    /**
     * 删除用户配置页
     *
     * @param file 配置页文件路径
     * @return result
     */
    @Log(title = "删除用户配置", businessType = BusinessType.OTHER)
    @PostMapping("/delete_config")
    public AjaxResult DConfig(@Validated @RequestBody String file) {
        try {
            FileUtils.deleteFile(file);
            return AjaxResult.success();
        } catch (Exception e) {
            return AjaxResult.error();
        }
    }

    public AjaxResult UConfig(@Validated @RequestBody Map<String, Object> UMap) {
        JSONObject jsonObject = landPageUtils.readJsonFile(UMap.get("file").toString());
        Map<String, Object> changes = new HashMap<>();
//        "https://bilibili.com", "https://weibo.com"
        changes.put("link", Arrays.asList(UMap.get("link")));
        changes.put("xsid", Arrays.asList("123456789012345", "123456789012345"));
        changes.put("whitelist", Arrays.asList("192.168.0.1", "192.168.0.2", "192.168.0.3"));
        changes.put("targetCountry", Arrays.asList("CN"));
        changes.put("targetLink", "https://example.com/ABC123");
        return AjaxResult.success(landPageUtils.modifyJson(jsonObject, changes));
    }
}
