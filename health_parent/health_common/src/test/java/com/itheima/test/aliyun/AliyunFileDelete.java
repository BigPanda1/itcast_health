package com.itheima.test.aliyun;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;

public class AliyunFileDelete {

    public static void main(String[] args) {
        // Endpoint以杭州为例，其它Region请按实际情况填写。
        String endpoint = "oss-cn-shenzhen.aliyuncs.com";
        // 阿里云主账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM账号进行API访问或日常运维，请登录 https://ram.console.aliyun.com 创建RAM账号。
        String accessKeyId = "LTAI4FsYTAtnvZvorXUZXYF7";
        String accessKeySecret = "QICL5PyT0KViegEwXCdjF8t3nUoh9y";
        String bucketName = "itheimahealth1";
        String objectName = "abc.jpg";

        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        // 删除文件。
        ossClient.deleteObject(bucketName, objectName);

        ossClient.shutdown();
    }


}
