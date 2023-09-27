package subtrap.nastu.network;

import org.json.JSONObject;
import subtrap.nastu.network.vk.VkApi;

public class LongPoolEvent extends MySignalEvent {
    static String method = "LongPoll";
    static JSONObject data;
    String message;

    public String debug() {
        return """
                New event from LongPoll module
                Command: %s
                Arguments: %s
                Data: %s
                Message: %s""".formatted(command, args, data, msg);
    }

    public LongPoolEvent(Event event, JSONObject args) {
        super(event);
        data = args;
        message = data.get("message").toString();
        parse();
        command = ((data.has("command")) ? data.get("command").toString(): command);
        vk = new VkApi(db.access_token);

        logger.info(debug());
    }
}
