package legendsmpcore.mitigation.discord;

import java.awt.*;

import static legendsmpcore.mitigation.discord.Constants.webhookURL;
import static org.bukkit.Bukkit.getLogger;

public class AlertDiscord {

    public static void alertDiscord(String reason, Level level) {

        getLogger().info("sending");

        DiscordWebhook webhook = new DiscordWebhook(webhookURL);

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
        }catch (java.io.IOException e){
            getLogger().severe(e.getStackTrace().toString());
        }


    }



}

