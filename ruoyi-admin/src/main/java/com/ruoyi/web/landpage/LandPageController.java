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
     * 查询 落地页或者用户配置
     *
     * @return src 图片 href 地址
     */
    @Log(title = "查询&落地页或者用户配置", businessType = BusinessType.OTHER)
    @GetMapping(value = {"/list", "/{username}"})
    public AjaxResult pageList(@PathVariable(value = "username", required = false) String username) {
        JSONArray jsonArray = new JSONArray();
        AjaxResult ajax;
        for (String href : landPageUtils.scanFolder(PUBLIC_DATA, username)) {
            ajax = new AjaxResult();
            ajax.put("src", href + "/saved_image.jpg");
            ajax.put("href", href);
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
