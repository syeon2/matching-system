package project.matchingsystem.member.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.geo.Circle;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Metrics;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import project.matchingsystem.member.web.request.GeoLocation;

@Service
public class MemberMatchingService {

	private static final String KEY = "GEO_LOCATION";

	private final RedisTemplate<String, Object> redisTemplate;
	private final RedisTemplate<String, String> redisRoutingTableTemplate;
	private final RedisTemplate<String, String> redisDistributedLockTemplate;

	public MemberMatchingService(
		RedisTemplate<String, Object> redisTemplate,
		@Qualifier(value = "redisRoutingTableTemplate") RedisTemplate<String, String> redisRoutingTableTemplate,
		@Qualifier(value = "redisDistributedLockTemplate") RedisTemplate<String, String> redisDistributedLockTemplate) {
		this.redisTemplate = redisTemplate;
		this.redisRoutingTableTemplate = redisRoutingTableTemplate;
		this.redisDistributedLockTemplate = redisDistributedLockTemplate;
	}

	// 매칭 시도 API

	/**
	 * 1. 현재 매칭할 회원 아이디, 위치 정보 받기
	 * 2. 현재 회원의 위치를 기준으로 반경 xkm에 있는 회원 위치 조회
	 * 3. 매칭될 회원이 다수 존재하면 distributed lock을 통해 이미 매칭된 회원인지 확인
	 * -> 이미 distributed lock에 등록된 회원이라면 다른 회원 조회
	 * -> distributed lock에 회원이 없다면 회원을 distributed lock에 등록 후 다음 절차
	 * 4-1. 만약 조회할 회원이 없거나 모두 distributed lock에 있는 회원이라면 매칭될 회원이 없다는 Response 보내기
	 * 4-2. 3번에서 특정된 회원이 존재해서 distributed lock에 등록되었다면 routing table에 key(매칭한 회원 ID) : value(매칭된 회원 ID)로 등록
	 * 5. 매칭 성공 결과 Response 보내기
	 */

	public Long matchingMember(GeoLocation geoLocation) {
		List<Long> memberIds = searchMembersInRadius(geoLocation);

		Long l = dispatchMember(memberIds);

		if (l == 0) {
			return 0L;
		}

		redisRoutingTableTemplate.opsForValue().set(geoLocation.getMemberId().toString(), l.toString());

		return l;
	}

	private Long dispatchMember(List<Long> memberIds) {
		for (Long memberId : memberIds) {

			Boolean check = redisDistributedLockTemplate.opsForValue()
				.setIfAbsent(memberId.toString(), memberId.toString());

			if (Boolean.TRUE.equals(check)) {
				return memberId;
			}
		}

		throw new RuntimeException("매칭된 회원이 존재하지 않습니다.");
	}

	private List<Long> searchMembersInRadius(GeoLocation geoLocation) {
		Long memberId = geoLocation.getMemberId();

		Point point = new Point(geoLocation.getLongitude(), geoLocation.getLatitude());
		Distance distance = new Distance(5, Metrics.KILOMETERS);
		Circle circle = new Circle(point, distance);

		return redisTemplate.opsForGeo()
			.radius(KEY, circle)
			.getContent().stream()
			.map(result -> Long.parseLong(result.getContent().getName().toString()))
			.collect(Collectors.toList());
	}

	// 매칭 이후 Polling 기법으로 지속적인 회원 위치 조회 API

	/**
	 * 1. 매칭 시도 성공 이후 요청되어야할 API
	 * 2. 현재 요청하는 회원 아이디를 받아 routing table에 어떤 회원과 매칭되었는지 조회
	 * 3. Routing table에서 매칭된 회원을 조회하면, 조회한 ID를 통해 GEO Redis에서 해당 회원 위치 조회
	 * -> 만약 Routing Table에 key:value가 존재하지 않는다면 에러 API 반환
	 * 4. 3에서 조회한 위치를 회원에게 Response로 반환
	 */
	public GeoLocation pollingMemberPosition(Long memberId) {
		Long matchedMember = Long.parseLong(redisRoutingTableTemplate.opsForValue().get(memberId.toString()));

		Point point = redisTemplate.opsForGeo().position(KEY, matchedMember).get(0);

		return GeoLocation.toGeoLocation(point);
	}

	/**
	 * 매칭 이후 위치 정보 조회에 필요했떤 distributed lock, routing table에 등록된 정보 삭제
	 */
	public void disconnectMember(Long memberId) {
		Long deletedMemberId = Long.parseLong(
			redisRoutingTableTemplate.opsForValue().getAndDelete(memberId.toString()));
		redisDistributedLockTemplate.opsForValue().getAndDelete(deletedMemberId.toString());
	}
}
