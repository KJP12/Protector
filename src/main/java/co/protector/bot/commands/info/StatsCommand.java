package co.protector.bot.commands.info;

import co.protector.bot.Main;
import co.protector.bot.core.listener.command.Command;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;

import java.awt.*;
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
        MessageChannel channel = trigger.getChannel();
        String MemberCount = String.valueOf(Arrays.stream(Main.bot.getShards()).mapToLong(shard -> shard.getJda().getUsers().size()).sum());
        String serversCount = String.valueOf(Arrays.stream(Main.bot.getShards()).mapToLong(shard -> shard.getJda().getGuilds().size()).sum());
        String channelCount = String.valueOf(Arrays.stream(Main.bot.getShards()).mapToLong(shard -> shard.getJda().getTextChannels().size()).sum());
        int Threads = Thread.getAllStackTraces().keySet().size();
        channel.sendMessage(new EmbedBuilder()
                .setAuthor("ModBot Stats", null, channel.getJDA().getSelfUser().getAvatarUrl())
                .addField("Server Count", serversCount, true)
                .addField("Total Members", MemberCount, true)
                .addField("Text Channels", channelCount, true)
                .addField("Threads", String.valueOf(Threads), true)
                .setThumbnail(channel.getJDA().getSelfUser().getAvatarUrl())
                .setColor(Color.green)
                .build()).queue();
    }

}
