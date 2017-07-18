package co.protector.bot.util;

import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

public class Text {

    public static String paste(String input) {
        try {
            return "https://feed-the-wump.us/" + Unirest.post("https://feed-the-wump.us/documents").body(input).asJson().getBody().getObject().getString("key");
        } catch (UnirestException e) {
            e.printStackTrace();
            return null;
        }
    }
}
