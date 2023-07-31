package legendsmpcore.mitigation.discord;

import legendsmpcore.mitigation.Constants;

import java.awt.*;

public class AlertDiscord {

    public static void alertDiscord(String reason, Level level) {
        DiscordWebhook webhook = new DiscordWebhook(Constants.WEBHOOK_URL);

        Color color = null;

        if (level == Level.CRITICAL){
            color = Color.RED;
        }

        if (level == Level.NORMAL){
            color = Color.ORANGE;
        }

        if (level == Level.LOW){
            color = Color.YELLOW;
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
}

