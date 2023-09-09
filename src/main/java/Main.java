import modules.JsonUtils;
import objects.Database;
import objects.Event;
import objects.LongPoolEvent;
import org.json.JSONArray;
import org.json.JSONObject;
import plugins.Loader;
import vk.LongPool;
import vk.Methods;
import vk.VkApi;

import java.util.List;
import java.util.Scanner;

import static objects.Utils.dp;

public class Main{
    static VkApi vk;
    static LongPool longpool;
    static LongPoolEvent event;
    public static Database db = new Database();
    public static void main(String[] args) throws Exception {
        if(db.access_token.length() < 20) {
            Scanner scan = new Scanner(System.in);
            System.out.println("Insert you token here:");
            String token = scan.next();
            int owner;
            try {
                JSONArray arr = new JSONArray(new VkApi(token).callns("users.get"));
                owner = new JSONObject(arr.get(0).toString()).getInt("id");
            } catch (Exception e) {
                throw new RuntimeException("Incorrect token, try again");
            }
            db.access_token = token;
            db.owner_id = owner;
            db.save();
        }
        run();
    }
    public static void run() throws Exception {
        vk = new VkApi(db.access_token);
        longpool = new LongPool(vk);
        while (true) {
            new Loader(event);
            JSONArray update = (JSONArray) longpool.listen();
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
                    String message = JsonUtils.getArray(vk.callns(Methods.getById, "message_ids=" + update.get(1)), "items").toString();
                    message = message.replace(update.get(5).toString(), update.get(5).toString().replace("<br>", "\n"));

                    event = new LongPoolEvent(new Event(vk, JsonUtils.json(message)), new JSONObject("{message:" + message + "}"));
                    dp.longpoll_event_run(event);
                }
            }
        }
    }
}
