package co.protector.bot.commands.config;

import co.protector.bot.core.GuildConfiguration;
import co.protector.bot.core.Settings;
import co.protector.bot.core.listener.command.Command;
import co.protector.bot.util.Misc;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.utils.PermissionUtil;

public class AntiLinkCommand extends Command {
    @Override
    public String getTrigger() {
        return "antilink";
    }

    @Override
    public String[] getAliases() {
        return new String[]{"al"};
    }

    @Override
    public String getDescription() {
        return "Toggle Anti Link setting, deletes invite links to avoid advertising.";
    }

    @Override
    public String getUsage() {
        return "antilink" + "toggles Anti Link\n" +
                "antilink on/off" + " turns Anti Link on or off";
    }


    @Override
    public void execute(Message trigger, String args) {
        Guild guild = trigger.getGuild();
        MessageChannel channel = trigger.getChannel();
        boolean hasPerms = PermissionUtil.checkPermission(trigger.getMember(), Permission.MANAGE_SERVER);
        GuildConfiguration setting = Settings.getSetting(guild);
        if (!hasPerms) return;
        Boolean needsUpdating = true;
        if (Misc.isFuzzyFalse(args) || setting.antilink) {
            needsUpdating = false;
        }
        Settings.update(guild, "antilink", String.valueOf(needsUpdating));
        if (needsUpdating) {
            channel.sendMessage("\u2705 **Anti Link has been enabled!**").queue();
        } else {
            channel.sendMessage("\u274C **Anti Link has been disabled**").queue();
        }
    }
}
