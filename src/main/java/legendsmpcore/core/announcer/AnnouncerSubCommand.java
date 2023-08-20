package legendsmpcore.core.announcer;
import org.bukkit.entity.Player;

public abstract class AnnouncerSubCommand {
    public abstract String getName();

    public abstract String getDescription();

    public abstract String getSyntax();

    public abstract void perform(Player player, String args[]);

}
