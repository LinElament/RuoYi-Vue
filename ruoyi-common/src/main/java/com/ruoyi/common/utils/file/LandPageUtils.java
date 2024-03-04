package com.ruoyi.common.utils.file;

import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.ruoyi.common.constant.Constants.*;

import static com.ruoyi.common.constant.Constants.*;

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
    public String downloadWebsite(String url) {
        System.out.println("\n\n\n" + url + "\n\n\n");
        String savePath = createUniqueDirectory(PUBLIC_DATA, "");
        String command = "wget -P " + savePath + " -c -r -e robots=off -nv -p -k " + url; // 构建wget命令
        System.out.println(shellCommandExecutor(command));
        String commandImg = "wget https://urlscan.io/liveshot/?width=240&height=" + new int[]{480, 900, 600}[new Random().nextInt(3)] + "&url=" + url + " -O " + savePath + "/saved_image.jpg";
        System.out.println(shellCommandExecutor(commandImg));
        String downloadUrl = scanFolder(savePath, null).get(0);

        return downloadUrl;
    }

    /**
     * 增加用户配置页
     *
     * @return 返回用户此次增加的落地链接
     */
    public String configPageAdd(String username) {
        String savePath = createUniqueDirectory(USER_DATA, username);
        String command = "cp -r " + TEST_USER + " " + savePath; //复制用户配置模板文件
        System.out.println("\n" + command + "\n");
        System.out.println(shellCommandExecutor(command));
        return scanFolder(savePath, null).get(0);
    }


    /**
     * 生成一个唯一的目录名，使用URL安全的Base64编码和时间戳
     * 创建一个唯一的目录名并在文件系统中创建该目录。
     *
     * @param basePath 基础路径，新目录将在此路径下创建。
     * @return 创建的目录的绝对路径，如果创建失败则返回null。
     */
    public String createUniqueDirectory(String basePath, String username) {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[36];
        random.nextBytes(bytes);
        String base64Encoded = Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
        // 一个随机的新目录名
        String dirName = base64Encoded + System.currentTimeMillis();
        if (!username.isEmpty()) {
            dirName = dirName + username;
        }
        File directory = new File(basePath, dirName);
        if (!directory.exists() && directory.mkdirs()) {
            setPermissions(directory);
            return directory.getAbsolutePath();
        }
        return null;
    }

    /**
     * 执行命令方法
     *
     * @param command linux 命令
     * @return 退出码
     */
    public int shellCommandExecutor(String command) {
        Process process = null;
        BufferedReader reader = null;
        BufferedReader errorReader = null;
        int exitCode = -1; // 初始化退出码

        try {
            process = Runtime.getRuntime().exec(command);
            reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));

            // 读取命令的标准输出
            System.out.println("Standard output:");
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }

            // 读取命令的错误输出
            System.out.println("Standard error:");
            while ((line = errorReader.readLine()) != null) {
                System.out.println(line);
            }

            // 等待命令执行完成
            exitCode = process.waitFor();
            System.out.println("Exit code: " + exitCode);

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
                if (errorReader != null) {
                    errorReader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (process != null) {
                process.destroy();
            }
        }
        return exitCode; // 返回退出码
    }

    /**
     * 展示的落地页扫描
     *
     * @param folderPath 扫描路径
     * @return 路径下包含落地页的文件列表
     */
    public List<String> scanFolder(String folderPath, String username) {
        // 使用 try-with-resources 语句来确保在操作完成后自动关闭流
        try (Stream<Path> paths = Files.walk(Paths.get(folderPath))) {
            // 使用流处理文件路径
            return paths
                    // 过滤出文件类型为普通文件的路径
                    .filter(Files::isRegularFile)
                    // 过滤出文件名为 index.html 的文件路径
                    .filter(path -> path.getFileName().toString().equals("index.html"))
                    // 如果 username 不为空，则过滤出包含 username 的路径，否则返回所有路径
                    .filter(path -> username == null || username.isEmpty() || path.getFileName().toString().contains(username))
                    // 排除包含 'error' 和 'localhost' 的文件路径
                    .filter(path -> !path.toString().contains("error") && !path.toString().contains("localhost"))
                    // 将文件路径转换为 URL 形式
                    .map(LandPageUtils::convertToUrl)
                    // 收集流处理的结果到一个列表中
                    .collect(Collectors.toList());
        } catch (IOException e) {
            // 打印异常堆栈信息
            e.printStackTrace();
            // 如果发生异常，则返回一个空列表
            return Collections.emptyList();
        }
    }

    private static String convertToUrl(Path path) {
        return "https:/" + path.toString().replace("/www/admin", "") // 移除路径中的 '/www/admin'
                .replace("/index.html", "") // 移除路径中的 '/index.html'
                .replace("_80", "") // 移除路径中的 '_80'
                .replace("/wwwroot", "") // 移除路径中的 '/wwwroot'
                .replace(File.separator, "/"); // 将文件分隔符替换为 '/'
    }

    /**
     * 设置目录权限为777
     *
     * @param directory 文件
     */
    private void setPermissions(File directory) {
        try {
            // 使用Runtime执行chmod命令
            ProcessBuilder builder = new ProcessBuilder("chmod", "777", directory.getAbsolutePath());
            Process process = builder.start();
            process.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
