package com.playernguyen.authes.util;

import java.io.*;

public class FileUtils {

    public static String fileToString(File file) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        String ls = System.getProperty("line.separator");
        while ((line = reader.readLine()) != null) {
            stringBuilder.append(line);
            stringBuilder.append(ls);
        }
        // delete the last new line separator
        stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        reader.close();

        return stringBuilder.toString();
    }

    public static void resourceToFile(InputStream resource, File file) throws IOException {
        try (FileOutputStream outputStream = new FileOutputStream(file)) {
            int read;
            byte[] bytes = new byte[1024];

            while (true) {
                assert resource != null;
                if ((read = resource.read(bytes)) == -1) break;
                outputStream.write(bytes, 0, read);
            }
        }
    }

}
