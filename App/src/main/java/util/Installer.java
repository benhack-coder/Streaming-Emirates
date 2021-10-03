package util;

import config.AppConfigHelper;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;

public class Installer {
    private static final String GSTREAMER_URL = "https://www.dropbox.com/s/lnr7kfdihtgdgcw/gstreamer.zip?dl=1";
    private static final String BATCH_URL = "https://www.dropbox.com/s/0387w8r4vgsaph5/SetGStreamerEnv.bat?dl=1";
    public boolean install() {
        PathUtil.OSPath();
        File file1 = new File(PathUtil.getApplicationFolder());
        if (!file1.exists()){
            if (!file1.mkdir()){
                return false;
            }
            return false;
        }
        File file = new File(PathUtil.getApplicationFolder() + "\\gstreamer");
        if (!file.exists()) {
            if (!file.mkdir()) {
                return false;
            }
        }
        try {
        downloadFile(PathUtil.getGStreamerFolder() + "\\gstreamer.zip", GSTREAMER_URL);
        downloadFile(PathUtil.getGStreamerFolder() + "\\SetGStreamerEnv.bat", BATCH_URL);
        unzip(PathUtil.getGStreamerFolder() + "\\gstreamer.zip", PathUtil.getGStreamerFolder());
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }

        try {
            setEnviromentVariable();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return false;
        }
        AppConfigHelper.overwriteProperty("gstreamerInstalled", true);
        return true;
    }

    private void unzip(String zipFilePath, String destDir) {
        System.out.println("Extracting Files...");
        try {
            ZipFile zipFile = new ZipFile(zipFilePath);
            zipFile.extractAll(destDir);

        } catch (ZipException e) {
            e.printStackTrace();
        }
        System.out.println("Extracted Files.");

    }
    private void setEnviromentVariable() throws IOException, InterruptedException {
        System.out.println("Setting Enviroment Variable...");
        Runtime rf = Runtime.getRuntime();

        Process process = rf.exec("cmd /c start \"GStreamerInstaller\" \"" + PathUtil.getGStreamerFolder() + "\\SetGStreamerEnv.bat\"\n");
        process.waitFor();  // Wait for the process to complete
        if (process.exitValue() == 0){
            System.out.println("Enviroment Variable Set.");
        }
    }

    private void downloadFile(String zipFilePath, String FILE_URL){
        System.out.println("Downloading File...");
        try (BufferedInputStream in = new BufferedInputStream(new URL(FILE_URL).openStream());
             FileOutputStream fileOutputStream = new FileOutputStream(zipFilePath)) {
            byte[] dataBuffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                fileOutputStream.write(dataBuffer, 0, bytesRead);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return;
            // handle exception
        }
        System.out.println("Download finished.");
    }
}
