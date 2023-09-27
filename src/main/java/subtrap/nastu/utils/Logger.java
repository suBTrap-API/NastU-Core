package subtrap.nastu.utils;

import java.sql.Timestamp;
import java.util.HashMap;

public class Logger {
    private String threader = "";
    public static final String INFO = "info";
    public static final String DEBUG = "debug";
    public static final String WARNING = "warning";
    public static final String ERROR = "error";

    public static HashMap foregroundColors = new HashMap();
    public static HashMap backgroundColors = new HashMap();

    private static void init() {
        foregroundColors.put("black", "0;30");
        foregroundColors.put("dark_gray", "1;30");
        foregroundColors.put("red", "0;31");
        foregroundColors.put("light_red", "1;31");
        foregroundColors.put("green", "0;32");
        foregroundColors.put("light_green", "1;32");
        foregroundColors.put("yellow", "0;33");
        foregroundColors.put("light_yellow", "1;33");
        foregroundColors.put("blue", "0;34");
        foregroundColors.put("light_blue", "1;34");
        foregroundColors.put("purple", "0;35");
        foregroundColors.put("light_purple", "1;35");
        foregroundColors.put("cyan", "0;36");
        foregroundColors.put("light_cyan", "1;36");
        foregroundColors.put("light_gray", "0;37");
        foregroundColors.put("white", "1;37");

        backgroundColors.put("black", "40");
        backgroundColors.put("red", "41");
        backgroundColors.put("green", "42");
        backgroundColors.put("yellow", "43");
        backgroundColors.put("blue", "44");
        backgroundColors.put("magenta", "45");
        backgroundColors.put("cyan", "46");
        backgroundColors.put("light_gray", "47");
    }
    public Logger(){
        init();
    }

    public Logger(String thread){
        init();
        threader = thread;
    }

    public Logger(Class thread){
        init();
        threader = thread.getSimpleName();
    }

    public void info(String string){
        log(INFO, threader, string, "blue");
    }

    public static void info(String string, String thread){
        log(INFO, thread, string, "blue");
    }

    public void debug(String string){
        log(DEBUG, threader, string, "purple");
    }

    public static void debug(String string, String thread){
        log(DEBUG, thread, string, "purple");
    }

    public void warning(String string){
        log(WARNING, threader, string, "yellow");
    }

    public static void warning(String string, String thread){
        log(WARNING, thread, string, "yellow");
    }

    public void error(String string){
        log(ERROR, threader, string, "red");
    }

    public static void error(String string, String thread){
        log(ERROR, thread, string, "red");
    }

    public static void log(String level, String thread, String string, String foreground){
        log(level, string, thread, foreground, null);
    }

    public static void log(String level, String string, String foreground){
        log(level, string, null, foreground, null);
    }
    public static void log(String level, String string, String thread, String foreground, String background){
        if(thread != null && !thread.equals("")){
            thread = " " + thread + " thread | ";
        } else thread = " ";
        String log = color(new Timestamp(System.currentTimeMillis()).toString().split("\\.")[0].replace("-", ".") + " >", foreground, background)  + color(thread + level.toUpperCase() + " > " + string, null);
        System.out.println(log);
    }

    public static String color(String string, String foregroundColor){
        return color(string, foregroundColor, null);
    }
    public static String color(String string, String foregroundColor, String backgroundColor){
        init();
        String color_string = "";
        if(foregroundColors.containsKey(foregroundColor)){
            color_string += "\033[" + foregroundColors.get(foregroundColor) + "m \r";
        }
        if(backgroundColors.containsKey(backgroundColor)){
            color_string += "\033[" + backgroundColors.get(backgroundColor) + "m \r";
        }
        color_string += string + "\033[0m";
        return color_string;
    }
}
