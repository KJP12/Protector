package co.automod.bot.commands.admin;

import co.automod.bot.core.listener.command.Command;
import co.automod.bot.util.Emoji;
import co.automod.bot.util.Misc;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.utils.PermissionUtil;

public abstract class ModAction extends Command {


    abstract Permission getRequiredPermission();

    abstract boolean doModAction(Guild guild, Member member, String args);

    @Override
    public void execute(Guild guild, TextChannel channel, User invoker, Member member, Message message, String args) {
        if (getRequiredPermission() != null) {
            if (!PermissionUtil.checkPermission(guild, guild.getMember(invoker), getRequiredPermission())) {
                channel.sendMessage(String.format(Emoji.REDX + " It seems like you don't have permission to %s! Make sure that you are able to **%s**", getTrigger(), getRequiredPermission().getName())).queue();
                return;
            }
            if (!PermissionUtil.checkPermission(guild, guild.getSelfMember(), getRequiredPermission())) {
                channel.sendMessage(Emoji.REDX + " **I am not allowed to %s members!** Make sure that I have permission to **%s**", getTrigger(), getRequiredPermission().getName()).queue();
                return;
            }
        }
        if (args.isEmpty()) {
            channel.sendMessage(String.format(Emoji.X + " **Who exactly should I %s?**", getTrigger())).queue();
            return;
        }
        User targetUser = Misc.findUser(channel, args);
        if (targetUser == null) {
            channel.sendMessage(String.format("Can't find **%s**! I guess he's pretty good at hide & seek.. %s", args, Emoji.EYES)).queue();
            return;
        }
        if (targetUser.getIdLong() == guild.getJDA().getSelfUser().getIdLong()) {
            channel.sendMessage(String.format("I'm not going to %s myself..!", getTrigger())).queue();
            return;
        }
        if (targetUser.getIdLong() == invoker.getIdLong()) {
            channel.sendMessage("Don't be so hard on yourself! \uD83D\uDC96 \u2728").queue();
            return;
        }
        if (!PermissionUtil.canInteract(guild.getSelfMember(), guild.getMember(targetUser))) {
            channel.sendMessage("I can't %s the user %s!", getTrigger(), targetUser.getName()).queue();
            return;
        }
        if (doModAction(guild, guild.getMember(targetUser), args)) {
            channel.sendMessage(String.format("%s **%s** is gone!", getTrigger().equals("kick") ? Emoji.KICK : Emoji.BAN, targetUser.getName() + "#" + targetUser.getDiscriminator())).queue();
            return;
        }
        channel.sendMessage("Failed to %s %s. Sad isn't it?", getTrigger(), targetUser.getName());
    }
}
