package project.matchingsystem.member.service.request;

import lombok.Builder;
import lombok.Getter;

@Getter
public class MemberCreateServiceRequest {

	private final String name;
	private final String nickname;

	@Builder
	private MemberCreateServiceRequest(String name, String nickname) {
		this.name = name;
		this.nickname = nickname;
	}
}
