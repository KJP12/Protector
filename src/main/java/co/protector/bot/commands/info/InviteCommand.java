package co.protector.bot.commands.info;

import co.protector.bot.core.listener.command.Command;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Message;

public class InviteCommand extends Command {
    private static final String embedDescription = "[Add me](https://discord.io/protector)\n[Official Discord](https://discord.io/botz)\n[Github](https://github.com/repulser/automod)\n";

    @Override
    public String getTrigger() {
        return "invite";
    }

    @Override
    public String[] getAliases() {
        return new String[]{};
    }

    @Override
    public String getDescription() {
        return "Links!";
    }

    @Override
    public String getUsage() {
        return "";
    }


    @Override
    public void execute(Message trigger, String args) {
        trigger.getChannel().sendMessage(new EmbedBuilder()
                .setDescription(embedDescription).build()).queue();

    }
}
