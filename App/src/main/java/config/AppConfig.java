package config;

import java.util.HashMap;


public class AppConfig {
    private final HashMap<String, Object> config;
    public AppConfig(HashMap<String, Object> config){
        this.config = config;
    }

    public static HashMap<String, Object> defaultConfig(){
        HashMap<String, Object> defaultConfig = new HashMap<>();
        defaultConfig.put("darkmode", false);
        defaultConfig.put("loggedin", false);
        defaultConfig.put("username", "");
        defaultConfig.put("password", "");
        defaultConfig.put("gstreamerInstalled", false);
        return defaultConfig;
    }

    public HashMap<String, Object> getConfig() {
        return config;
    }
}
