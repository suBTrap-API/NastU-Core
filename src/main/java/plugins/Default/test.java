package plugins.Default;

import objects.*;
import org.json.JSONObject;

public class test {
    @Command(command = {"test", "example", "t"})
    public static String exampleFunc(LongPoolEvent event) {
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
