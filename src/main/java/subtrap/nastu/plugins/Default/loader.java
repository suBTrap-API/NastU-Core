package subtrap.nastu.plugins.Default;

import subtrap.nastu.commands.Command;
import subtrap.nastu.commands.CommandBase;
import org.json.JSONObject;
import subtrap.nastu.plugins.PluginLoader;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class loader extends CommandBase {
    @Command(command = "pl", description = "Посмотреть список плагинов")
    public static String plugins(){
        editMessage("Список плагинов(%s):\n\n%s".formatted(PluginLoader.getPlugins().size(), String.join(", ", PluginLoader.getPlugins())));
        return "ok";
    }

    @Command(command = "dlpl", description = "Загрузить плагин с сети", usage = ":p: dlpl <ответ на сообщение>")
    public static String downloadPlugin(){
        if(event.reply_message == null || event.reply_message.isEmpty() || event.reply_message.getJSONArray("attachments").isEmpty()){
            editMessage("Нужно ответить на сообщение с файлом *.jar");
            return "ok";
        }
        JSONObject file = event.reply_message.getJSONArray("attachments").getJSONObject(0).getJSONObject("doc");
        if(!file.getString("ext").equals("jar")){
            editMessage("Нужно ответить на сообщение с файлом *.jar");
            return "ok";
        }
        File plugin = new File("plugins/"+file.getString("title"));
        editMessage("Загружаю плагин...");
        try {
            URL url = new URL(file.getString("url"));
            try(InputStream in = url.openStream()) {
                Files.copy(in, Paths.get(plugin.toURI()));
            }
        } catch (Exception ignored){}
        String name = PluginLoader.loadPlugin(plugin);
        if(name == null){
            editMessage("Файл %s не является плагином для subtrap.nastu.NastU".formatted(file.getString("title")));
            plugin.delete();
            return "ok";
        }
        editMessage("Плагин %s(%s) загружен".formatted(name, file.getString("title")));
        return "ok";
    }

    @Command(command = "rmpl", description = "Удалить плагин", usage = ":p: rmpl <название плагина>")
    public static String removePlugin(List<String> args){
        if (args.isEmpty()){
            editMessage("Введите название плагина");
            return "ok";
        }
        if(!PluginLoader.unloadPlugin(args.get(0), true)){
            editMessage("Плагин %s не найден".formatted(args.get(0)));
            return "ok";
        }
        editMessage("Плагин %s удалён".formatted(args.get(0)));
        return "ok";
    }
}
