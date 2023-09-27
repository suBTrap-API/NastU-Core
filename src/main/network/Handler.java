package subtrap.nastu.network;

import lombok.SneakyThrows;

import java.lang.reflect.Method;

import static subtrap.nastu.network.Event.logger;

public class Handler {
    @SneakyThrows
    public static void handler(LongPoolEvent event, Method func){
        logger.info("Command execution: "+ LongPoolEvent.method +"\nFunction: "+func.getName());
        try {
            func.invoke(event, event);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
