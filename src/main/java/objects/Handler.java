package objects;

import lombok.SneakyThrows;

import java.lang.reflect.Method;

import static objects.Event.logger;

public class Handler {
    @SneakyThrows
    public static void handler(LongPoolEvent event, Method func){
        logger.info("Command execution: "+ LongPoolEvent.method +"\nFunction: "+func.getName());
        try {
            func.invoke(event);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
