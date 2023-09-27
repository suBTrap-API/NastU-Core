package subtrap.nastu.utils;

import lombok.SneakyThrows;
import subtrap.nastu.network.LongPoolEvent;
import subtrap.nastu.objects.VKAttach;
import org.json.JSONArray;
import org.json.JSONObject;

import static subtrap.nastu.utils.Utils.vk;

public class SendFile {

    @SneakyThrows
    public static void photo(VKAttach file, LongPoolEvent event, String text, int mode, boolean url_path){
        String upload_url = new JSONObject(vk.call("photos.getMessagesUploadServer")).getString("upload_url");
        JSONObject uploaded = Utils.requestF(upload_url, file.file(), url_path);
        JSONArray a = new JSONArray(vk.call("photos.saveMessagesPhoto",
                "server="+uploaded.get("server"), "photo="+uploaded.get("photo"), "hash="+uploaded.get("hash")));
        JSONObject att = new JSONObject(a.get(0).toString());
        event.msg_op(mode, text, "photo"+att.get("owner_id")+"_"+att.get("id"));
        vk.call("docs.delete", "owner_id="+att.get("owner_id"), "doc_id="+att.get("id"));

    }
    @SneakyThrows
    public static void graffiti(VKAttach file, LongPoolEvent event, boolean url_file){
        String upload_url = new JSONObject(vk.call("docs.getMessagesUploadServer", "type=graffiti")).getString("upload_url");
        JSONObject uploaded = Utils.requestF(upload_url, file.file(), url_file);
        JSONObject a = new JSONObject(vk.call("docs.save",
                "file="+uploaded.get("file")));
        JSONObject att = new JSONObject(a.get("graffiti").toString());
        event.msg_op(1, "", "doc"+att.get("owner_id")+"_"+att.get("id"));
        event.msg_op(3);
        vk.call("docs.delete", "owner_id="+att.get("owner_id"), "doc_id="+att.get("id"));
    }
    @SneakyThrows
    public static void file(VKAttach file, LongPoolEvent event, String text, int mode, boolean url_path){
        String upload_url = new JSONObject(vk.call("docs.getMessagesUploadServer")).getString("upload_url");
        JSONObject uploaded = Utils.requestF(upload_url, file.file(), url_path);
        JSONObject a = new JSONObject(vk.call("docs.save",
                "file="+uploaded.get("file")));
        JSONObject att = new JSONObject(a.get("doc").toString());
        event.msg_op(mode, text, "doc"+att.get("owner_id")+"_"+att.get("id"));
        vk.call("docs.delete", "owner_id="+att.get("owner_id"), "doc_id="+att.get("id"));
    }
}
