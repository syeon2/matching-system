package project.matchingsystem.member.web;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import project.matchingsystem.member.service.MemberMatchingService;
import project.matchingsystem.member.web.request.GeoLocation;
import project.matchingsystem.util.wrapper.ApiResult;

@RestController
@RequiredArgsConstructor
public class MemberMatchingController {

	private final MemberMatchingService memberMatchingService;

	/**
	 * 현재 회원 위치에서 반경 5Km에 있는 회원들 중 한명과 matching 하는 API
	 */
	@PostMapping("/api/v1/init-match")
	public ApiResult<Long> initMatchingMember(@RequestBody GeoLocation geoLocation) {
		Long l = memberMatchingService.matchingMember(geoLocation);

		return ApiResult.onSuccess(l);
	}

	/**
	 * 매칭한 회원이 Polling 방식을 통해 상대발 위치를 지속적으로 호출하는 API
	 */
	@GetMapping("/api/v1/geo/{memberId}")
	public ApiResult<GeoLocation> searchMemberPosition(@PathVariable Long memberId) {
		GeoLocation geoLocation = memberMatchingService.pollingMemberPosition(memberId);

		return ApiResult.onSuccess(geoLocation);
	}

	/**
	 * 매칭 연결을 해제하는 API
	 */
	@DeleteMapping("/api/v1/match/{memberId}")
	public void disconnectMember(@PathVariable Long memberId) {
		memberMatchingService.disconnectMember(memberId);
	}
}
