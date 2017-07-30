package co.protector.bot.commands.admin;

import co.protector.bot.core.Database;
import co.protector.bot.core.listener.command.Command;
import co.protector.bot.util.Emoji;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.utils.PermissionUtil;

import java.util.Map;

/**
 * Created by Repulser
 * https://github.com/Repulser
 */
public class MuteListCommand extends Command {

    @Override
    public String getTrigger() {
        return "mutelist";
    }

    @Override
    public String[] getAliases() {
        return new String[]{};
    }

    @Override
    public String getDescription() {
        return "Lists muted users in the current guild";
    }

    @Override
    public String getUsage() {
        return "";
    }

    @Override
    public void execute(Message trigger, String args) {
        if (!PermissionUtil.checkPermission(trigger.getMember(), Permission.VOICE_MUTE_OTHERS)) {
            trigger.getChannel().sendMessage(Emoji.REDX + " **You require the `Mute Members` permission in order to view the mute list**").queue();
            return;
        }
        Guild guild = trigger.getGuild();
        Map<String, String> muted = Database.getMutedUsers(guild.getId());
        if (muted.isEmpty()) {
            trigger.getChannel().sendMessage(Emoji.WARN + " **No muted users found in** `" + guild.getName() + "`").queue();
            return;
        }
        StringBuilder description = new StringBuilder();
        for (Map.Entry<String, String> entry : muted.entrySet()) {
            String mute = entry.getKey();
            String muter = entry.getValue();
            Member mutedMember = guild.getMemberById(mute);
            Member muterMember = guild.getMemberById(muter);
            if (mutedMember == null) continue;
            description.append("`")
                    .append(mutedMember.getUser().getName())
                    .append("#")
                    .append(mutedMember.getUser().getDiscriminator())
                    .append(mutedMember.getNickname() != null ? " (" + mutedMember.getNickname() + ")" : "")
                    .append("`")
                    .append(" Muted by: `")
                    .append(muterMember != null ? muterMember.getUser().getName() + "#" + muterMember.getUser().getDiscriminator() : "Unknown")
                    .append(muterMember != null ? (muterMember.getNickname() != null ? " (" + muterMember.getNickname() + ")" : "") : "")
                    .append("`")
                    .append("\n\n");
        }
        trigger.getChannel().sendMessage(
                new EmbedBuilder()
                        .setDescription(description.toString())
                        .build()).queue();
    }
}
