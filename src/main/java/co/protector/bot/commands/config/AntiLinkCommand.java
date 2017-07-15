package co.protector.bot.commands.config;

import co.protector.bot.core.Settings;
import co.protector.bot.core.listener.command.Command;
import co.protector.bot.util.Misc;
import co.protector.bot.core.GuildConfiguration;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.*;
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
    public String[] getUsage() {
        return new String[]{
                "antilink", "toggles Anti Link",
                "antilink on/off", "turns Anti Link on or off"
        };
    }


    @Override
    public void execute(Guild guild, TextChannel channel, User invoker, Member member, Message message, String args) {
        boolean hasPerms = PermissionUtil.checkPermission(member, Permission.MANAGE_SERVER);
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
