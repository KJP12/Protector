package co.protector.bot;

import com.kaaz.configuration.ConfigurationOption;

public class Config {

    @ConfigurationOption
    public static String discord_token = "your-discord-token";

    @ConfigurationOption
    public static String db_name = "protector";

    @ConfigurationOption
    public static String owner_id = "owner-id";

    @ConfigurationOption
    public static String default_command_prefix = "p!";

    public final static long SHARD_RATELIMIT = 5_000L;
}
