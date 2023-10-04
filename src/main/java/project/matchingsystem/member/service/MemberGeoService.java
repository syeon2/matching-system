package project.matchingsystem.member.service;

import java.util.List;

import org.springframework.data.geo.Point;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import project.matchingsystem.member.web.request.GeoLocation;

@Service
public class MemberGeoService {

	private static final String KEY = "GEO_LOCATION";

	private final RedisTemplate<String, Object> redisGeoTemplate;

	private MemberGeoService(RedisTemplate<String, Object> redisGeoTemplate) {
		this.redisGeoTemplate = redisGeoTemplate;
	}

	public void reflectPosition(Long memberId, GeoLocation geoLocation) {
		saveMemberPositionToRedis(memberId, geoLocation.toPoint());
	}

	public void deletePositionOfMember(Long memberId) {
		redisGeoTemplate.opsForGeo().remove(KEY, memberId);
	}

	public GeoLocation getPositionOfMember(Long memberId) {
		List<Point> position = redisGeoTemplate.opsForGeo().position(KEY, memberId);

		if (!position.isEmpty() && position.get(0) == null) {
			throw new IllegalArgumentException("잘못된 접근입니다.");
		}

		return GeoLocation.toGeoLocation(position.get(0));
	}

	private void saveMemberPositionToRedis(Long memberId, Point point) {
		redisGeoTemplate.opsForGeo().add(KEY, point, memberId);
	}
}
