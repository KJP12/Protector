package co.protector.bot.commands.admin;

import co.protector.bot.core.Database;
import co.protector.bot.core.listener.command.Command;
import co.protector.bot.util.Emoji;
import co.protector.bot.util.Misc;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.utils.PermissionUtil;

/**
 * Created by Repulser
 * https://github.com/Repulser
 */
public class UnmuteCommand extends Command {
    @Override
    public String getTrigger() {
        return "unmute";
    }

    @Override
    public String[] getAliases() {
        return new String[]{};
    }

    @Override
    public String getDescription() {
        return "Unmutes a member";
    }

    @Override
    public String[] getUsage() {
        return new String[0];
    }

    private boolean canManageRole(Member author) {
        return PermissionUtil.checkPermission(author.getGuild().getSelfMember(), Permission.MANAGE_ROLES)
                &&
                PermissionUtil.canInteract(author.getGuild().getSelfMember(), author);
    }

    private boolean checks(Message trigger, Member target) {
        if (!PermissionUtil.checkPermission(trigger.getMember(), Permission.KICK_MEMBERS)) {
            trigger.getChannel().sendMessage(Emoji.REDX + " **You require the `Kick Members` permission in order to unmute**").queue();
            return false;
        }
        if (!canManageRole(target)) {
            trigger.getChannel().sendMessage(Emoji.REDX + " **I cannot assign this user roles**").queue();
            return false;
        }
        return true;
    }

    @Override
    public void execute(Message trigger, String args) {
        Guild guild = trigger.getGuild();
        if (args.isEmpty()) {
            trigger.getChannel().sendMessage(Emoji.X + " **Who exactly should I unmute?**").queue();
            return;
        }
        User user = Misc.findUser(trigger.getTextChannel(), args);
        if (user == null) {
            trigger.getChannel().sendMessage(Emoji.X + " **Could not find user**").queue();
            return;
        }
        Member target = guild.getMember(user);
        if (!checks(trigger, target)) return;
        String id = Database.getMutedRole(guild.getId());
        if (id == null) {
            trigger.getChannel().sendMessage(Emoji.WARN +
                    " **The mute role was never configured in this server, start by muting someone**").queue();
            return;
        }
        if (target.getRoles().stream().noneMatch(role -> role.getId().equals(id))) {
            trigger.getChannel().sendMessage(Emoji.WARN + " **This user is not muted**").queue();
            return;
        }
        Role role = guild.getRoleById(id);
        if (role == null) {
            trigger.getChannel().sendMessage(Emoji.WARN +
                    " **The mute role was never configured in this server, start by muting someone**").queue();
            return;
        }
        if (!target.getRoles().contains(role)) {
            trigger.getChannel().sendMessage(Emoji.WARN + " **This user is not muted**").queue();
            return;
        }
        guild.getController().removeRolesFromMember(target, role).queue();
        Database.removeMutedUser(guild.getId(), target.getUser().getId());
        trigger.getChannel().sendMessage(Emoji.GREEN_TICK + " **Unmuted** `" + target.getUser().getName() + "#" + target.getUser().getDiscriminator() + "`").queue();
    }
}
