package subtrap.nastu.plugins;

import subtrap.nastu.commands.CommandBase;
import subtrap.nastu.network.Dispatcher;
import subtrap.nastu.utils.Logger;

import static subtrap.nastu.utils.Utils.dp;

public class PluginBase {
    protected static String NAME;
    protected static String DATA_FOLDER;
    protected static String VERSION;
    public PluginBase(){

    }

    public void onLoad() {

    }

    protected static Logger getLogger(){
        return new Logger(getName());
    }

    protected static String getName(){
        return NAME;
    }
    protected static String getVersion(){
        return VERSION;
    }
    public static String getDataFolder(){
        return DATA_FOLDER;
    }
    protected Dispatcher getDispatcher() {
        return dp;
    }

    protected static void registerCommand(CommandBase command){
        registerCommand(command, true);
    }
    protected static void registerCommand(CommandBase command, boolean show){
        dp.registerCommand(getName(), command, show);
    }
    protected static void registerCommand(String name, CommandBase command){
        registerCommand(name, command, true);
    }
    protected static void registerCommand(String name, CommandBase command, boolean show){
        dp.registerCommand(name, command, show);
    }

    protected static void unregisterCommands(String name){
        dp.unregisterCommands(name);
    }
}
