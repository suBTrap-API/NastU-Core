package objects;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Arrays;
import java.util.logging.Logger;

class DB_exp {
    public static String db_file = new File("src/main/java/database.json").getAbsolutePath();
    public static Logger logger = Logger.getLogger("Database");
    public static JSONObject read(String rel_path){
        try {
            logger.info("Reading "+ rel_path);
            JSONParser parser = new JSONParser();
            return (JSONObject) parser.parse(new FileReader(rel_path));
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }
    public static void write(String rel_path, JSONObject data){
        logger.info("Editing "+rel_path);
        try (FileWriter file = new FileWriter(rel_path)) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String json = gson.toJson(data);
            file.write(json);
            file.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void set(String rel_path, String key, Object value){
        logger.info("Editing "+rel_path);
        JSONObject db = read(rel_path);
        db.put(key, value);
        try (FileWriter file = new FileWriter(rel_path)) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String json = gson.toJson(db);
            file.write(json);
            file.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

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

public class Database extends DB_exp {
    public static Database instance;
    public long owner_id = 0;
    public String access_token = "Not specified";
    JSONObject setting = DB_defaults.settings;
    public JSONObject lp_settings = DB_defaults.lp_settings;
    public Database(){
        instance = this;
        try {
            access_token = DB_exp.read(DB_exp.db_file).get("access_token").toString();
            owner_id = (long) DB_exp.read(DB_exp.db_file).get("owner_id");
            lp_settings = (JSONObject) DB_exp.read(DB_exp.db_file).get("lp_settings");
            setting = (JSONObject) DB_exp.read(DB_exp.db_file).get("settings");
        } catch (Exception e){
            e.printStackTrace();
            save();
        }
    }
    public void save(){
        DB_exp.logger.info("Saving the database");
        DB_exp.write(DB_exp.db_file, DB_defaults.load_user(instance));
    }
    public static DB_exp getExp(){
        return new DB_exp();
    }
}