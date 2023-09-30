package project.matchingsystem.member.dao.common;

public interface MemberCustomRepository {

	Long updateMemberStatus(Long memberId, String status);
}
