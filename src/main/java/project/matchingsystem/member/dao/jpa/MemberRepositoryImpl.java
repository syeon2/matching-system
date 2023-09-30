package project.matchingsystem.member.dao.jpa;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import project.matchingsystem.member.dao.common.MemberCustomRepository;
import project.matchingsystem.member.domain.MemberStatus;
import project.matchingsystem.member.domain.QMember;

@Repository
@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberCustomRepository {

	private final JPAQueryFactory jpaQueryFactory;
	private final QMember member = QMember.member;

	@Transactional
	@Modifying(clearAutomatically = true)
	@Override
	public Long updateMemberStatus(Long memberId, String status) {
		return jpaQueryFactory.update(member)
			.set(member.memberStatus, MemberStatus.getStatus(status))
			.where(member.id.eq(memberId))
			.execute();
	}
}
