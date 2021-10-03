package common;

import java.io.File;
import java.util.Objects;

public class FileManager {

    public static void deleteAllFilesInDirectory(File dir) {
        for (File file : Objects.requireNonNull(dir.listFiles())) {
            if (file.isDirectory())
                deleteAllFilesInDirectory(file);
            if (!file.delete()){
                //TODO Exception Handling
                System.out.println("Something went wrong");
            }
        }
    }
}
