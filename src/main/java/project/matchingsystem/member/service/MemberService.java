package project.matchingsystem.member.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import project.matchingsystem.member.dao.MemberRepository;
import project.matchingsystem.member.domain.Member;
import project.matchingsystem.member.service.request.MemberCreateServiceRequest;

@Service
@RequiredArgsConstructor
public class MemberService {

	private final MemberRepository memberRepository;

	public Long join(MemberCreateServiceRequest request) {
		Member savedMember = memberRepository.save(Member.toEntity(request));

		return savedMember.getId();
	}

	public Long updateStatus(Long memberId, String status) {
		memberRepository.updateMemberStatus(memberId, status);

		return memberId;
	}

	public void deleteMember(Long memberId) {
		memberRepository.deleteById(memberId);
	}
}
