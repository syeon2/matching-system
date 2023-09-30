package project.matchingsystem.member.domain;

public enum MemberStatus {
	ACTIVE, INACTIVE;

	public static MemberStatus getStatus(String status) {
		if (status.equals(ACTIVE.toString())) {
			return ACTIVE;
		} else if (status.equals(INACTIVE.toString())) {
			return INACTIVE;
		}

		throw new IllegalArgumentException("유효하지 않은 상태 값입니다.");
	}
}
