package subtrap.nastu.plugins.Default;

import subtrap.nastu.commands.Command;
import subtrap.nastu.commands.CommandBase;
import subtrap.nastu.objects.Database;
import org.json.JSONObject;
import subtrap.nastu.utils.Utils;

import java.util.List;

import static subtrap.nastu.utils.Utils.dp;

public class misc extends CommandBase {
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

    @Command(command = "ping", alias = {"кинг", "пиу", "тик", "пинг", "king", "tick"}, description = "Посмотреть пинг")
    public static String ping(){
        long time = Utils.getCurrentTimestamp();
        long delta = Math.round(event.msg.getInt("date")-time);
        String response = pings.getString(event.command);

        editMessage("%s\nПолучено через %sсек".formatted(response, delta));
        return "ok";
    }

    @Command(command = "prefixes", alias = "префиксы", description = "Посмотреть список префиксов")
    public static String prefixes(){
        List pref = (List) Database.instance.lp_settings.get("prefixes");
        String prefix = String.join("\n‣ ", pref);
        editMessage("Список из %s доступных префиксов\n\n‣ %s".formatted(pref.size(), prefix));
        return "ok";
    }

    @Command(command = "help", alias = {"помощь", "хелп"}, description = "Помощь по командам")
    public static String help(List<String> args){
        String prefix = (String) ((List) Database.instance.lp_settings.get("prefixes")).get(0);

        StringBuilder result = new StringBuilder();
        JSONObject commands = dp.getAllCommands();
        if(args.isEmpty()) {
            result.append("Помощь для subtrap.nastu.NastU\nДля получения дополнительной информации о том, как использовать команду, введите %s help <команда>\n\n".formatted(prefix));
            result.append("Список из %s доступных плагинов:\n".formatted(commands.names().length()));
            for (Object name : commands.names()) {
                String command = "";
                for (Object command_data : commands.getJSONArray(name.toString())) {
                    JSONObject data = new JSONObject(command_data.toString());
                    command += data.getString("command") + "&";
                }
                command = String.join(" | ", command.split("&"));

                result.append("‣ %s ➔ 【 %s 】\n".formatted(name, command));
            }
        } else {
            boolean successful = false;
            String name = args.get(0);
            for (Object cat : commands.names()) {
                for (Object command_data : commands.getJSONArray(cat.toString())) {
                    JSONObject data = new JSONObject(command_data.toString());
                    if (data.getString("command").equals(name) || data.getJSONArray("alias").toList().contains(name)) {

                        List aliases = data.getJSONArray("alias").toList();

                        result.append("Помощь для \""+data.getString("command")+"\"\n");
                        if(!data.getString("description").equals("")) result.append("— "+ data.getString("description")+"\n\n");
                        if(!aliases.isEmpty()) result.append("‣ Псевдонимы ➔ 【 %s 】\n\n".formatted(String.join(", ", aliases)));
                        if(!data.getString("usage").equals("")) result.append("Использоывние:\n"+data.getString("usage").replace(":p:", prefix));
                        successful = true;
                    }
                }
            }
            if (!successful) {
                editMessage("Команда с именем " + name + " несущетсвует\n" + prefix + " help - для получения справки");
                return "ok";
            }
        }

        editMessage(result.toString());
        return "ok";
    }
}
