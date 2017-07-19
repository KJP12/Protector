package co.protector.bot.commands.admin;

import co.protector.bot.core.listener.command.Command;
import co.protector.bot.util.Emoji;
import co.protector.bot.util.Misc;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.utils.MiscUtil;
import net.dv8tion.jda.core.utils.PermissionUtil;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Created by Repulser
 * https://github.com/Repulser
 */
public class PruneCommand extends Command {

    @Override
    public String getTrigger() {
        return "prune";
    }

    @Override
    public String[] getAliases() {
        return new String[]{};
    }

    @Override
    public String getDescription() {
        return "Deletes message with a handful of useful tools";
    }

    @Override
    public String getUsage() {
        return "";
    }

    private boolean checks(Message trigger) {
        if (!PermissionUtil.checkPermission(trigger.getMember(), Permission.MESSAGE_MANAGE)) {
            trigger.getChannel().sendMessage(Emoji.REDX + " **You require the `Manage Messages` permission in order to prune**").queue();
            return false;
        }
        if (!PermissionUtil.checkPermission(trigger.getGuild().getSelfMember(), Permission.MESSAGE_MANAGE)) {
            trigger.getChannel().sendMessage(Emoji.REDX + " **I do not have the `Manage Messages` permission in order to prune**").queue();
            return false;
        }
        return true;
    }

    private boolean messageBeforeTwoWeeks(Message message) {
        long twoWeeksAgo = ((System.currentTimeMillis() - (14 * 24 * 60 * 60 * 1000)) - MiscUtil.DISCORD_EPOCH) << MiscUtil.TIMESTAMP_OFFSET;
        return message.getIdLong() < twoWeeksAgo;
    }

    private void deleteMessages(Message trigger, Predicate<Message> condition, TextChannel channel, int past) {
        if (past > 100) {
            past = 100;
        }
        trigger.delete().queue();
        channel.getHistory().retrievePast(past).queue(
                messages -> {
                    //Gather messages we are going to delete into a list
                    List<Message> deleting = messages.stream().filter(
                            message -> condition.test(message) && !messageBeforeTwoWeeks(message) && !message.isPinned()
                    ).collect(Collectors.toList());
                    if (deleting.size() < 2) {
                        channel.sendMessage(Emoji.X + " **No messages that match the criteria were found**").queue(
                                message -> message.delete().queueAfter(5, TimeUnit.SECONDS)
                        );
                        return;
                    }
                    //Delete and send message
                    channel.deleteMessages(deleting).queue((Void) ->
                            channel.sendMessage(Emoji.GREEN_TICK + " **Pruned `" + deleting.size() + "` messages**").queue(
                                    message -> message.delete().queueAfter(5, TimeUnit.SECONDS)
                            ));

                });
    }

    private void sendUsage(TextChannel channel) {
        channel.sendMessage(getUsage()).queue();
    }

    @Override
    public void execute(Message trigger, String args) {
        if (!checks(trigger)) return;
        TextChannel channel = trigger.getTextChannel();
        if (args.isEmpty()) {
            deleteMessages(trigger, message -> true, channel, 100);
            return;
        }
        String[] parsedArgs = args.split("\\s+");
        if (NumberUtils.isCreatable(parsedArgs[0])) {
            deleteMessages(trigger, message -> true, channel, Integer.parseInt(parsedArgs[0]));
        } else if (Misc.isUserMention(parsedArgs[0])) {
            User user = trigger.getJDA().getUserById(Misc.mentionToId(parsedArgs[0]));
            if (user == null) {
                channel.sendMessage(Emoji.X + " **Unable to find user, this should not happen**").queue();
                return;
            }
            int past = 100;
            if (parsedArgs.length > 1) {
                if (NumberUtils.isCreatable(parsedArgs[1])) {
                    past = Integer.parseInt(parsedArgs[1]);
                }
            }
            deleteMessages(trigger, message -> message.getAuthor() == user, channel, past);
        } else if (parsedArgs[0].equalsIgnoreCase("has")) {
            int past = 100;
            if (parsedArgs.length < 2) {
                sendUsage(channel);
                return;
            }
            String search = String.join(" ", Arrays.copyOfRange(parsedArgs, 1, parsedArgs.length));
            deleteMessages(trigger, message -> message.getContent().toLowerCase().contains(search), channel, 100);
        } else if (parsedArgs[0].equalsIgnoreCase("embeds")) {
            int past = 100;
            if (parsedArgs.length > 1) {
                if (NumberUtils.isCreatable(parsedArgs[1])) {
                    past = Integer.parseInt(parsedArgs[1]);
                }
            }
            deleteMessages(trigger, message -> !message.getEmbeds().isEmpty(), channel, past);
        } else if (parsedArgs[0].equalsIgnoreCase("attachments")) {
            int past = 100;
            if (parsedArgs.length > 1) {
                if (NumberUtils.isCreatable(parsedArgs[1])) {
                    past = Integer.parseInt(parsedArgs[1]);
                }
            }
            deleteMessages(trigger, message -> !message.getAttachments().isEmpty(), channel, past);
        } else if (parsedArgs[0].equalsIgnoreCase("after")) {
            if (parsedArgs.length < 2) {
                sendUsage(channel);
                return;
            }
            channel.getMessageById(parsedArgs[1]).queue(after -> {
                deleteMessages(trigger, message -> message.getCreationTime().isAfter(after.getCreationTime()), channel, 100);
            }, (Throwable) -> channel.sendMessage(Emoji.X + " **Could not find message for provided ID**").queue());
        } else if (parsedArgs[0].equalsIgnoreCase("bot")) {
            if (parsedArgs.length < 1) {
                sendUsage(channel);
                return;
            }
            int past = 100;
            if (parsedArgs.length > 1) {
                if (NumberUtils.isCreatable(parsedArgs[1])) {
                    past = Integer.parseInt(parsedArgs[1]);
                }
            }
            deleteMessages(trigger, message -> message.getAuthor().isBot(), channel, past);
        } else {
            sendUsage(channel);
        }

    }
}
