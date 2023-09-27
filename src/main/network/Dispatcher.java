package subtrap.nastu.network;

import subtrap.nastu.commands.Command;
import subtrap.nastu.commands.CommandBase;
import lombok.SneakyThrows;
import subtrap.nastu.utils.Logger;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.Arrays;

public class Dispatcher {

    static Logger logger = new Logger("Dispatcher");
    private final JSONObject longpoll_events = new JSONObject();
    private final JSONObject longpoll_data = new JSONObject();

    public void longpoll_event_register(CommandBase plugin) {
        Class<?> clazz = plugin.getClass();
        for (Method method : clazz.getDeclaredMethods()) {
            if (method.isAnnotationPresent(Command.class)) {
                method.setAccessible(true);
                Command annotation = method.getAnnotation(Command.class);
                longpoll_events.append(annotation.command(), method);
                for (String alias : annotation.alias()) {
                    longpoll_events.append(alias, method);
                }
                JSONObject data = new JSONObject(
                        "{\"command\": \"" + annotation.command() +
                                "\", \"usage\": \""+ annotation.usage() +
                                "\", \"description\": \""+ annotation.description()+
                                "\", \"alias\": "+ Arrays.toString(annotation.alias()) + "}");
                longpoll_data.append(plugin.getName(), data);
            }
        }
    }
    @SneakyThrows
    public void longpoll_event_run(LongPoolEvent event){
        logger.info("Event Handling: "+ LongPoolEvent.method +"\nCommand: "+ event.command);
        if(longpoll_events.has(event.command)){
            Method func = (Method) longpoll_events.getJSONArray(event.command).get(0);
            Handler.handler(event, func);
        }
    }

    public JSONObject getLongpollData() {
        return longpoll_data;
    }
}
