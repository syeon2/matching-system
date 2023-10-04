package project.matchingsystem.member.web;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import project.matchingsystem.member.service.MemberGeoService;
import project.matchingsystem.member.web.request.GeoLocation;
import project.matchingsystem.util.wrapper.ApiResult;

@RestController
@RequiredArgsConstructor
public class MemberGeoController {

	private final MemberGeoService memberGeoService;

	/**
	 * WebSocket Connection 이후 지속적으로 위치 데이터 전송 API
	 */
	@MessageMapping("/location")
	public void reflectPositionOfMember(@Payload GeoLocation geoLocation) {
		memberGeoService.reflectPosition(geoLocation.getMemberId(), geoLocation);
	}

	/**
	 * WebSocket Connection close 이후 Redis에 해당 member_id의 위치 삭제 API
	 */
	@DeleteMapping("/api/v1/location/{memberId}")
	public void deletePositionOfMember(@PathVariable Long memberId) {
		memberGeoService.deletePositionOfMember(memberId);
	}

	/**
	 * member_id에 해당하는 위치 데이터 조회 API
	 */
	@GetMapping("/api/v1/location/{memberId}")
	public ApiResult<GeoLocation> lookupPositionOfMember(@PathVariable Long memberId) {
		GeoLocation positionOfMember = memberGeoService.getPositionOfMember(memberId);

		return ApiResult.onSuccess(positionOfMember);
	}
}
