package co.protector.bot.core.listener;


import net.dv8tion.jda.core.entities.User;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by gudenau on 5/11/2017.
 * <p>
 * Rythm
 */
class RateLimiter {
    private static final long LIMIT_PERIOD = 5000;
    private static final int LIMIT_COUNT = 3;

    private static final Object limitMap$lock = new Object[0];
    private static final Map<User, LimitBuffer> limitMap = new HashMap<>();

    static boolean checkIfRateLimited(User user) {
        LimitBuffer userLimit = getLimitBuffer(user);
        long oldestTime = userLimit.getOldestTime();
        long currentTime = System.currentTimeMillis();

        return currentTime - oldestTime < LIMIT_PERIOD;
    }

    private static LimitBuffer getLimitBuffer(User user) {
        synchronized (limitMap$lock) {
            LimitBuffer buffer = limitMap.get(user);
            if (buffer != null) {
                return buffer;
            }
            buffer = new LimitBuffer();
            limitMap.put(user, buffer);
            return buffer;
        }
    }

    private static class LimitBuffer {
        private final long[] timeStamps = new long[LIMIT_COUNT];
        private int lastStamp = 0;

        long getOldestTime() {
            synchronized (this) {
                long time = timeStamps[lastStamp];
                timeStamps[lastStamp] = System.currentTimeMillis();
                lastStamp++;
                if (lastStamp >= LIMIT_COUNT) {
                    lastStamp = 0;
                }
                return time;
            }
        }

        void cooldown(long cooldown) {
            synchronized (this) {
                Arrays.fill(timeStamps, System.currentTimeMillis() + cooldown);
            }
        }
    }
}