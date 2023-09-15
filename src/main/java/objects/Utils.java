package objects;

import lombok.SneakyThrows;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONArray;
import org.json.JSONObject;
import vk.VkApi;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Utils {
    public static Dispatcher dp = new Dispatcher();

    public static List att_parse(JSONArray attachments){
        List<String> atts = new ArrayList<>();
        if(attachments.length() > 0){
            for(Object i: attachments){
                JSONObject att = new JSONObject(i.toString());
                String att_t = att.getString("type");
                if (Arrays.asList("link", "article").equals(att_t)) continue;
                atts.add(att_t+att.getJSONObject(att_t).get("owner_id")+"_"+att.getJSONObject(att_t).get("id"));
                if(att.getJSONObject(att_t).has("access_key")){
                    atts.set(atts.size()-1, atts.get(atts.size() - 1)+ "_"+att.getJSONObject(att_t).get("access_key"));
                }
            }
        }
        return atts;
    }
    public static JSONObject request(String url){
        return request(url, "GET", "");
    }
    public static JSONObject request(String url, String method){
        return request(url, method, "");
    }
    @SneakyThrows
    public static JSONObject request(String url, String method, String... args) {
        String data = "?" + String.join("&", args);
        StringBuilder result = new StringBuilder();
        URL r = new URL(url+data);
        HttpURLConnection conn = (HttpURLConnection) r.openConnection();
        conn.setRequestMethod(method.toUpperCase());
        if( conn.getResponseCode() == 200){
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()))) {
                for (String line; (line = reader.readLine()) != null; ) {
                    result.append(line);
                }
            }
        } else {
            return new JSONObject("{\"code\": "+conn.getResponseCode()+"}");
        }
        JSONObject datas =  new JSONObject(result.toString());
        datas.put("code", conn.getResponseCode());
        return datas;
    }

    @SneakyThrows
    public static JSONObject requestF(String url, String path, boolean url_path){
        HttpClientBuilder builder = HttpClientBuilder.create();
        CloseableHttpClient httpClient = builder.build();

        HttpPost httpPost = new HttpPost(url);


        File file = new File(path);

        if (url_path){
            HttpClientBuilder builder1 = HttpClientBuilder.create();
            CloseableHttpClient httpClient1 = builder1.build();

            HttpGet request = new HttpGet(path);

            request.addHeader("content-type", "image/png");
            try (CloseableHttpResponse response1 = httpClient1.execute(request)) {
                HttpEntity entity1 = response1.getEntity();
                if (entity1 != null) {
                    String[] fileName = path.split("/");
                    file = new File(fileName[fileName.length-1].split("\\?")[0]);
                    try(FileOutputStream outputStream = new FileOutputStream(file)){
                        entity1.writeTo(outputStream);
                    }
                }
            }
        }

        MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();
        entityBuilder.addPart("file", new FileBody(file));
        final HttpEntity entity = entityBuilder.build();
        httpPost.setEntity(entity);
        HttpResponse response = httpClient.execute(httpPost);
        InputStreamReader reader = new InputStreamReader(response.getEntity().getContent());

        BufferedReader in = new BufferedReader(reader);


        String inputLine;
        StringBuilder response2 = new StringBuilder();

        while ((inputLine = in.readLine()) != null) {
            response2.append(inputLine);
        }
        reader.close();
        if(url_path)
            file.delete();
        return new JSONObject(response2.toString());
    }
    public static JSONObject getUserData(long id){
        String user = VkApi.call("users.get", "users_id="+id, "fields=online,domain,photo_400,photo_id");
        return new JSONArray(user).getJSONObject(0);
    }


    public static long getCurrentTimestamp(){
        String date = new Timestamp(System.currentTimeMillis()).toString().split("\\.")[0];

        return Long.parseLong(String.valueOf(Timestamp.valueOf(date).getTime()).substring(0,10));
    }
}

