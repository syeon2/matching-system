package project.matchingsystem.member.web.request;

import javax.validation.constraints.NotNull;

import lombok.Builder;
import lombok.Getter;
import project.matchingsystem.member.service.request.MemberCreateServiceRequest;

@Getter
public class MemberCreateRequest {

	@NotNull(message = "이름은 필수 값입니다.")
	private final String name;

	@NotNull(message = "닉네임은 필수 값입니다.")
	private final String nickname;

	@Builder
	private MemberCreateRequest(String name, String nickname) {
		this.name = name;
		this.nickname = nickname;
	}

	public MemberCreateServiceRequest toServiceRequest() {
		return MemberCreateServiceRequest.builder()
			.name(this.name)
			.nickname(this.nickname)
			.build();
	}
}
