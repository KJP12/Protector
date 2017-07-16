package co.protector.bot.commands.config;

import co.protector.bot.core.Settings;
import co.protector.bot.core.listener.CommandListener;
import co.protector.bot.core.listener.command.Command;
import co.protector.bot.settings.PrefixSetting;
import co.protector.bot.util.Emoji;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.utils.PermissionUtil;

import java.awt.*;

public class ServerPrefixCommand extends Command {
    @Override
    public String getTrigger() {
        return "serverprefix";
    }

    @Override
    public String[] getAliases() {
        return new String[]{"sp"};
    }

    @Override
    public String getDescription() {
        return "Change the bots prefix in your server";
    }

    @Override
    public String[] getUsage() {
        return new String[0];
    }


    @Override
    public void execute(Message trigger, String args) {
        TextChannel channel = trigger.getTextChannel();
        boolean hasPerms = PermissionUtil.checkPermission(trigger.getMember(), Permission.MANAGE_SERVER);
        if (!hasPerms) return;
        if (args.isEmpty()) {
            channel.sendMessage(new EmbedBuilder().setColor(Color.red).setDescription(String.format("**\u274C Missing args**\n\n%sserverprefix [Prefix]", CommandListener.getPrefix(trigger.getGuild()))).build()).queue();
            return;
        }
        String prefix = args.replace(" ", "");
        if (Settings.update(trigger.getGuild(), PrefixSetting.class, prefix)) {
            channel.sendMessage(Emoji.OK + " **Set the prefix to `" + prefix + "`**").queue();
        } else {
            channel.sendMessage(Emoji.X + " **Failed to update prefix!**").queue();
        }
    }
}
