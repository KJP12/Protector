package co.automod.bot.commands.useful;

import co.automod.bot.core.UserData;
import co.automod.bot.core.listener.command.Command;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.*;

import java.awt.*;
import java.util.List;

public class CriminalHistoryCommand extends Command {
    @Override
    public String getTrigger() {
        return "history";
    }

    @Override
    public String[] getAliases() {
        return new String[]{"criminalhistory", "his", "ch"};
    }

    @Override
    public String getDescription() {
        return "Check criminal history on a user";
    }

    @Override
    public String[] getUsage() {
        return new String[0];
    }


    @Override
    public void execute(Guild guild, TextChannel channel, User invoker, Member member, Message message, String args) {
        User checking;
        List<User> mentions = message.getMentionedUsers();
        if (!mentions.isEmpty()) {
            checking = mentions.get(0);
        } else {
            checking = invoker;
        }
        long bans = UserData.getBans(checking.getId());
        long links = UserData.getLinks(checking.getId());
        String user = checking.getName() + "#" + checking.getDiscriminator();
        channel.sendMessage(new EmbedBuilder().setColor(Color.ORANGE).setDescription("__**Criminal History for " + user +
                "**__\n\n\n**User was banned** __**" + bans + "** __**times**\n\n" +
                "**Sent** __**" + links + "**__ " + "**discord links** (And I deleted them)").build()).queue();
    }
}
