package project.matchingsystem.member.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.matchingsystem.member.service.request.MemberCreateServiceRequest;
import project.matchingsystem.util.wrapper.BaseEntity;

@Getter
@Entity
@Table(name = "member")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {

	@Id
	@GeneratedValue
	@Column(name = "member_id")
	private Long id;

	@Column(columnDefinition = "varchar(40)")
	private String name;

	@Column(columnDefinition = "varchar(40)")
	private String nickname;

	@Enumerated(EnumType.STRING)
	private MemberStatus memberStatus;

	@Builder
	private Member(String name, String nickname, MemberStatus memberStatus) {
		this.name = name;
		this.nickname = nickname;
		this.memberStatus = memberStatus;
	}

	public static Member toEntity(MemberCreateServiceRequest request) {
		return Member.builder()
			.name(request.getName())
			.nickname(request.getNickname())
			.memberStatus(MemberStatus.INACTIVE)
			.build();
	}
}
