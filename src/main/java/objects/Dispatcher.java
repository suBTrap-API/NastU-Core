package objects;

import lombok.SneakyThrows;
import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Method;

import static objects.Event.logger;

public class Dispatcher {
    JSONObject longpoll_events = new JSONObject();
    @SneakyThrows
    public void longpoll_event_register(String func, String... commands) {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        Class classes = Class.forName(stackTrace[2].getClassName());
        for(String cmd : commands) {
            longpoll_events.append(cmd, classes.getMethod(func));
        }
    }
    @SneakyThrows
    public void longpoll_event_run(LongPoolEvent event){
        logger.info("Event Handling: "+ LongPoolEvent.method +"\nCommand: "+ Event.command);
        if(longpoll_events.has(Event.command)){
            Method func = (Method) ((JSONArray) longpoll_events.get(Event.command)).get(0);
            Handler.handler(event, func);
        }
    }
}
