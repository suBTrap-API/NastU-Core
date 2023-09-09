package objects;

import lombok.SneakyThrows;
import org.json.JSONArray;
import org.json.JSONObject;
import vk.VkApi;

public class SendFile {
    public static void photo(String photo, LongPoolEvent event) {
        photo(photo, event, "", 2, true);
    }
    public static void photo(String photo, LongPoolEvent event, boolean url_path) {
        photo(photo, event, "", 2, url_path);
    }
    public static void photo(String photo, LongPoolEvent event, int mode){
        photo(photo, event, "", mode, true);
    }
    public static void photo(String photo, LongPoolEvent event, int mode, boolean url_path) {
        photo(photo, event, "", mode, url_path);
    }
    public static void photo(String photo, LongPoolEvent event, String text){
        photo(photo, event, text, 2, true);
    }
    public static void photo(String photo, LongPoolEvent event, String text, boolean url_path){
        photo(photo, event, text, 2, url_path);
    }
    public static void photo(String photo, LongPoolEvent event, String text, int mode){
        photo(photo, event, text, mode, true);
    }
    @SneakyThrows
    public static void photo(String photo, LongPoolEvent event, String text, int mode, boolean url_path){
        String upload_url = new JSONObject(VkApi.call("photos.getMessagesUploadServer")).getString("upload_url");
        JSONObject uploaded = Utils.requestF(upload_url, photo, url_path);
        JSONArray a = new JSONArray(VkApi.call("photos.saveMessagesPhoto",
                "server="+uploaded.get("server"), "photo="+uploaded.get("photo"), "hash="+uploaded.get("hash")));
        JSONObject att = new JSONObject(a.get(0).toString());
        event.msg_op(mode, text, "photo"+att.get("owner_id")+"_"+att.get("id"));

    }

    public static void graffiti(String file, LongPoolEvent event){
        graffiti(file, event, true);
    }
    @SneakyThrows
    public static void graffiti(String file, LongPoolEvent event, boolean url_file){
        String upload_url = new JSONObject(VkApi.call("docs.getMessagesUploadServer", "type=graffiti")).getString("upload_url");
        JSONObject uploaded = Utils.requestF(upload_url, file, url_file);
        JSONObject a = new JSONObject(VkApi.call("docs.save",
                "file="+uploaded.get("file")));
        JSONObject att = new JSONObject(a.get("graffiti").toString());
        event.msg_op(1, "", "doc"+att.get("owner_id")+"_"+att.get("id"));
        event.msg_op(3);
    }

    public static void file(String file, LongPoolEvent event) {
        file(file, event, "", 2, true);
    }
    public static void file(String file, LongPoolEvent event, boolean url_path) {
        file(file, event, "", 2, url_path);
    }
    public static void file(String file, LongPoolEvent event, int mode){
        file(file, event, "", mode, true);
    }
    public static void file(String file, LongPoolEvent event, int mode, boolean url_path){
        file(file, event, "", mode, url_path);
    }
    public static void file(String file, LongPoolEvent event, String text){
        file(file, event, text, 2, true);
    }
    public static void file(String file, LongPoolEvent event, String text, boolean url_path){
        file(file, event, text, 2, url_path);
    }
    public static void file(String file, LongPoolEvent event, String text, int mode){
        file(file, event, text, mode);
    }
    @SneakyThrows
    public static void file(String file, LongPoolEvent event, String text, int mode, boolean url_path){
        String upload_url = new JSONObject(VkApi.call("docs.getMessagesUploadServer")).getString("upload_url");
        JSONObject uploaded = Utils.requestF(upload_url, file, url_path);
        JSONObject a = new JSONObject(VkApi.call("docs.save",
                "file="+uploaded.get("file")));
        JSONObject att = new JSONObject(a.get("doc").toString());
        event.msg_op(mode, text, "doc"+att.get("owner_id")+"_"+att.get("id"));
    }
}
