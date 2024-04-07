package legendsmpcore.mitigation;

/**
 * Utility class with global variables for Mitigations
 * */
public class Constants {
    public static final String WEBHOOK_URL = "https://discord.com/api/webhooks/1153465780718018722/U5kOPO6__yiYjAsOFfSwA8eBiv2ecQB_Mnh3fsqWerY2A-MgE_cglxzxDpec8H18XZaX\n";

    private static boolean debugMode = true; // TURN TO OFF WHEN NOT DEVELOPMENT

    public static void setDebugMode(boolean debugMode) {
        Constants.debugMode = debugMode;
    }

    public static boolean isDebugMode() {
        return debugMode;
    }
}
