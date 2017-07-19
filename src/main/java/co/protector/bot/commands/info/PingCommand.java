package co.protector.bot.commands.info;

import co.protector.bot.core.listener.command.Command;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;

import java.awt.*;

public class PingCommand extends Command {
    @Override
    public String getTrigger() {
        return "ping";
    }

    @Override
    public String[] getAliases() {
        return new String[]{};
    }

    @Override
    public String getDescription() {
        return "Check the bots response time";
    }

    @Override
    public String getUsage() {
        return "";
    }


    @Override
    public void execute(Message trigger, String args) {
        MessageChannel channel = trigger.getChannel();
        long startTime = System.currentTimeMillis();
        channel.sendTyping().complete();
        long stopTime = System.currentTimeMillis();
        long ms = stopTime - startTime;
        channel.sendMessage(new EmbedBuilder()
                .setDescription("**Pong.**\nTime: " + String.valueOf(ms) + "ms")
                .setColor(Color.green).build()).queue();

    }
}
