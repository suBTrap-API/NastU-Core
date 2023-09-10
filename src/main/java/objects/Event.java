package objects;

import lombok.SneakyThrows;
import org.json.JSONObject;
import vk.VkApi;

import java.util.List;
import java.util.logging.Logger;

public class Event {
    static Logger logger = Logger.getLogger("VkApi");
    protected JSONObject response;
    Database db = new Database();
    String method;
    public VkApi vk;

    JSONObject obj;
    static JSONObject reply_message;
    static JSONObject responses;
    static JSONObject msg;
    static List attachments;
    static String command;
    static String payload;
    public static List<String> args;
    static int peer_id;

    public Event(VkApi vk, JSONObject message){
        msg = message;
        this.vk = vk;
    }
    public void parse(){
        Message message = new Message(msg);

        reply_message = ((msg.has("reply_message")) ? (JSONObject) msg.get("reply_message") : null);
        attachments = message.attachments;
        command = message.command;
        payload = message.payload;
        args = message.args;
        peer_id = (int) msg.get("peer_id");
    }
}
class MySignalEvent extends Event {
    public MySignalEvent(Event event) {
        super(event.vk, msg);
        vk = event.vk;
        method = event.method;
        obj = event.obj;
        responses = event.response;
    }

    /**
     * @param mode
     *  3 - delete message,
     *  4 - delete message just for yourself,
     */
    public void msg_op(int mode){
        msg_op(mode, "", "");
    }

    /**
     * @param mode
     *  1 - send message,
     *  2 - edit message,
     *  3 - delete message,
     *  4 - delete message just for yourself,
     */
    public void msg_op(int mode, String text) {
        msg_op(mode, text, "");
    }

    /**
     * @param mode
     *  1 - send message,
     *  2 - edit message,
     *  3 - delete message,
     *  4 - delete message just for yourself,
     */
    @SneakyThrows
    public void msg_op(int mode, String text, String attachments) {
        int msg_id = (int) msg.get("id");
        VkApi.msg_op(mode, (int) msg.get("peer_id"), text, msg_id, attachments);
    }
}