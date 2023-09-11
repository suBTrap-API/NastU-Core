package plugins;

import lombok.SneakyThrows;
import objects.LongPoolEvent;
import plugins.Default.*;

import static objects.Utils.dp;
public class Loader {
    public Loader(LongPoolEvent event) {
        dp.longpoll_event_register(new test());
        dp.longpoll_event_register(new anime());
        dp.longpoll_event_register(new misc());
    }
}
