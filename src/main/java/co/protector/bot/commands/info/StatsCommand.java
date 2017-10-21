package co.protector.bot.commands.info;

import co.protector.bot.Main;
import co.protector.bot.core.listener.command.Command;
import co.protector.bot.util.Text;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;

import java.awt.*;
import java.text.NumberFormat;
import java.util.Arrays;

public class StatsCommand extends Command {
    @Override
    public String getTrigger() {
        return "stats";
    }

    @Override
    public String[] getAliases() {
        return new String[]{};
    }

    @Override
    public String getDescription() {
        return "Statistics";
    }

    @Override
    public String getUsage() {
        return "";
    }


    @Override
    public void execute(Message trigger, String args) {
        NumberFormat numberFormat = Text.getNumberFormat();

        MessageChannel channel = trigger.getChannel();
        long memberCount = Arrays.stream(Main.bot.getShards()).mapToLong(shard -> shard.getJda().getUsers().size()).sum();
        long serversCount = Arrays.stream(Main.bot.getShards()).mapToLong(shard -> shard.getJda().getGuilds().size()).sum();
        long channelCount = Arrays.stream(Main.bot.getShards()).mapToLong(shard -> shard.getJda().getTextChannels().size()).sum();
        int threads = Thread.getAllStackTraces().keySet().size();
        channel.sendMessage(new EmbedBuilder()
                .setAuthor("Protector Stats", null, channel.getJDA().getSelfUser().getAvatarUrl())
                .addField("Server Count", numberFormat.format(serversCount), true)
                .addField("Total Members", numberFormat.format(memberCount), true)
                .addField("Text Channels", numberFormat.format(channelCount), true)
                .addField("Threads", numberFormat.format(threads), true)
                .setThumbnail(channel.getJDA().getSelfUser().getAvatarUrl())
                .setColor(Color.green)
                .build()).queue();
    }

}
