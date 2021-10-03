package config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import util.PathUtil;

import java.io.*;
import java.util.HashMap;
import java.util.Optional;

public class AppConfigHelper {
    private static final transient File CONFIG_FILE = new File(PathUtil.getConfigFolder() + "/config.json");
    private static final transient Gson GSON = new GsonBuilder().serializeNulls().setPrettyPrinting().create();

    public static Optional<AppConfig> read(){
        if(!CONFIG_FILE.exists()){
            save(new AppConfig(AppConfig.defaultConfig()));
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(CONFIG_FILE))){
            AppConfig config = GSON.fromJson(reader, AppConfig.class);
            return Optional.of(config);
        } catch (IOException e){
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public static Object getConfigValue(String key) {
        Optional<AppConfig> appConfig = read();
        if (appConfig.isPresent()) {
            AppConfig config = appConfig.get();
            HashMap<String, Object> configReader = config.getConfig();
            return configReader.get(key);
        }
        return null;
    }

    public static void overwriteProperty(String key, Object value) {
        Optional<AppConfig> config = read();
        config.ifPresent(e -> {
            HashMap<String, Object> configReader = e.getConfig();
            if (configReader.containsKey(key)){
                configReader.put(key, value);
            }
            save(new AppConfig(configReader));
        });
    }

    public static void save(AppConfig configuration) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CONFIG_FILE))) {
            writer.write(GSON.toJson(configuration));
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
