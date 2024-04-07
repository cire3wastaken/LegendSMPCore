package legendsmpcore.core.discord;

import legendsmpcore.mitigation.Constants;
import org.bukkit.Bukkit;

import java.awt.*;

/**
 * Utility class to send messages to discord webhook
 * */
public class AlertDiscord {

    public static void alertDiscord(String reason, Level level) {
        DiscordWebhook webhook = new DiscordWebhook(Constants.WEBHOOK_URL);

        Color color = null;

        if(!Constants.isDebugMode()) {
            if (level == Level.CRITICAL) {
                webhook.setContent("@everyone");
                color = Color.RED;
            }

            if (level == Level.NORMAL) {
                webhook.setContent("@here");
                color = Color.ORANGE;
            }

            if (level == Level.LOW) {
                webhook.setContent("@here");
                color = Color.YELLOW;
            }
        } else {
            color = Color.GREEN;
        }

        webhook.addEmbed(new DiscordWebhook.EmbedObject()
                        .setFooter("Level: " + level.toString(), "")
                .setTitle("Security Alert!")
                .setDescription(reason)
                .setColor(color));


        try {
            webhook.execute();
        }catch (java.io.IOException ignored){;
        }
    }

    /**
     * Test method for webhook
     * */
    public static void initMessage(){
        DiscordWebhook webhook = new DiscordWebhook(Constants.WEBHOOK_URL);

        webhook.addEmbed(new DiscordWebhook.EmbedObject()
                .setTitle("Success!")
                .setDescription("Webhook initialized on server")
                .setColor(Color.GREEN));

        webhook.addEmbed(new DiscordWebhook.EmbedObject()
                .setColor(Color.GREEN)
                .setDescription("Server Version: " + Bukkit.getVersion())
                .setTitle("Server Info"));


        try {
            webhook.execute();
        }catch (java.io.IOException ignored){
        }
    }
}

