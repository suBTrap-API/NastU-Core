package subtrap.nastu.network;

import lombok.SneakyThrows;
import subtrap.nastu.network.vk.VkApi;
import subtrap.nastu.objects.Database;
import org.json.JSONObject;
import subtrap.nastu.utils.Logger;

import java.util.List;

public class Event {
    static Logger logger = new Logger("Event");
    protected JSONObject response;
    Database db = new Database();
    String method;
    public VkApi vk;

    JSONObject obj;
    public JSONObject reply_message;
    public JSONObject responses;
    public static JSONObject msg;
    public List attachments;
    public String command;
    public String payload;
    public List<String> args;
    public int peer_id;

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
     * @param mode <br>
     *  3 - delete message,<br>
     *  4 - delete message just for yourself,<br>
     */
    public void msg_op(int mode){
        msg_op(mode, "", "");
    }

    /**
     * @param mode <br>
     *  1 - send message,<br>
     *  2 - edit message
     */
    public void msg_op(int mode, String text) {
        msg_op(mode, text, "");
    }

    /**
     * @param mode <br>
     *  1 - send message,<br>
     *  2 - edit message
     */
    @SneakyThrows
    public void msg_op(int mode, String text, String attachment) {
        int msg_id = (int) msg.get("id");
        vk.msg_op(mode, (int) msg.get("peer_id"), text, msg_id, attachment);
    }
}