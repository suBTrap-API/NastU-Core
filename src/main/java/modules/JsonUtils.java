package modules;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class JsonUtils extends HashMap implements Map {
    static JSONObject jsonData;

    public JsonUtils(){
        jsonData = new JSONObject();

    }

    public static JsonUtils get(String key) {
        return (JsonUtils) jsonData.get(key);
    }
    public static JSONObject get(String json, String key) {
        Object data = json(json).get(key);
        jsonData = (JSONObject) data;
        return (JSONObject) data;
    }

    public JsonUtils put(String key, Object value){
        jsonData.put(key, value);
        System.out.println(jsonData);
        return this;
    }

    public static Integer getInt(String key) {
        return jsonData.getInt(key);
    }
    public static Integer getInt(String json, String key) {
        jsonData = json(json);
        return jsonData.getInt(key);
    }

    public static String getString(String key) {
        return jsonData.getString(key);
    }
    public static String getString(String json, String key) {
        jsonData = json(json);
        return jsonData.getString(key);
    }

    public static JSONObject getArray(String key){
        return getArray(key, 0);
    }
    public static JSONObject getArray(String key, int index){
        JSONArray data = (JSONArray) jsonData.get(key);
        jsonData = (JSONObject) data.get(index);
        return (JSONObject) data.get(index);
    }
    public static JSONObject getArray(String json, String key){
        return getArray(json, key, 0);
    }
    public static JSONObject getArray(String json, String key, int index){
        JSONArray data = (JSONArray) json(json).get(key);
        jsonData = (JSONObject) data.get(index);
        return (JSONObject) data.get(index);
    }

    public static JSONObject json(String json){
        jsonData = new JSONObject(json);
        return jsonData;
    }
    public boolean has(String key) {
        return jsonData.has(key);
    }
}
