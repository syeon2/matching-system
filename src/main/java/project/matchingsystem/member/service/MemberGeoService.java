package project.matchingsystem.member.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import project.matchingsystem.member.web.request.GeoLocation;

@Service
public class MemberGeoService {

	private static final String KEY = "GEO_LOCATION";

	private final RedisTemplate<String, Object> redisTemplate;

	private MemberGeoService(@Qualifier("geoRedisTemplate") RedisTemplate<String, Object> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	public void reflectPosition(Long memberId, GeoLocation geoLocation) {
		saveMemberPositionToRedis(memberId, geoLocation.toPoint());
	}

	public void deletePositionOfMember(Long memberId) {
		redisTemplate.opsForGeo().remove(KEY, memberId);
	}

	public GeoLocation getPositionOfMember(Long memberId) {
		List<Point> position = redisTemplate.opsForGeo().position(KEY, memberId);

		if (!position.isEmpty() && position.get(0) == null) {
			throw new IllegalArgumentException("잘못된 접근입니다.");
		}

		return GeoLocation.toGeoLocation(position.get(0));
	}

	private void saveMemberPositionToRedis(Long memberId, Point point) {
		redisTemplate.opsForGeo().add(KEY, point, memberId);
	}
}
