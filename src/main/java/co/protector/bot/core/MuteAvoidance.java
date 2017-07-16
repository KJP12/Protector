package co.protector.bot.core;

import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.events.channel.text.TextChannelCreateEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

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

}
