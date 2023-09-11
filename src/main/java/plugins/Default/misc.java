package plugins.Default;

import objects.Command;
import objects.LongPoolEvent;
import objects.Utils;
import org.json.JSONObject;

public class misc {
    static JSONObject pings = new JSONObject("""
            {
                "пинг": "ПОНГ",
                "кинг": "КОНГ",
                "пиу": "ПАУ",
                "тик": "ТОК",
                "ping": "PONG",
                "king": "The Lion King*",
                "tick": "Tick-Tock-Tack"
            }""");

    @Command(command = {"пинг", "кинг", "пиу", "тик", "ping", "king", "tick"})
    public static String ping(LongPoolEvent event){
        long time = Utils.getCurrentTimestamp();
        long delta = Math.round(event.msg.getInt("date")-time);
        String response = pings.getString(event.command);

        event.msg_op(1, "%s\nПолучено через %sсек".formatted(response, delta));
        return "ok";
    }
}
