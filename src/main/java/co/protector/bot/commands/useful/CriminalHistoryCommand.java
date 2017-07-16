package co.protector.bot.commands.useful;

import co.protector.bot.core.UserData;
import co.protector.bot.core.listener.command.Command;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.User;

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
    public void execute(Message trigger, String args) {
        User checking;
        List<User> mentions = trigger.getMentionedUsers();
        if (!mentions.isEmpty()) {
            checking = mentions.get(0);
        } else {
            checking = trigger.getAuthor();
        }
        int bans = UserData.getBans(checking.getId());
        int links = UserData.getLinks(checking.getId());
        String user = checking.getName() + "#" + checking.getDiscriminator();
        trigger.getChannel().sendMessage(new EmbedBuilder()
                .setColor(Color.ORANGE)
                .setDescription("__**Criminal History for " + user +
                        "**__\n\n\n**User was banned** __**" + bans + "** __**times**\n\n" +
                        "**Sent** __**" + links + "**__ " + "**discord links** (And I deleted them)")
                .build()).queue();
    }
}
