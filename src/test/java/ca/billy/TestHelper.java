package ca.billy;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import org.junit.Assert;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TestHelper {

    public static String run() throws IOException {
        Process pro = Runtime.getRuntime().exec("java Main");

        String line;
        StringBuilder builder = new StringBuilder();
        BufferedReader in = new BufferedReader(new InputStreamReader(pro.getInputStream()));
        while ((line = in.readLine()) != null) {
            builder.append(line).append('\n');
        }

        StringBuilder errorBuilder = new StringBuilder();
        BufferedReader inError = new BufferedReader(new InputStreamReader(pro.getErrorStream()));
        while ((line = inError.readLine()) != null) {
            errorBuilder.append(line).append('\n');
        }
        if (!errorBuilder.toString().isEmpty()) {
            Log.log(errorBuilder.toString());
            Assert.fail("Compilation error");
        }

        return builder.toString();
    }

    public static void removeGeneratedFile() {
        new File("Main.class").delete();
    }
}
