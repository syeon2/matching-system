package project.matchingsystem.member.web.request;

import javax.validation.constraints.NotNull;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberUpdateStatusRequest {

	@NotNull(message = "상태 값은 필수 값입니다.")
	private String status;

	public MemberUpdateStatusRequest(String status) {
		this.status = status;
	}
}
