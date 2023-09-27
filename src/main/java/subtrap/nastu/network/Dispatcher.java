package subtrap.nastu.network;

import subtrap.nastu.commands.Command;
import subtrap.nastu.commands.CommandBase;
import lombok.SneakyThrows;
import org.json.JSONArray;
import org.json.JSONObject;
import subtrap.nastu.utils.Logger;

import java.lang.reflect.Method;
import java.util.Arrays;

public class Dispatcher {

    static Logger logger = new Logger("Dispatcher");
    private final JSONObject longpoll_events = new JSONObject();
    private final JSONObject commands_data = new JSONObject();

    public void registerCommand(String name, CommandBase plugin) {
        registerCommand(name, plugin, true);
    }
    public void registerCommand(String name, CommandBase plugin, boolean show) {
        Class<? extends CommandBase> clazz = plugin.getClass();
        for (Method method : clazz.getDeclaredMethods()) {
            if (method.isAnnotationPresent(Command.class)) {
                method.setAccessible(true);
                Command annotation = method.getAnnotation(Command.class);
                longpoll_events.append(annotation.command(), plugin).append(annotation.command(), method);
                for (String alias : annotation.alias()) {
                    longpoll_events.append(alias, plugin).append(alias, method);
                }
                if(show) {
                    JSONObject data = new JSONObject(
                            "{\"command\": \"" + annotation.command() +
                                    "\", \"usage\": \"" + annotation.usage() +
                                    "\", \"description\": \"" + annotation.description() +
                                    "\", \"alias\": " + Arrays.toString(annotation.alias()) + "}");
                    commands_data.append(name, data);
                }
            }
        }
    }
    public void unregisterCommands(String name){
        if(commands_data.has(name)) {
            for(Object data : commands_data.getJSONArray(name)) {
                String cmd = ((JSONObject) data).getString("command");
                longpoll_events.remove(cmd);
                for (Object alias : ((JSONObject)data).getJSONArray("alias")) {
                    longpoll_events.remove(alias.toString());
                }
            }
            commands_data.remove(name);
        }
    }
    @SneakyThrows
    public void commandRun(LongPoolEvent event){
        logger.info("Event Handling: "+ LongPoolEvent.method +"\nCommand: "+ event.command);
        if(longpoll_events.has(event.command)) {
            JSONArray plugin = longpoll_events.getJSONArray(event.command);
            CommandBase clazz = (CommandBase) plugin.get(0);
            Method function = (Method) plugin.get(1);
            clazz.setEvent(event);
            Handler.handler(event, function);
        }
    }

    public JSONObject getAllCommands() {
        return commands_data;
    }
}
