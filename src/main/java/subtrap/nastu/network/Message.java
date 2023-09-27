package subtrap.nastu.network;

import subtrap.nastu.utils.ListUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import subtrap.nastu.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Message {
    String text;
    List<String> args;
    String payload;
    String command;
    List attachments;
    JSONObject reply;
    JSONArray fwd;

    public Message(JSONObject msg){
        Pattern pattern = Pattern.compile("(\\S+)|\\n(.*)");
        Matcher matcher = pattern.matcher(msg.get("text").toString());

        List<String> listMatches = new ArrayList<>();

        while(matcher.find())
        {
            listMatches.add(matcher.group(0));
        }
        listMatches.remove(0);
        command = ((!listMatches.isEmpty()) ? listMatches.remove(0) : "");
        reply = (msg.has("reply_message") ? (JSONObject) msg.get("reply_message") : new JSONObject("{}"));
        fwd = (msg.has("fwd_messages") ? (JSONArray) msg.get("fwd_messages") : new JSONArray("[]"));
        text = msg.get("text").toString();
        args = new ArrayList<>();
        payload = "";
        for(ListUtils.EnumeratedItem<String> lst : ListUtils.enumerate(listMatches, 1)) {
            if(lst.item.split("\n")[0].length() > 0){
                args.add(lst.item);
            }
            else{
                payload += ((lst.index < listMatches.size()) ? lst.item : lst.item+("\n"));
            }
        }
        attachments = Utils.att_parse((JSONArray) msg.get("attachments"));
    }
}
