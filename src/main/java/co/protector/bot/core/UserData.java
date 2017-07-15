package co.protector.bot.core;

import org.bson.Document;

public class UserData {

    public static int getBans(String id) {
        Object bans = Database.getDocument(id, "users").get("bans");
        if (bans == null) return 0;
        return (int) bans;
    }

    public static int getLinks(String id) {
        Object links = Database.getDocument(id, "users").get("links");
        if (links == null) return 0;
        return (int) links;
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
