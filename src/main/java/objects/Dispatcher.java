package objects;

import lombok.SneakyThrows;
import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Method;

import static objects.Event.logger;

public class Dispatcher {
    JSONObject longpoll_events = new JSONObject();

    public void longpoll_event_register(Object object) {
        Class<?> clazz = object.getClass();
        for (Method method : clazz.getDeclaredMethods()) {
            if (method.isAnnotationPresent(Command.class)) {
                method.setAccessible(true);
                for (String command : method.getAnnotation(Command.class).command()) {
                    longpoll_events.append(command, method);
                }
            }
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
