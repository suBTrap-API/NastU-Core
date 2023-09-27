package subtrap.nastu.network;

import lombok.SneakyThrows;

import java.lang.reflect.Method;

import static subtrap.nastu.network.Event.logger;

public class Handler {
    @SneakyThrows
    public static void handler(LongPoolEvent event, Method func){
        logger.info("Command execution: "+ LongPoolEvent.method +"\nFunction: "+func.getName());
        try {
            func.invoke(event, event.args);
        } catch (IllegalArgumentException exception){
            if(exception.getMessage().equals("wrong number of arguments: 1 expected: 0")) {
                func.invoke(event);
                return;
            }
            logger.error("Function `"+ func.getDeclaringClass().getName() + "."+ func.getName() + "` is not static");
        } catch (Exception exception) {
            logger.error(exception.getMessage());
            throw exception;
        }
    }
}
