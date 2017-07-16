package co.protector.bot.core;

import co.protector.bot.Config;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.UpdateOptions;
import org.bson.Document;

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
        return Database.getDocument(id, "mute").getString("role");
    }
}
