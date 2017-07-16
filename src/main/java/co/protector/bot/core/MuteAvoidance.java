package co.protector.bot.core;

import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.events.channel.text.TextChannelCreateEvent;
import net.dv8tion.jda.core.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.util.List;

/**
 * Created by Repulser
 * https://github.com/Repulser
 */
public class MuteAvoidance extends ListenerAdapter {

    @Override
    public void onTextChannelCreate(TextChannelCreateEvent event) {
        Guild guild = event.getGuild();
        String id = Database.getMutedRole(guild.getId());
        if (id != null) {
            Role role = guild.getRoleById(id);
            if (role != null) {
                event.getChannel().createPermissionOverride(role).setDeny(Permission.MESSAGE_WRITE).queue();
            }
        }
    }

    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent event) {
        String guildid = event.getGuild().getId();
        List<String> muted = Database.getMutedUsers(guildid);
        if(muted.contains(event.getMember().getUser().getId())) {
            String id = Database.getMutedRole(guildid);
            if(id != null) {
                Role role = event.getGuild().getRoleById(id);
                if(role != null) {
                    event.getGuild().getController().addRolesToMember(event.getMember(), role).queue();
                }
            }
        }
    }

}
