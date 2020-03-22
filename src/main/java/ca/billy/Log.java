package ca.billy;

import lombok.Getter;

public class Log {
    
    @Getter
    private static boolean enabled;

    private static long startTime;

    private static long timer;

    public static void enable() {
        if (!enabled) {
            enabled = true;
            startTime = System.currentTimeMillis();
        }
    }

    public static <T> void log(T msg) {
        if (enabled) {
            System.out.println(msg);
        }
    }

    public static void end() {
        if (enabled) {
            log("Compilation done in " + getSecondsElapsed(startTime));
        }
    }

    public static void startTimer() {
        if (enabled) {
            timer = System.currentTimeMillis();
        }
    }

    public static void printTimer(String msg) {
        if (enabled) {
            log(msg + getSecondsElapsed(timer));
        }
    }

    private static String getSecondsElapsed(long start) {
        long end = System.currentTimeMillis();
        return (end - start) / 1000F + " second(s)";
    }

}
