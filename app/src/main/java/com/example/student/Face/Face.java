package com.example.student.Face;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;

import com.baidu.aip.util.Base64Util;
import com.example.student.DoResult.AddResult;
import com.example.student.DoResult.SearchResult;
import com.example.student.Info.GsonUtils;
import com.example.student.Info.HttpUtil;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.student.utils.ImageUtil.getBytesByBitmap;

public class Face {

    private static String FACE_GROUP = "student";

    public static String getAuth() {
        // 官网获取的 API Key 更新为你注册的
        String clientId = "GACoGSpqozOcph38p25QIIDO";
        // 官网获取的 Secret Key 更新为你注册的
        String clientSecret = "NtsoqoU9irWTjBBc3tdGeE4Zinc8ZuGl";

        return getAuth(clientId, clientSecret);
    }

    public static String getAuth(String ak, String sk) {
        // 获取token地址
        String authHost = "https://aip.baidubce.com/oauth/2.0/token?";
        String getAccessTokenUrl = authHost
                // 1. grant_type为固定参数
                + "grant_type=client_credentials"
                // 2. 官网获取的 API Key
                + "&client_id=" + ak
                // 3. 官网获取的 Secret Key
                + "&client_secret=" + sk;
        try {
            URL realUrl = new URL(getAccessTokenUrl);
            // 打开和URL之间的连接
            HttpURLConnection connection = (HttpURLConnection) realUrl.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            // 获取所有响应头字段
            Map<String, List<String>> map = connection.getHeaderFields();
            // 遍历所有的响应头字段
            for (String key : map.keySet()) {
                System.err.println(key + "--->" + map.get(key));
            }
            // 定义 BufferedReader输入流来读取URL的响应
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String result = "";
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
            /**
             * 返回结果示例
             */
            System.err.println("result:" + result);
            JSONObject jsonObject = new JSONObject(result);
            String access_token = jsonObject.getString("access_token");
            return access_token;
        } catch (Exception e) {
            System.err.printf("获取token失败！");
            e.printStackTrace(System.err);
        }
        return null;
    }

    public static void addFace(String stunum, Bitmap bitmap, Handler handler) {
        // 错误代码
        Integer code = null;
        // 请求url
        String url = "https://aip.baidubce.com/rest/2.0/face/v3/faceset/user/add";
        Message message = new Message();
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("image", Base64Util.encode(getBytesByBitmap(bitmap)));
            // 组
            map.put("group_id", FACE_GROUP);
            // 用户标识
            map.put("user_id", stunum);
            map.put("liveness_control", "NORMAL");
            map.put("image_type", "BASE64");
            map.put("quality_control", "LOW");
            String param = GsonUtils.toJson(map);

            // 注意这里仅为了简化编码每一次请求都去获取access_token，线上环境access_token有过期时间， 客户端可自行缓存，过期后重新获取。
            // 获得权限
            String accessToken = getAuth();

            String result = HttpUtil.post(url, accessToken, "application/json", param);
            // 返回结果
            Gson gson = new Gson();
            AddResult addResult = gson.fromJson(result,AddResult.class);
            System.out.println(addResult);
            code = addResult.getError_code();
            message.what=0;
            // 上传成功
            if(code==0) {
                message.what=1;

            }else {
                message.what=code;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        handler.sendMessage(message);
    }

    /**
     *
     *   在人脸库中查询
     * @param bitmap  该人脸的位图
     * @param handler  消息回传
     */
    public static void faceSearch(String stunum,Bitmap bitmap ,Handler handler) {
        // 请求url
        String url = "https://aip.baidubce.com/rest/2.0/face/v3/search";
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("image", Base64Util.encode(getBytesByBitmap(bitmap)));
            map.put("liveness_control", "NORMAL");
            map.put("group_id_list", FACE_GROUP);
            map.put("image_type", "BASE64");
            map.put("quality_control", "LOW");

            String param = GsonUtils.toJson(map);

            // 注意这里仅为了简化编码每一次请求都去获取access_token，线上环境access_token有过期时间， 客户端可自行缓存，过期后重新获取。
            String accessToken = getAuth();

            String result = HttpUtil.post(url, accessToken, "application/json", param);
            Gson gson = new Gson();
            SearchResult searchResult = gson.fromJson(result,SearchResult.class);
            Integer code =searchResult.getError_code();
            Message message = new Message();
            message.what=-1;
            // 成功
            if(code==0){
                SearchResult.ResultBean.UserListBean userListBean =searchResult.getResult().getUser_list().get(0);
                // 账号相等
                if(userListBean.getUser_id().equals(stunum)){
                    // 相识度大于90 才认为 是本人
                    if(userListBean.getScore()>90)
                        message.what=0;

                }
            }else{
                // 设置 百度给的错误代码
                message.what=code;
            }
            handler.sendMessage(message);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /*
       Android 规范 ，不能在主线程中上传文件等耗时操作，会严重影响体验
       必须使用线程，使用方式多样
    */
    public static void runUpload( String stunum,  Bitmap bitmap, Handler handler){
        // 启动线程
        new Thread(new Runnable() {
            @Override
            public void run() {
                addFace(stunum,bitmap,handler);
            }
        }).start();
    }
    public static void runSerach(String stunum,Bitmap bitmap,Handler handler){
        new Thread(new Runnable() {
            @Override
            public void run() {
                faceSearch(stunum,bitmap,handler);
            }
        }).start();
    }
}
