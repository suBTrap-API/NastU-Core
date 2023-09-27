package subtrap.nastu.utils;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class StreamUtils {
    public static String readFile(InputStream inputStream) throws IOException {
        return readFile(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
    }

    private static String readFile(Reader reader) throws IOException {
        BufferedReader br = new BufferedReader(reader);

        String var4;
        try {
            StringBuilder stringBuilder = new StringBuilder();

            for(String temp = br.readLine(); temp != null; temp = br.readLine()) {
                if (stringBuilder.length() != 0) {
                    stringBuilder.append("\n");
                }

                stringBuilder.append(temp);
            }

            var4 = stringBuilder.toString();
        } catch (Throwable var6) {
            try {
                br.close();
            } catch (Throwable var5) {
                var6.addSuppressed(var5);
            }

            throw var6;
        }

        br.close();
        return var4;
    }
}
