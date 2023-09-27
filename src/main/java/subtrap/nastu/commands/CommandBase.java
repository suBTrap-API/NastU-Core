package subtrap.nastu.commands;

import subtrap.nastu.network.LongPoolEvent;
import subtrap.nastu.objects.VKAttach;
import subtrap.nastu.utils.Logger;
import subtrap.nastu.utils.SendFile;

public class CommandBase {
    protected static LongPoolEvent event;
    private static Logger logger;
    public CommandBase(){
        logger = new Logger(this.getClass());
    }
    protected static Logger getLogger(){
        return logger;
    }

    protected static void sendMessage(String text){
        event.msg_op(1, text);
    }
    protected static void sendMessage(String text, String attachments){
        event.msg_op(1, text, attachments);
    }
    protected static void editMessage(String text){
        event.msg_op(2, text);
    }
    protected static void editMessage(String text, String attachments){
        event.msg_op(2, text, attachments);
    }

    protected static void deleteMessage(){
        event.msg_op(3);
    }
    protected static void deleteMessage(boolean deleteForAll){
        if(deleteForAll) event.msg_op(3);
        else event.msg_op(4);
    }

    protected static void sendPhoto(VKAttach file){
        sendPhoto(file, "");
    }
    protected static void sendPhoto(VKAttach file, String text){
        sendPhoto(file, text, true);
    }
    protected static void sendPhoto(VKAttach file, boolean urlFile){
        sendPhoto(file, "", urlFile);
    }
    protected static void sendPhoto(VKAttach file, String text, boolean urlFile){
        SendFile.photo(file, event, text, 1, urlFile);
    }

    protected static void sendGraffiti(VKAttach file){
        sendGraffiti(file, true);
    }
    protected static void sendGraffiti(VKAttach file, boolean urlFile){
        SendFile.graffiti(file, event,urlFile);
    }

    protected static void sendFile(VKAttach file){
        sendFile(file, "");
    }
    protected static void sendFile(VKAttach file, String text){
        sendFile(file, text, true);
    }
    protected static void sendFile(VKAttach file, boolean urlFile){
        sendFile(file, "", urlFile);
    }
    protected static void sendFile(VKAttach file, String text, boolean urlFile){
        SendFile.file(file, event, text, 1, urlFile);
    }

    protected static void editMessage(VKAttach attachment){
        editMessage(attachment, true);
    }
    protected static void editMessage(String text, VKAttach attachment){
        editMessage(text, attachment, false);
    }
    protected static void editMessage(VKAttach attachment, boolean urlFile){
        editMessage("", attachment, false, urlFile);
    }
    protected static void editMessage(String text, VKAttach attachment, boolean asFile){
        editMessage(text, attachment, false, true);
    }
    protected static void editMessage(VKAttach attachment, boolean asFile, boolean urlFile){
        editMessage("", attachment, asFile, urlFile);
    }
    protected static void editMessage(String text, VKAttach attachment, boolean asFile, boolean urlFile){
        SendFile.file(attachment, event, text, 2, urlFile);
    }

    public void setEvent(LongPoolEvent longPoolEvent){
        event = longPoolEvent;
    }
}
