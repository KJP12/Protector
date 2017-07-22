package co.protector.bot.core;

import org.bson.Document;

public class UserData {

    public static long getBans(String id) {
        Long bans = Database.getDocument(id, "users").getLong("bans");
        if (bans == null) return 0;
        return bans;
    }

    public static long getLinks(String id) {
        Long links = Database.getDocument(id, "users").getLong("links");
        if (links == null) return 0;
        return links;
    }

    public static void onBan(String id) {
        Database.saveConfigField("users", new Document()
                .append("bans", getBans(id) + 1)
                .append("links", getLinks(id)), id);
    }

    public static void onLink(String id) {
        Database.saveConfigField("users", new Document()
                .append("links", getLinks(id) + 1)
                .append("bans", getBans(id)), id);
    }
}
