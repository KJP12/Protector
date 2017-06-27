package co.protector.bot.core;

import co.protector.bot.data.StringUserData;
import com.rethinkdb.gen.exc.ReqlNonExistenceError;

public class UserData {

    public static long getBans(String id) {
        try {
            return Rethink.r.table("users").get(id).getField("bans").run(Rethink.connection);
        } catch (ReqlNonExistenceError ignored) {
            return 0;
        }
    }

    public static long getLinks(String id) {
        try {
            return Rethink.r.table("users").get(id).getField("links").run(Rethink.connection);
        } catch (ReqlNonExistenceError ignored) {
            return 0;
        }
    }

    public static void onBan(String id) {
        Rethink.r.table("users").insert(new StringUserData(id, getBans(id) + 1, getLinks(id))).optArg("conflict", "replace").runNoReply(Rethink.connection);
    }

    public static void onLink(String id) {
        Rethink.r.table("users").insert(new StringUserData(id, getBans(id), getLinks(id) + 1)).optArg("conflict", "replace").runNoReply(Rethink.connection);
    }
}
