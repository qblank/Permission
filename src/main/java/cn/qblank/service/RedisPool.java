package cn.qblank.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

import javax.annotation.Resource;

/**
 * @author evan_qb
 * @date 2018/9/11
 */
@Service("redisPool")
@Slf4j
public class RedisPool {
    @Resource(name = "shardedJedisPool")
    private ShardedJedisPool shardedJedisPool;

    /**
     * 创建实例
     * @return
     */
    public ShardedJedis instance(){
        return shardedJedisPool.getResource();
    }

    /**
     * 安全关闭
     * @param shardedJedis
     */
    public void safeClose(ShardedJedis shardedJedis){
        try {
            if (shardedJedis != null){
                shardedJedis.close();
            }
        }catch (Exception e){
            log.error("return redis resource exception",e);
        }
    }

}
