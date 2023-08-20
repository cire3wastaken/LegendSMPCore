package legendsmpcore.mitigation;

public class Constants {

    public static final String WEBHOOK_URL = "https://discord.com/api/webhooks/1135710919679283290/fxLu7JUv0kIJESQkHdCDbkg8Dh-iEma-7bp_ssK-p1X1cVl1q2slOyNs8KtpqDNWooO8";

    private static boolean debugMode = false;

    public static void setDebugMode(boolean debugMode) {
        Constants.debugMode = debugMode;
    }

    public static boolean isDebugMode() {
        return debugMode;
    }
}
