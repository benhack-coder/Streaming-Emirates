package util;

public class PathUtil {
    private static String applicationFolder;
    private static String videoFolder;
    private static String configFolder;
    private static String GStreamerFolder;

    public static void OSPath() {
        String operatingSystem = System.getProperty("os.name").toUpperCase();

        switch (operatingSystem) {
            case "MAC OS X" -> {
                applicationFolder = System.getenv("HOME") + "/Library/Application Support/streaming-emirates";
                videoFolder = applicationFolder + "/videos";
                configFolder = applicationFolder + "/config";
            }
            case "WINDOWS 10" -> {
                applicationFolder = System.getenv("appdata") + "\\streaming-emirates";
                videoFolder = applicationFolder + "\\videos";
                configFolder = applicationFolder + "\\config";
                GStreamerFolder = applicationFolder + "\\gstreamer";
            }
        }
    }

    public static String getApplicationFolder() {
        return applicationFolder;
    }

    public static String getVideoFolder() {
        return videoFolder;
    }

    public static String getConfigFolder() {
        return configFolder;
    }

    public static String getGStreamerFolder() {
        return GStreamerFolder;
    }
}