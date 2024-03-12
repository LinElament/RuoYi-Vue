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
import java.lang.reflect.Array;
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
        try {
            JSONArray jsonArray = new JSONArray();
            AjaxResult ajax;
            for (String href : landPageUtils.scanFolderHtml(PUBLIC_DATA, null, true)) {
                ajax = new AjaxResult();
                ajax.put("src", href.substring(0, 83) + "/saved_image.jpg");
                ajax.put("href", href);
                jsonArray.add(ajax);
            }
            return AjaxResult.success(jsonArray);
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
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
        try {
            JSONArray jsonArray = new JSONArray();
            AjaxResult ajax;
            JSONObject jsonObject;
            String file;
            for (String href : landPageUtils.scanFolderHtml(USER_DATA, name, false)) {
                file = href.replaceAll("index.html", "") + "config.json";
                jsonObject = landPageUtils.readJsonFile(file);
                ajax = new AjaxResult();
                ajax.put("file", file);
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
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
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
        try {
            return AjaxResult.success(landPageUtils.downloadWebsite(url));
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
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
        try {
            landPageUtils.configPageAdd(name);
            return AjaxResult.success();
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
    }

    /**
     * 删除用户配置页
     *
     * @param file 配置页文件路径
     * @return result
     */
    @Log(title = "删除用户配置", businessType = BusinessType.OTHER)
    @PostMapping("/delete_config")
    public AjaxResult DConfig(@Validated @RequestBody List<String> file) {
        try {
            for (String filePath : file) {
                FileUtils.deleteFile(filePath);
                FileUtils.deleteFile(filePath.replaceAll("config.json", "index.html"));
            }
            return AjaxResult.success();
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
    }

    /**
     * 修改用户配置页
     *
     * @param UMap 表单
     * @return result
     */
    @Log(title = "修改用户配置", businessType = BusinessType.OTHER)
    @PostMapping("/update_config")
    public AjaxResult UConfig(@Validated @RequestBody Map<String, Object> UMap) {
        try {
            JSONObject jsonObject = landPageUtils.readJsonFile(UMap.get("file").toString());
            Map<String, Object> changes = new HashMap<>();
            changes.put("link", UMap.get("link"));
            changes.put("xsid", UMap.get("xid"));
            changes.put("whitelist", UMap.get("whitelist"));
            changes.put("targetCountry", UMap.get("targetCountry"));
            changes.put("targetLink", UMap.get("targetLink").toString());
            changes.put("lament", UMap.get("lament").toString());
            return AjaxResult.success(landPageUtils.modifyJson(jsonObject, changes, UMap.get("file").toString()));
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
    }
}
