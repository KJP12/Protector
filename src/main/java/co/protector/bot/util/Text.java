package co.protector.bot.util;

import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.text.NumberFormat;
import java.util.Locale;

public class Text {
    private static final ThreadLocal<NumberFormat> NUMBER_FORMAT = ThreadLocal.withInitial(() -> NumberFormat.getNumberInstance(Locale.US));

    public static NumberFormat getNumberFormat() {
        return NUMBER_FORMAT.get();
    }

    public static String paste(String input) {
        try {
            return "https://feed-the-wump.us/" + Unirest.post("https://feed-the-wump.us/documents").body(input).asJson().getBody().getObject().getString("key");
        } catch (UnirestException e) {
            e.printStackTrace();
            return null;
        }
    }
}
