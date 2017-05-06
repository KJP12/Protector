package co.automod.bot.core;

import co.automod.bot.data.StringUserData;
import com.rethinkdb.gen.exc.ReqlNonExistenceError;

import static co.automod.bot.core.Rethink.connection;
import static co.automod.bot.core.Rethink.r;

public class UserData {

    public static long getBans(String id) {
        try {
            return r.table("users").get(id).getField("bans").run(connection);
        } catch (ReqlNonExistenceError ignored) {
            return 0;
        }
    }

    public static long getLinks(String id) {
        try {
            return r.table("users").get(id).getField("links").run(connection);
        } catch (ReqlNonExistenceError ignored) {
            return 0;
        }
    }

    public static void onBan(String id) {
        r.table("users").insert(new StringUserData(id, getBans(id) + 1, getLinks(id))).optArg("conflict", "replace").runNoReply(connection);
    }

    public static void onLink(String id) {
        r.table("users").insert(new StringUserData(id, getBans(id), getLinks(id) + 1)).optArg("conflict", "replace").runNoReply(connection);
    }
}
