package cn.qblank.service;

import cn.qblank.beans.CacheKeyConstants;
import cn.qblank.util.JsonMapper;
import com.google.common.base.Joiner;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import redis.clients.jedis.ShardedJedis;

import javax.annotation.Resource;

/**
 * @author evan_qb
 * @date 2018/9/11
 */
@Service
@Slf4j
public class SysCacheService {
    @Resource(name = "redisPool")
    private RedisPool redisPool;

    /**
     * 调用缓存
     * @param toSaveValue
     * @param timeoutSeconds
     * @param prefix
     */
    public void saveCache(String toSaveValue, int timeoutSeconds, CacheKeyConstants prefix){
        saveCache(toSaveValue,timeoutSeconds,prefix,null);
    }

    /**
     * 进行缓存
     * @param toSaveValue
     * @param timeoutSeconds
     * @param prefix
     * @param keys
     */
    public void saveCache(String toSaveValue, int timeoutSeconds, CacheKeyConstants prefix,String... keys){
        if (toSaveValue == null){
            return;
        }
        ShardedJedis shardedJedis = null;
        try {
            String cacheKey = generateCacheKey(prefix,keys);
            shardedJedis = redisPool.instance();
            shardedJedis.setex(cacheKey,timeoutSeconds,toSaveValue);
        }catch (Exception e){
            log.error("save cache exception,prefix:{},keys:{}",prefix.name(),JsonMapper.object2String(keys),e);
        }finally {
            redisPool.safeClose(shardedJedis);
        }
    }

    /**
     * 获取缓存
     * @param prefix
     * @param keys
     * @return
     */
    public String getFromCache(CacheKeyConstants prefix,String... keys){
        ShardedJedis shardedJedis = null;
        String cacheKey = generateCacheKey(prefix,keys);
        try {
            shardedJedis = redisPool.instance();
            String value = shardedJedis.get(cacheKey);
            return value;
        }catch (Exception e){
            log.error("get from cache exception,prefix:{},keys:{}",prefix.name(),JsonMapper.object2String(keys),e);
            return null;
        }finally {
            redisPool.safeClose(shardedJedis);
        }
    }


    /**
     * 生成缓存的key
     * @param prefix
     * @param keys
     * @return
     */
    private String generateCacheKey(CacheKeyConstants prefix,String... keys){
        String key = prefix.name();
        if (keys != null && keys.length > 0){
            key += "_" + Joiner.on("_").join(keys);
        }
        return key;
    }
}
