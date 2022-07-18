package com.example.demo.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.example.demo.common.Result;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.List;

@RestController
@RequestMapping("/files")
public class FileController {
    @Value("${server.port}")
    private String port;
    private  static  final String ip="http://localhost";
    /**
     *@Description:上传接口
     ** @param file
     *@Author:HLL
     *@Date:2022/7/14 20:40
     */
    @PostMapping("/upload")
    public Result<?> upload(MultipartFile file) throws IOException {
        String originalFilename=file.getOriginalFilename();//获取源文件名称
        //使用huTool工具
        //自主定义唯一标识，避免文件名重复导致文件覆盖
        String flag = IdUtil.fastSimpleUUID();
        String rootFilePath=System.getProperty("user.dir")+ "/springboot/src/main/resources/files/"+flag+"_"+originalFilename;
        FileUtil.writeBytes(file.getBytes(),rootFilePath);//将文件写入该路径下

        return Result.success(ip + ":" + port.trim() + "/files/" + flag);  // 返回结果 url

    }

    @PostMapping("/editor/upload")
    public JSON editorUpload(MultipartFile file) throws IOException {
        String originalFilename=file.getOriginalFilename();//获取源文件名称
        //使用huTool工具
        //自主定义唯一标识，避免文件名重复导致文件覆盖
        String flag = IdUtil.fastSimpleUUID();
        String rootFilePath=System.getProperty("user.dir")+ "/springboot/src/main/resources/files/"+flag+"_"+originalFilename;
        FileUtil.writeBytes(file.getBytes(),rootFilePath);//将文件写入该路径下
        String url=ip + ":" + port.trim() + "/files/" + flag;
        JSONObject json=new JSONObject();
        JSONArray arr=new JSONArray();
        JSONObject data=new JSONObject();
        data.set("url",url);
        arr.add(data);

        json.set("errno",0);
        json.set("data",arr);
        return json;  // 返回结果 url

    }
    /**
     *@Description:下载接口
     ** @param flag
     * @param response
     *@Author:HLL
     *@Date:2022/7/14 20:40
     */
    @GetMapping("/{flag}")
    public void getFiles(@PathVariable String flag,HttpServletResponse response) {
        OutputStream os;//输出流对象
        String basePath = System.getProperty("user.dir") + "/springboot/src/main/resources/files/";//文件要上传的路径
        List<String> fileNames = FileUtil.listFileNames(basePath);//获取所有文件的名称
        String avatar = fileNames.stream().filter(name -> name.contains(flag)).findAny().orElse("");//找出与flag一致的文件
        try {
            if (StrUtil.isNotEmpty(avatar)) {
                response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(avatar, "UTF-8"));
                response.setContentType("application/octet-stream");
                byte[] bytes = FileUtil.readBytes(basePath + avatar);
                os = response.getOutputStream();//通过输出流返回文件
                os.write(bytes);
                os.flush();
                os.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
