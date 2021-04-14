package org.example.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.connection.ReturnType;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.types.Expiration;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author Vick C
 * @Description: 分布式锁工具类
 * @CreateDate: 2020/05/29
 */
@Component
@Slf4j
public class RedisLock {

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 释放锁脚本，原子操作，lua脚本
     */
    private static final String UNLOCK_LUA;
    /**
     * 默认过期时间(30ms)
     */
    private static final long DEFAULT_EXPIRE = 30L;

    static {
        StringBuilder sb = new StringBuilder();
        sb.append("if redis.call(\"get\",KEYS[1]) == ARGV[1] ");
        sb.append("then ");
        sb.append("    return redis.call(\"del\",KEYS[1]) ");
        sb.append("else ");
        sb.append("    return 0 ");
        sb.append("end ");
        UNLOCK_LUA = sb.toString();
    }

    /**
     * 获取分布式锁，原子操作
     * @param lockKey   锁
     * @param lockValue 唯一ID, 可以使用UUID.randomUUID().toString();
     * @return 是否枷锁成功
     */
    public boolean lock(String lockKey, String lockValue) {
        return this.lock(lockKey, lockValue, DEFAULT_EXPIRE, TimeUnit.MILLISECONDS);
    }

    /**
     * 获取分布式锁，原子操作
     * @param lockKey   锁
     * @param lockValue 唯一ID, 可以使用UUID.randomUUID().toString();
     * @param expire    过期时间
     * @param timeUnit  时间单位
     * @return 是否枷锁成功
     */
    public boolean lock(String lockKey, String lockValue, long expire, TimeUnit timeUnit) {
        try {
            Boolean result = redisTemplate.opsForValue().setIfAbsent(lockKey,lockValue,expire,timeUnit);

            return result;
        } catch (Exception e) {
            log.error("redis lock error ,lock key: {}, value : {}, error info : {}", lockKey, lockValue, e);
        }
        return false;
    }

    /**
     * 释放锁
     * @param lockKey   锁
     * @param lockValue 唯一ID
     * @return 执行结果
     */
    public Long unlock(String lockKey, String lockValue) {
        /** 释放锁lua脚本 */
        final String RELEASE_LOCK_LUA_SCRIPT = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";

        // 指定 lua 脚本，并且指定返回值类型
        DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>(RELEASE_LOCK_LUA_SCRIPT,Long.class);
        // 参数一：redisScript，参数二：key列表，参数三：arg（可多个）
        Long result = (Long) redisTemplate.execute(redisScript, Collections.singletonList(lockKey),lockValue);
        System.out.println(result);

        return result;
    }

    /**
     * 获取Redis锁的value值
     * @param lockKey 锁
     */
    public String get(String lockKey) {
        try {
            Object result = redisTemplate.opsForValue().get(lockKey);
            if(result==null){
                return null ;
            }
            else {
                return (String)result ;
            }

        } catch (Exception e) {
            log.error("get redis value occurred an exception,the key is {}, error is {}", lockKey, e);
            return null ;
        }

    }

}
