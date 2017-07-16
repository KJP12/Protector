package co.protector.bot.core;

import co.protector.bot.Config;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.UpdateOptions;
import org.bson.Document;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;

public class Database {
    public static final MongoClient mongoClient = new MongoClient();
    public static final MongoDatabase db = mongoClient.getDatabase(Config.db_name);
    private static final UpdateOptions updateOptions = new UpdateOptions().upsert(true);


    public static void saveConfigField(String collection, Document data, String id) {
        db.getCollection(collection).replaceOne(eq("_id", id), data.append("_id", id), updateOptions);
    }

    public static Document getDocument(String id, String collection) {
        Document query = db.getCollection(collection).find(eq("_id", id)).first();
        if (query == null) return new Document();
        return query;
    }

    public static String getMutedRole(String id) {
        return getDocument(id, "mute").getString("role");
    }

    @SuppressWarnings("unchecked")
    public static List<String> getMutedUsers(String id) {
        List<String> users = (List<String>) getDocument(id, "muted").get("users");
        if(users == null) return new ArrayList<>();
        return users;
    }

    public static void addMutedUser(String id, String user) {
        List<String> muted = getMutedUsers(id);
        muted.add(user);
        saveConfigField("muted", new Document()
        .append("users", muted), id);
    }

    public static void removeMutedUser(String id, String user) {
        List<String> muted = getMutedUsers(id);
        muted.remove(user);
        saveConfigField("muted", new Document()
                .append("users", muted), id);
    }

}
