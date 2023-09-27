package subtrap.nastu.plugins;

import lombok.SneakyThrows;
import subtrap.nastu.network.LongPoolEvent;
import org.json.JSONObject;
import subtrap.nastu.plugins.Default.loader;
import subtrap.nastu.plugins.Default.misc;
import subtrap.nastu.utils.StreamUtils;
import subtrap.nastu.utils.Utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
public class PluginLoader extends PluginBase {
    private static final HashMap<String, List> plugin_classes = new HashMap<>();
    private static final ArrayList<String> plugins = new ArrayList();
    public PluginLoader(LongPoolEvent event) {
        registerCommand("Default", new misc());
        registerCommand("Loader", new loader());
        loadDir("plugins");
    }

    public static void loadDir(String path) {
        File dir = new File(path);
        if (!dir.exists()) dir.mkdir();
        for (File fileEntry : Objects.requireNonNull(dir.listFiles())) {
            loadPlugin(fileEntry);
        }
    }

    public static String loadPlugin(File pluginFile) {
        if (pluginFile.isFile()) {
            if (pluginFile.getName().endsWith(".jar")) {
                try {
                    JarFile plugin = new JarFile(pluginFile);

                    InputStream stream;

                    load:
                    {
                        JSONObject var1;
                        JarEntry jarEntry = plugin.getJarEntry("plugin.json");
                        if (jarEntry == null) {
                            plugin.close();
                            break load;
                        }
                        stream = plugin.getInputStream(jarEntry);

                        try {
                            var1 = new JSONObject(StreamUtils.readFile(stream));
                        } catch (Throwable throwable) {
                            if (stream != null) {
                                try {
                                    stream.close();
                                } catch (Throwable var8) {
                                    throwable.addSuppressed(var8);
                                }
                            }

                            throw throwable;
                        }
                        if (stream != null) {
                            stream.close();
                        }
                        plugin.close();
                        String mainClass = var1.getString("main");
                        String name = var1.getString("name");
                        String version = var1.getString("version");
                        URLClassLoader classLoader = new URLClassLoader(new URL[]{pluginFile.toURI().toURL()}, PluginLoader.class.getClassLoader());
                        try {
                            Class javaClass = classLoader.loadClass(mainClass);
                            if (!PluginBase.class.isAssignableFrom(javaClass)) {
                                Utils.logger.debug("Main class `" + mainClass + "' does not extend PluginBase");
                                return null;
                            }
                            try {
                                Class<PluginBase> pluginClass = javaClass.asSubclass(PluginBase.class);
                                PluginBase pl = pluginClass.newInstance();
                                pl.NAME = name;
                                File plugin_data = new File("plugins_data/" + name);
                                if (!plugin_data.isDirectory()) plugin_data.mkdirs();
                                pl.DATA_FOLDER = plugin_data.getAbsolutePath();
                                pl.VERSION = version;
                                pl.onLoad();
                                Utils.logger.info("Loading plugin " + name + " v" + version);
                                plugin_classes.put(name, List.of(pluginFile, classLoader, version));
                                plugins.add(name+" v"+version);
                                return name;
                            } catch (ClassCastException var9) {
                                Utils.logger.error("Error whilst initializing main class `" + mainClass + "'");
                                return null;
                            } catch (IllegalAccessException | InstantiationException var10) {
                                Utils.logger.error("An exception happened while initializing the plugin %s, %s, %s".formatted(pluginFile, mainClass, name));
                                return null;
                            } catch (NoSuchMethodError var11) {
                                Utils.logger.error(var11.getMessage());
                                return null;
                            }
                        } catch (ClassNotFoundException var11) {
                            Utils.logger.debug("Couldn't load plugin " + name + ": main class not found");
                            return null;
                        }
                    }
                } catch (IOException exception) {
                    Utils.logger.error(exception.getMessage());
                    return null;
                }
            }
        }
        return null;
    }

    @SneakyThrows
    public static boolean unloadPlugin(String name, boolean removePlugin){
        if(plugin_classes.containsKey(name)){
            List plugin = plugin_classes.get(name);
            ((URLClassLoader)plugin.get(1)).close();
            if(removePlugin)
                ((File)plugin.get(0)).delete();

            unregisterCommands(name);
            plugin_classes.remove(name);
            plugins.remove(name+" v"+plugin.get(2));
            return true;
        }
        return false;
    }

    public static List<String> getPlugins(){
        return plugins;
    }
}