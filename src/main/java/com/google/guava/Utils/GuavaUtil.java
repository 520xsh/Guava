package com.google.guava.Utils;

import com.google.common.cache.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;


/**
 * @author 许书豪
 */
public class GuavaUtil {
    private static Logger logger = LoggerFactory.getLogger(GuavaUtil.class);

    private static RemovalListener<Object, Object> removalListener = new RemovalListener<Object, Object>() {
        @Override
        public void onRemoval(RemovalNotification<Object, Object> removal) {
          logger.info(" [{}]被移除原因是 [{}] ",removal.getKey(),removal.getCause());
        }
    };

    /**
     *   .initialCapacity(100)//设置缓存的初始容量为100
     *   .maximumSize(1000)//设置缓存项的数目不超过固定值，设置这个缓存会尝试回收不常用的缓存
     *   .expireAfterAccess(1, TimeUnit.HOURS)//缓存项在给定时间内没有被读/写访问，则回收
     *    .expireAfterWrite(30,TimeUnit.MINUTES)//缓存项在给定时间内没有被写访问（创建或覆盖），则回收。
     *    guava不允许返回为空，否则会有如下异常
     *    com.google.common.cache.CacheLoader$InvalidCacheLoadException: CacheLoader returned null for key name.
     *    解决办法 (1)在load（）方法中设置 return "null"
     *             (2)在getKey()方法中捕捉该异常 并返回null
     */
    public static LoadingCache<Object,Object> localCache = CacheBuilder.newBuilder()
            .initialCapacity(2)
            .maximumSize(3)
            .expireAfterAccess(1, TimeUnit.MINUTES)
            .expireAfterWrite(1,TimeUnit.MINUTES)
            .removalListener(removalListener)
            .build(new CacheLoader<Object, Object>() {
                @Override
                public Object load(Object o) throws Exception {
                    return null;
                }
        });
    public static void setKey(Object key,Object value){
        localCache.put(key,value);
        logger.info("[{}]缓存成功",key);
    }
    public static Object getKey(Object key,Object defultValue){
        try {
            return localCache.get(key);
        } catch (ExecutionException e) {
            logger.info("获取缓存异常",e);
        }catch (CacheLoader.InvalidCacheLoadException e){
            logger.info("没有从缓存中获取到数据，返回为null----");
        }
        return defultValue;
    }
}
