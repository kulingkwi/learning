package org.kulingkwi.learning;

import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.config.Configuration;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.xml.XmlConfiguration;

public class ehcache {

    public static void main(String[] args) {
        CacheManager cacheManager = CacheManagerBuilder.newCacheManagerBuilder()
                .withCache("preConfigured",
                        CacheConfigurationBuilder.newCacheConfigurationBuilder(Long.class, String.class,
                                ResourcePoolsBuilder.heap(100))
                                .build())
                .build(true);

        Cache<Long, String> preConfigured = cacheManager.getCache("preConfigured", Long.class, String.class);

        Cache<Long, String> myCache = cacheManager.createCache("myCache",
                CacheConfigurationBuilder.newCacheConfigurationBuilder(Long.class, String.class,
                        ResourcePoolsBuilder.heap(100)).build());

        myCache.put(1L, "da one!");
        String value = myCache.get(1L);

        System.out.println(value);
        cacheManager.close();

        Configuration configuration = new XmlConfiguration(Class.class.getResource("/ehcache.xml"));
        CacheManager cm = CacheManagerBuilder.newCacheManager(configuration);
        cm.init();
        Cache<String, String> cache = cm.getCache("simpleCache", String.class, String.class);
        cache.put("test", "test value");
        String test = cache.get("test");
        System.out.println(test);
        cm.close();

    }

}
