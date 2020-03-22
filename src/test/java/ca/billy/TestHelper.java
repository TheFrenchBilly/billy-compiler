package ca.billy;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TestHelper {

    public static String run() throws IOException {
        Process pro = Runtime.getRuntime().exec("java Main");

        StringBuilder builder = new StringBuilder();
        String line;
        BufferedReader in = new BufferedReader(new InputStreamReader(pro.getInputStream()));
        while ((line = in.readLine()) != null) {
            builder.append(line);
        }

        return builder.toString();
    }
    
    public static void removeGeneratedFile() {
        new File("Main.class").delete();
    }
}
