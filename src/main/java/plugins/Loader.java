package plugins;

import lombok.SneakyThrows;
import objects.LongPoolEvent;
import plugins.Default.anime;
import plugins.Default.misc;
import plugins.Default.test;

import java.io.File;
import java.util.Objects;

import static objects.Utils.dp;
public class Loader {
    public Loader(LongPoolEvent event) {
        dp.longpoll_event_register(new test());
        dp.longpoll_event_register(new anime());
        dp.longpoll_event_register(new misc());
        loadDir(new File("plugins"));
    }

    @SneakyThrows
    private void loadDir(File path){
        if(!path.exists()) path.mkdir();
        for (final File fileEntry : Objects.requireNonNull(path.listFiles())) {
            if(!fileEntry.isDirectory()) {
                if(fileEntry.getName().endsWith(".jar")) {
                    System.out.println("Loader dir-plugins - Coming Soon...");
                    break;
                }
            }
        }
    }
}
