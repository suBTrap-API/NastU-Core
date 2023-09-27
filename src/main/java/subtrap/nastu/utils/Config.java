package subtrap.nastu.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

public class Config {
    private final String path;
    private final File configFile;
    private JSONObject configData;

    public Config(File file){
        configFile = file;
        path = file.getAbsolutePath();
        try {
            if(!file.exists()) {
                FileWriter writer = new FileWriter(file);

                writer.write(file.getName().endsWith(".json") ? "{}" : "");
                writer.close();
            }
            JSONParser parser = new JSONParser();
            configData = (JSONObject) parser.parse(new FileReader(file));
        } catch (Exception ignored){
            configData = new JSONObject();
        }
    }

    public Config(String file){
        this(new File(file));
    }

    public Object get(String key, Object defaults){
        if(configData.containsKey(key)){
            return configData.get(key);
        }
        return defaults;
    }
    public Object get(String key){
        return get(key, false);
    }
    public void write(JSONObject data){
        try (FileWriter file = new FileWriter(getPath())) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String json = gson.toJson(data);
            file.write(json);
            file.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void set(String key, Object value){
        configData.put(key, value);
        try (FileWriter file = new FileWriter(getPath())) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String json = gson.toJson(configData);
            file.write(json);
            file.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getPath() {
        return path;
    }

    public JSONObject getConfigData() {
        return configData;
    }
}
