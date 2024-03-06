package com.ruoyi.web.landpage;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
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

import javax.servlet.http.HttpServletResponse;
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
        System.out.println("\n\n\n" + name + "\n\n\n");
        for (String href : landPageUtils.scanFolderHtml(USER_DATA, name, true)) {
            ajax = new AjaxResult();
            ajax.put("href", href);
            ajax.put("landLink", href.substring(0, 81) + "/config.json");
            ajax.put("link", href.substring(0, 81) + "/config.json");
            ajax.put("area", href.substring(0, 81) + "/config.json");
            ajax.put("xid", href.substring(0, 81) + "/config.json");
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


}
