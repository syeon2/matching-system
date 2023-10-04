package project.matchingsystem.util.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

@Configuration
public class RedisDistributedLockConfig {

	@Value("${spring.redis.distributed-lock.host}")
	private String host;

	@Value("${spring.redis.distributed-lock.port}")
	private Integer port;

	@Bean
	public RedisConnectionFactory redisDistributedLockConnectionFactory() {
		return new LettuceConnectionFactory(host, port);
	}

	@Bean
	@Qualifier(value = "redisDistributedLockTemplate")
	public RedisTemplate<String, String> redisDistributedLockTemplate() {
		RedisTemplate<String, String> redisTemplate = new StringRedisTemplate();
		redisTemplate.setConnectionFactory(redisDistributedLockConnectionFactory());

		return redisTemplate;
	}
}
