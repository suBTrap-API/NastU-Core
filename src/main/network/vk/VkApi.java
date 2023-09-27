package subtrap.nastu.network.vk;

import lombok.SneakyThrows;
import subtrap.nastu.utils.Logger;
import subtrap.nastu.utils.Utils;
import org.json.JSONObject;

import java.util.Arrays;

public class VkApi extends Methods{
    static String url = "https://api.vk.com/method/";
    static String query;

    static Logger logger = new Logger("VKApi");

    public VkApi(String token) {
        query = "?v=5.110&access_token="+token+"&lang=ru";
    }
    public static String call(String method) {
        return call(method, true, "");
    }
    public static String call(String method, String... args) {
        return call(method, true, args);
    }
    @SneakyThrows
    public static String call(String method, boolean response, String... args) {
        JSONObject result = Utils.request(url+method+query, "GET", args);
        if(result.getInt("code") == 200) {
            logger.info("Request " + method + " completed");
        }
        if(response){
            try {
                String res = result.toString();
                if (result.has("response")) {
                    res = result.get("response").toString();
                }
                if (result.has("error")) {
                    res = result.get("error").toString();
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