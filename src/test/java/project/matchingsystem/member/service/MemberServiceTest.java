package project.matchingsystem.member.service;

import static org.assertj.core.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import project.matchingsystem.member.dao.MemberRepository;
import project.matchingsystem.member.domain.Member;
import project.matchingsystem.member.domain.MemberStatus;
import project.matchingsystem.member.service.request.MemberCreateServiceRequest;

@SpringBootTest
class MemberServiceTest {

	@Autowired
	private MemberService memberService;

	@Autowired
	private MemberRepository memberRepository;

	@Test
	@DisplayName(value = "신규 회원을 생성합니다.")
	void join() {
		// given
		String name = "memberA";
		MemberCreateServiceRequest request = MemberCreateServiceRequest.builder()
			.name(name)
			.nickname("nicknameA")
			.build();

		// when
		Long joinedMemberId = memberService.join(request);

		// then
		Optional<Member> findMemberOptional = memberRepository.findById(joinedMemberId);

		findMemberOptional.ifPresentOrElse(
			member -> assertThat(member.getName()).isEqualTo(name),
			() -> fail("회원이 저장되지 없습니다.")
		);
	}

	@Test
	@DisplayName(value = "회원 상태를 변경합니다. - ACTIVE")
	void changeStatusToActive() {
		// given
		String name = "memberA";
		MemberCreateServiceRequest request = MemberCreateServiceRequest.builder()
			.name(name)
			.nickname("nicknameA")
			.build();
		Long joinedMemberId = memberService.join(request);

		// when
		Long memberId = memberService.updateStatus(joinedMemberId, "ACTIVE");

		// then
		Optional<Member> findMemberOptional = memberRepository.findById(joinedMemberId);

		findMemberOptional.ifPresentOrElse(
			member -> assertThat(member.getMemberStatus()).isEqualTo(MemberStatus.ACTIVE),
			() -> fail("상태가 변경되지 않았습니다.")
		);
	}

	@Test
	@DisplayName(value = "회원 상태를 변경합니다. - INACTIVE")
	void changeStatusToInactive() {
		// given
		String name = "memberA";
		MemberCreateServiceRequest request = MemberCreateServiceRequest.builder()
			.name(name)
			.nickname("nicknameA")
			.build();
		Long joinedMemberId = memberService.join(request);

		// when
		Long memberId = memberService.updateStatus(joinedMemberId, "INACTIVE");

		// then
		Optional<Member> findMemberOptional = memberRepository.findById(joinedMemberId);

		findMemberOptional.ifPresentOrElse(
			member -> assertThat(member.getMemberStatus()).isEqualTo(MemberStatus.INACTIVE),
			() -> fail("상태가 변경되지 않았습니다.")
		);
	}

	@Test
	@DisplayName(value = "회원을 삭제합니다.")
	void delete() {
		// given
		String name = "memberA";
		MemberCreateServiceRequest request = MemberCreateServiceRequest.builder()
			.name(name)
			.nickname("nicknameA")
			.build();
		Long joinedMemberId = memberService.join(request);

		// when
		memberService.deleteMember(joinedMemberId);

		// then
		Optional<Member> findMemberOptional = memberRepository.findById(joinedMemberId);

		assertThat(findMemberOptional).isEmpty();
	}
}
