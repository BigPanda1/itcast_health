package com.itheima.uitls;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.PutObjectRequest;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
public class AliyunUtils {

    // Endpoint以杭州为例，其它Region请按实际情况填写。
    private static String endpoint = "oss-cn-shenzhen.aliyuncs.com";
    // 阿里云主账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM账号进行API访问或日常运维，请登录 https://ram.console.aliyun.com 创建RAM账号。
    private static String accessKeyId = "LTAI4FsYTAtnvZvorXUZXYF7";
    private static String accessKeySecret = "QICL5PyT0KViegEwXCdjF8t3nUoh9y";
    private static String bucketName = "itheimahealth1";


    public static void uploadAliyun(String filePath,String fileName) {

        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        // 上传文件流。
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(filePath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        ossClient.putObject(bucketName, fileName, inputStream);

        // 关闭OSSClient。
        ossClient.shutdown();
    }

    public static void upload2Aliyun(byte[] bytes, String fileName){
        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId,accessKeySecret);

        // 上传Byte数组。
        byte[] content = bytes;
        ossClient.putObject(bucketName, fileName, new ByteArrayInputStream(content));

        // 关闭OSSClient。
        ossClient.shutdown();
    }

    public static void deleteFileFromAliyun(String fileName){
        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        // 删除文件。
        ossClient.deleteObject(bucketName, fileName);

        ossClient.shutdown();
    }

}
