package plugins.Default;

import objects.*;

import java.util.Arrays;

public class anime {
    @Command(command = "hentai", alias = {"хентай"}, description = "Отправляет хентай картинки")
    public static String hentai(LongPoolEvent event) {
        String arg = "neko";
        if(!event.args.isEmpty()){
            String args = event.args.get(0);
            if (Arrays.asList("waifu", "вайфу").contains(args)) {
                arg = "waifu";
            }
        }
        String url = Utils.request("https://api.waifu.pics/nsfw/"+arg).getString("url");
        SendFile.file(url, event, "Вот твой "+arg+" хентай :D");
        return "ok";
    }

    @Command(command = "anime", alias = {"аниме"}, description = "Отправляет аниме картинки")
    public static String pics(LongPoolEvent event) {
        String arg = "neko";
        if(!event.args.isEmpty()){
            arg = event.args.get(0);
        }
        String url = Utils.request("https://api.waifu.pics/sfw/"+arg).getString("url");
        SendFile.file(url, event, "Вот твоё "+arg+" :D");
        return "ok";
    }
}
