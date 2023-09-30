package project.matchingsystem.member.web;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import project.matchingsystem.member.service.MemberService;
import project.matchingsystem.member.web.request.MemberCreateRequest;
import project.matchingsystem.member.web.request.MemberUpdateStatusRequest;
import project.matchingsystem.util.wrapper.ApiResult;

@RestController
@RequiredArgsConstructor
public class MemberController {

	private final MemberService memberService;

	@PostMapping("/api/v1/member")
	public ApiResult<Long> joinMember(@Valid @RequestBody MemberCreateRequest request) {
		Long joinedMemberId = memberService.join(request.toServiceRequest());

		return ApiResult.onSuccess(joinedMemberId);
	}

	@PostMapping("/api/v1/member/{memberId}")
	public ApiResult<Long> updateStatusMember(
		@PathVariable Long memberId,
		@Valid @RequestBody MemberUpdateStatusRequest request
	) {
		Long updatedMemberId = memberService.updateStatus(memberId, request.getStatus());

		return ApiResult.onSuccess(updatedMemberId);
	}

	@DeleteMapping("/api/v1/member/{memberId}")
	public ApiResult<String> deleteMember(@PathVariable Long memberId) {
		memberService.deleteMember(memberId);

		return ApiResult.onSuccess("회원 정보가 성공적으로 삭제되었습니다.");
	}
}
