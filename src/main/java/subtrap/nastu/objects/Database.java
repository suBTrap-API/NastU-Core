package subtrap.nastu.objects;

import org.json.simple.JSONObject;
import subtrap.nastu.utils.Config;
import subtrap.nastu.utils.Logger;

import java.util.Arrays;

class DB_defaults {
    static JSONObject settings = new JSONObject();
    static JSONObject lp_settings = new JSONObject();

    public static void setDB(){
        settings.put("silent_deleting", true);

        lp_settings.put("ignored_users", "[]");
        lp_settings.put("prefixes", Arrays.asList(".л", "!л", ".l", "!l", ".лп", "!лп", ".lp", "!lp"));
        lp_settings.put("binds", "{}");
        lp_settings.put("key", "");
    }
    public static JSONObject load_user(){
        return load_user(null);
    }
    public static JSONObject load_user(Database inst){
        setDB();
        if(inst == null) inst = Database.instance;
        JSONObject data = new JSONObject();
        data.put("owner_id", inst.owner_id);
        data.put("access_token", inst.access_token);
        data.put("lp_settings", inst.lp_settings);
        data.put("settings", inst.setting);
        return data;
    }
}

public class Database {
    public static Database instance;
    public long owner_id = 0;
    public String access_token = "Not specified";
    JSONObject setting = DB_defaults.settings;
    public JSONObject lp_settings = DB_defaults.lp_settings;

    static Logger logger = new Logger("Database");

    static Config db = new Config("./database.json");
    public Database(){
        instance = this;
        try {
            access_token = db.get("access_token").toString();
            owner_id = (long) db.get("owner_id");
            lp_settings = (JSONObject) db.get("lp_settings");
            setting = (JSONObject) db.get("settings");
            logger.info("Reading "+ db.getPath());
        } catch (Exception e){
            logger.error(e.getMessage());
            save();
        }
    }
    public void save(){
        logger.info("Saving the database");
        logger.info("Writing "+db.getPath());
        db.write(DB_defaults.load_user(instance));
    }
    public static Config getConfig(){
        return db;
    }
}