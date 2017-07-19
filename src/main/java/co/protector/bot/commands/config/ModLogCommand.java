package co.protector.bot.commands.config;

import co.protector.bot.core.Settings;
import co.protector.bot.core.listener.command.Command;
import co.protector.bot.settings.ModLogChannelSetting;
import co.protector.bot.util.Misc;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.utils.PermissionUtil;

import java.util.List;


public class ModLogCommand extends Command {
    @Override
    public String getTrigger() {
        return "modlog";
    }

    @Override
    public String[] getAliases() {
        return new String[]{};
    }

    @Override
    public String getDescription() {
        return "Configure the Mod Log which logs deleted messages, edited messages and much more";
    }

    @Override
    public String getUsage() {
        return "modlog #channel" + " Sets the channel the Mod Log will go to\n" +
                "modlog off" + " disables the Mod Log";

    }

    @Override
    public void execute(Message trigger, String args) {
        Guild guild = trigger.getGuild();
        TextChannel channel = trigger.getTextChannel();
        Member member = trigger.getMember();
        boolean hasPerms = PermissionUtil.checkPermission(member, Permission.MANAGE_SERVER);
        if (!hasPerms) return;
        if (args.isEmpty()) {
            if (Settings.getSetting(guild).modlog.isEmpty()) {
                channel.sendMessage("\u274C **Mod Log is not set**").queue();
            } else {
                channel.sendMessage("\u2705 ** The modlog is set to " + guild.getTextChannelById(Settings.getSetting(guild).modlog).getAsMention() + "**").queue();
            }
            return;
        }
        if (Misc.isFuzzyFalse(args)) {
            Settings.update(guild, ModLogChannelSetting.class, "");
            channel.sendMessage("\u2705 **Disabled modlog**").queue();
            return;
        }
        List<TextChannel> ments = trigger.getMentionedChannels();
        if (!Settings.update(guild, ModLogChannelSetting.class, args)) {
            channel.sendMessage("\u274C **You must mention a channel you want the modlog to be in**").queue();
            return;
        }
        TextChannel ch = ments.get(0);
        channel.sendMessage("\u2705 **Set " + ch.getAsMention() + " as the modlog channel**").queue();
    }
}
