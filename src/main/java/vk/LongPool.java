package vk;

import lombok.SneakyThrows;
import modules.Logger;
import objects.Utils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collections;
import java.util.List;

public class LongPool {
    static String key;
    static String server;
    static int ts;
    static VkApi vk;
    static int wait;

    static modules.Logger logger = new Logger("LongPoll");

    @SneakyThrows
    public LongPool(VkApi vkapi){
        vk = vkapi;
        String data = VkApi.call("messages.getLongPollServer");
        JSONObject json = new JSONObject(data);
        if(json.has("error")){
            if(json.getJSONObject("error").getInt("error_code") == 5) throw new Exception("token-fail");
        }
        server = json.get("server").toString();
        key = json.get("key").toString();
        ts = (int) json.get("ts");
        wait = 25;
    }

    public List<Object> check() throws Exception {
        JSONObject data = Utils.request("https://"+server+"?act=a_check&key="+key+"&ts="+ts+"&wait="+wait+"&version=3&mode=2");

        if(data.getInt("code") != 200){
            logger.warning("Network error");
            return List.of();
        }

        if(data.has("failed")){
            if((int) data.get("failed") == 1){
                logger.warning("Event history error");
                ts = (int) data.get("ts");
            } else if ((int) data.get("failed") == 2) {
                key = new JSONObject(VkApi.call("messages.getLongPollServer")).get("key").toString();
            } else {
                throw new Exception("User information is lost");
            }
            return List.of();
        }
        ts = (int) data.get("ts");
        return Collections.singletonList(data.getJSONArray("updates"));
    }
    public JSONArray listen() throws Exception {
        while (true){
            for(Object update: check()){
                try {
                    return ((JSONArray) update).getJSONArray(0);
                } catch (JSONException e) {
                    return null;
                }
            }
        }
    }
}
