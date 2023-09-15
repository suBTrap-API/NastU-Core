package vk;

import lombok.SneakyThrows;
import modules.Logger;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;

public class VkApi extends Methods{
    static String url = "https://api.vk.com/method/";
    static String query;

    static modules.Logger logger = new Logger("VKApi");

    public VkApi(String token) {
        query = "?v=5.110&access_token="+token+"&lang=ru";
    }
    public String callns(String method) {
        return call(method);
    }
    public String callns(String method, String... args) {
        return call(method, true,args);
    }
    public String callns(String method, boolean response, String... args) {
        return call(method, response, args);
    }
    public static String call(String method) {
        return call(method, true, "");
    }
    public static String call(String method, String... args) {
        return call(method, true, args);
    }
    @SneakyThrows
    public static String call(String method, boolean response, String... args) {
        String data = "&";
        if (args != null) {
            data += String.join("&", args);
        }
        StringBuilder result = new StringBuilder();
        URL r = new URL(url + method + query + data);
        HttpURLConnection conn = (HttpURLConnection) r.openConnection();
        conn.setRequestMethod("GET");
        if (conn.getResponseCode() == 200) {
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()))) {
                for (String line; (line = reader.readLine()) != null; ) {
                    result.append(line);
                    logger.info("Request " + method + " completed");
                }
            }
        }
        if(response){
            try {
                JSONObject jsonObject = new JSONObject(result.toString());
                String res = jsonObject.toString();
                if (jsonObject.has("response")) {
                    res = jsonObject.get("response").toString();
                }
                if (jsonObject.has("error")) {
                    res = jsonObject.get("error").toString();
                }
                return res;
            } catch (Exception e){
                return result.toString();
            }
        }
        else return result.toString();
    }

    /**
     * @param mode
     *  1 - send message,
     *  2 - edit message,
     *  3 - delete message,
     *  4 - delete message just for yourself,
     */
    public static String msg_op(int mode, int peer_id, String message) {
        return msg_op(mode, peer_id, message, 0, "");
    }

    /**
     * @param mode
     *  1 - send message,
     *  2 - edit message,
     *  3 - delete message,
     *  4 - delete message just for yourself,
     */
    public static String msg_op(int mode, int peer_id, String message, int msg_id) {
        return msg_op(mode, peer_id, message, msg_id, "");
    }

    /**
     * @param mode
     *  1 - send message,
     *  2 - edit message,
     *  3 - delete message,
     *  4 - delete message just for yourself,
     */
    public static String msg_op(int mode, int peer_id, String message, int msg_id, String attachment) {
        message = message.replace("&amp;", "&").replace("&quot;", "\"").replace("&lt;", "<").replace("&gt;", ">").replace(" ", "+").replace("\n", "<br>");
        int dfa = 1;
        if(mode == 4){
            mode = 3;
            dfa = 0;
        }
        String modes = Arrays.asList("messages.send", "messages.edit", "messages.delete").get(mode-1);
        return call(modes, "peer_id="+peer_id, "message="+message,"message_id="+msg_id, "delete_for_all="+dfa, "attachment="+attachment, "random_id=0");
    }
    public static void exec(String code) {
        exec(code, null);
    }

    public static void exec(String code, String token) {
        if(token != null){
            new VkApi(token);
            call("execute", "code=" + code);
            return;
        }
        call("execute", "code=" + code);
    }
}