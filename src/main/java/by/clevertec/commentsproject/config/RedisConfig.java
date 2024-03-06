package by.clevertec.commentsproject.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;

/**
 * Конфигурация Redis для профиля "prod".
 */
@Configuration
@Profile("prod")
public class RedisConfig {

    /**
     * Создает менеджер кэша Redis.
     *
     * @param redisConnectionFactory фабрика подключений Redis
     * @return менеджер кэша Redis
     */
    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
        return RedisCacheManager.create(redisConnectionFactory);
    }
}
