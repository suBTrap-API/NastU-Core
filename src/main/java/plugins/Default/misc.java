package plugins.Default;

import objects.Command;
import objects.Database;
import objects.LongPoolEvent;
import objects.Utils;
import org.json.JSONObject;

import java.util.List;

import static objects.Utils.dp;

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

    @Command(command = "пинг", alias = {"кинг", "пиу", "тик", "ping", "king", "tick"}, description = "Посмотреть пинг")
    public static String ping(LongPoolEvent event){
        long time = Utils.getCurrentTimestamp();
        long delta = Math.round(event.msg.getInt("date")-time);
        String response = pings.getString(event.command);

        event.msg_op(1, "%s\nПолучено через %sсек".formatted(response, delta));
        return "ok";
    }

    @Command(command = "help", alias = {"помощь", "хелп"}, description = "Помощь по командам")
    public static String help(LongPoolEvent event){
        StringBuilder result = new StringBuilder("Помощь по командам\n\n");
        String prefix = (String) ((List) Database.instance.lp_settings.get("prefixes")).get(0);
        JSONObject commands = dp.getLongpollData();
        for(Object name: commands.names()){
            String alias_str = "";
            String description_str = "";

            List alias = commands.getJSONObject(name.toString()).getJSONArray("alias").toList();
            String description = commands.getJSONObject(name.toString()).getString("description");

            if(!alias.isEmpty()) alias_str = "(%s)".formatted(String.join(", ", alias));
            if(!description.equals("")) description_str = " - "+ description;

            result.append("%s %s%s%s\n".formatted(prefix, name, alias_str, description_str));
        }

        event.msg_op(2, result.toString());
        return "ok";
    }
}
