package project.matchingsystem.member.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;

@Getter
@Entity
@Table(name = "member")
public class Member {

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
}
