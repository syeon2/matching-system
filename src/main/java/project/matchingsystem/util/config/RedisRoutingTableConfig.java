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
public class RedisRoutingTableConfig {

	@Value("${spring.redis.routing-table.host}")
	private String host;

	@Value("${spring.redis.routing-table.port}")
	private Integer port;

	@Bean
	public RedisConnectionFactory redisRoutingTableConnectionFactory() {
		return new LettuceConnectionFactory(host, port);
	}

	@Bean
	@Qualifier(value = "redisRoutingTableTemplate")
	public RedisTemplate<String, String> redisRoutingTableTemplate() {
		RedisTemplate<String, String> redisTemplate = new StringRedisTemplate();
		redisTemplate.setConnectionFactory(redisRoutingTableConnectionFactory());

		return redisTemplate;
	}
}
