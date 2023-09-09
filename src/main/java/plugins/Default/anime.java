package plugins.Default;

import objects.Event;
import objects.LongPoolEvent;
import objects.SendFile;
import objects.Utils;

import java.util.Arrays;

import static objects.Utils.dp;

public class anime {
    static LongPoolEvent event;
    public anime(LongPoolEvent e) {
        event = e;
        dp.longpoll_event_register("hentai", "hentai", "хентай");
        dp.longpoll_event_register("pics", "anime", "аниме");
    }
    public static String hentai() {
        String arg = "neko";
        if(!Event.args.isEmpty()){
            String args = Event.args.get(0);
            if (Arrays.asList("waifu", "вайфу").contains(args)) {
                arg = "waifu";
            }
        }
        String url = Utils.request("https://api.waifu.pics/nsfw/"+arg).getString("url");
        SendFile.file(url, event, "Вот твой "+arg+" хентай :D");
        return "ok";
    }
    public static String pics() {
        String arg = "neko";
        if(!Event.args.isEmpty()){
            String args = Event.args.get(0);
            if (Arrays.asList("waifu", "вайфу").contains(args)) {
                arg = "waifu";
            } else {
                arg = args;
            }
        }
        String url = Utils.request("https://api.waifu.pics/sfw/"+arg).getString("url");
        SendFile.file(url, event, "Вот твоё "+arg+" :D");
        return "ok";
    }
}
