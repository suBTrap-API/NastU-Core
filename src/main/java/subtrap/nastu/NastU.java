package subtrap.nastu;

import org.json.JSONArray;
import org.json.JSONObject;
import subtrap.nastu.network.Event;
import subtrap.nastu.network.LongPoolEvent;
import subtrap.nastu.network.vk.LongPool;
import subtrap.nastu.network.vk.Methods;
import subtrap.nastu.network.vk.VkApi;
import subtrap.nastu.objects.Database;
import subtrap.nastu.plugins.PluginLoader;
import subtrap.nastu.utils.Logger;
import subtrap.nastu.utils.Utils;

import java.net.UnknownHostException;
import java.util.List;
import java.util.Scanner;

public class NastU {
    static VkApi vk;
    static LongPool longpool;
    static LongPoolEvent event;
    public static Database db = new Database();
    static Logger logger = new Logger("Main");
    public static void main(String[] args) throws Exception {
        if(db.access_token.length() < 20) {
            Scanner scan = new Scanner(System.in);
            System.out.println("Insert you token here:");
            String token = scan.next();
            int owner;
            try {
                JSONArray arr = new JSONArray(new VkApi(token).call("users.get"));
                owner = new JSONObject(arr.get(0).toString()).getInt("id");
            } catch (Exception e) {
                throw new IllegalArgumentException("Incorrect token, try again");
            }
            db.access_token = token;
            db.owner_id = owner;
            db.save();
        }
        vk = new VkApi(db.access_token);
        new Utils(vk);
        run();
    }
    public static void run() throws Exception {
        logger.info("subtrap.nastu.NastU Running...");
        try {
            longpool = new LongPool(vk);
            new PluginLoader(event);
            while (true) {
                JSONArray update = longpool.listen();
                if (update == null) {
                    continue;
                }
                if ((int) update.get(0) != 4) {
                    continue;
                }
                if (((int) update.get(2) & 2) == 2) {
                    String[] words = update.get(5).toString().replace("<br>", "").split(" ", 2);
                    String first_word = words[0].toLowerCase();
                    List prefixes = (List) db.lp_settings.get("prefixes");

                    if (prefixes.contains(first_word)) {
                        JSONObject data = new JSONObject(vk.call(Methods.getById, "message_ids=" + update.get(1)));
                        String message = data.getJSONArray("items").getJSONObject(0).toString();
                        message = message.replace("<br>", "\n");

                        event = new LongPoolEvent(new Event(vk, new JSONObject(message)), new JSONObject("{message:" + message + "}"));
                        Utils.dp.commandRun(event);

                    }
                }
            }
        } catch (UnknownHostException exception){
            logger.error("Не удалось подключиться к хосту "+exception.getMessage());
            logger.error("Возможно, у вас нет подключения к Интернету");
        }
    }
}
