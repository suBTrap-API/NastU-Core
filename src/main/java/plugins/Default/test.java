package plugins.Default;

import objects.Database;
import objects.LongPoolEvent;
import objects.SendFile;
import objects.Utils;
import org.json.JSONObject;

import static objects.Utils.dp;

public class test {
    static LongPoolEvent event;
    public test(LongPoolEvent e) {
        event = e;
        dp.longpoll_event_register("example", "example", "test", "t");
    }
    public static String example() {
        JSONObject user = Utils.getUserData(Database.instance.owner_id);
        String last = user.getString("last_name");
        String first = user.getString("first_name");
        String domain = user.getString("domain");
        SendFile.photo("https://img.freepik.com/premium-photo/white-cat-dark-room-dark-concept_41085-544.jpg", event,
                "Hello, "+last+" "+first+"(@"+domain+").\n It works :D", 2);
        event.msg_op(1, ":D");

        return "ok";
    }
}
