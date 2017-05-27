package automod.bot.data;

import java.beans.ConstructorProperties;

public class StringUserData {

    private String id;
    private long bans;
    private long links;

    @ConstructorProperties({"id", "bans", "links"})
    public StringUserData(String uid, long bans, long links) {
        this.id = uid;
        this.bans = bans;
        this.links = links;
    }

    public String getId() {
        return id;
    }

    public long getbans() {
        return bans;
    }

    public long getlinks() {
        return links;
    }
}
