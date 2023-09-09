package objects;

import lombok.SneakyThrows;

import java.lang.reflect.Method;

import static objects.Event.logger;

public class Handler {
    @SneakyThrows
    public static Object handler(LongPoolEvent event, Method func){
        logger.info("Command execution: "+ event.method +"\nF:"+func.getName());
        try {
            return func.invoke(event);
        } catch (Exception e) {return null;}
    }
}
