package cn.bitoffer.msgcenter.redis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;

@Component
@Slf4j
public class TimerMsgCache {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    public String GetCacheKey(){
        return "Timer_Msgs";
    }

    public void cacheSaveMsgTimePoint(long sendTimestamp){
        redisTemplate.opsForZSet().add(GetCacheKey(), sendTimestamp, sendTimestamp);
    }

    public List<String> getOnTimePointsFromCache(){
        try {
            List<String>  result = excuteScript(GetCacheKey(),0l, System.currentTimeMillis()/1000);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private List<String> excuteScript(String key, Long field1, Long field2) {
        String script = "local elements = redis.call('ZRANGEBYSCORE', KEYS[1], ARGV[1], ARGV[2]) "+
                "for i, elem in ipairs(elements) do "+
                " redis.call('ZREM', KEYS[1], elem) "+
                "end "+
                "return elements ";

        return redisTemplate.execute(
                new DefaultRedisScript<List>(script, List.class),
                Collections.singletonList(key),  // KEYS[1]
                field1,                          // ARGV[1]
                field2                           // ARGV[2]
        );
    }
}
