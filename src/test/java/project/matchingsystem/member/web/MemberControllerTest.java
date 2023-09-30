package project.matchingsystem.member.web;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import project.matchingsystem.BaseTestConfig;
import project.matchingsystem.member.service.MemberService;
import project.matchingsystem.member.service.request.MemberCreateServiceRequest;
import project.matchingsystem.member.web.request.MemberCreateRequest;
import project.matchingsystem.member.web.request.MemberUpdateStatusRequest;

@WebMvcTest(controllers = MemberController.class)
class MemberControllerTest extends BaseTestConfig {

	@MockBean
	private MemberService memberService;

	@Test
	@DisplayName(value = "새로운 회원 등록하는 API 호출입니다.")
	void joinMember() throws Exception {
		// given
		long memberId = 1L;
		MemberCreateRequest request = MemberCreateRequest.builder()
			.name("memberA")
			.nickname("nicknameA")
			.build();

		given(memberService.join(any(MemberCreateServiceRequest.class))).willReturn(memberId);

		// when  // then
		mockMvc.perform(
				post("/api/v1/member")
					.content(objectMapper.writeValueAsString(request))
					.contentType(MediaType.APPLICATION_JSON)
			)
			.andDo(print())
			.andExpect(status().isOk());
	}

	@Test
	@DisplayName(value = "회원 정보를 수정하는 API를 호출합니다.")
	public void updateMember() throws Exception {
		// given
		long memberId = 1L;
		String status = "active";
		MemberUpdateStatusRequest request = new MemberUpdateStatusRequest(status);

		when(memberService.updateStatus(memberId, status)).thenReturn(memberId);

		// when  // then
		mockMvc.perform(
				post("/api/v1/member/{memberId}", memberId)
					.content(objectMapper.writeValueAsString(request))
					.contentType(MediaType.APPLICATION_JSON)
			)
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.data").value(memberId));
	}

	@Test
	@DisplayName(value = "회원을 삭제하는 API를 호출합니다.")
	void deleteMember() throws Exception {
		// given
		long memberId = 1L;

		// when  // then
		mockMvc.perform(
				delete("/api/v1/member/{memberId}", memberId)
			)
			.andDo(print())
			.andExpect(jsonPath("$.data").value("회원 정보가 성공적으로 삭제되었습니다."));
	}
}
