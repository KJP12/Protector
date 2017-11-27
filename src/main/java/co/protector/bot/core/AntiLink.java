package co.protector.bot.core;

import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.events.message.guild.GuildMessageUpdateEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import net.dv8tion.jda.core.utils.PermissionUtil;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AntiLink extends ListenerAdapter {
    private static final Pattern discordURL = Pattern.compile("di?sco?rd(?:([.,|<>\\\\]*(?:me|io|li|gg|com)|(?:sites|list)[.,|<>\\\\]*(?:me|com))[/|?\\\\]*|app[.,|<>\\\\]*com[/|?\\\\]*(?:invite|oauth2)[/|?\\\\]*)\\w+");
    private final Permission[] ignoredPerms = {Permission.MANAGE_SERVER, Permission.MANAGE_ROLES, Permission.BAN_MEMBERS, Permission.KICK_MEMBERS};
    private final Map<String, Long> messageCooldown = new ConcurrentHashMap<>();

    private Boolean enabled(Guild guild) {
        return Settings.getSetting(guild).antilink;
    }

    private String cleanString(String input) {
        return input.replaceAll("\\p{C}", "")
                .replace(" ", "")
                .toLowerCase();
    }

    private boolean ignoreMember(Member member) {
        return Arrays.stream(ignoredPerms).anyMatch(perm -> PermissionUtil.checkPermission(member, perm));
    }

    private void handleMessage(Message message) {
        String content = message.getRawContent();
        if (message.getAuthor().getId().equals(message.getJDA().getSelfUser().getId())) return;
        if (!enabled(message.getGuild())) return;
        if (ignoreMember(message.getMember())) return;
        String cleanContent = cleanString(content);
        Matcher m = discordURL.matcher(cleanContent);
        if (m.find()) {
            message.delete().queue(a -> {
                UserData.onLink(message.getAuthor().getId());
                if (messageCooldown.containsKey(message.getAuthor().getId())) {
                    long lastLink = messageCooldown.get(message.getAuthor().getId());
                    if ((System.currentTimeMillis() - lastLink) < 300000) return;
                }
                message.getChannel().sendMessage(String.format("%s \u26D4 **Advertising is not allowed!**", message.getAuthor().getAsMention())).queue();
                messageCooldown.put(message.getAuthor().getId(), System.currentTimeMillis());
            });
        }
    }

    @Override
    public void onGuildMessageUpdate(GuildMessageUpdateEvent e) {
        handleMessage(e.getMessage());
    }

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent e) {
        handleMessage(e.getMessage());
    }
}
