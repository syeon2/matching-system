package project.matchingsystem.member.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import project.matchingsystem.member.dao.common.MemberCustomRepository;
import project.matchingsystem.member.domain.Member;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberCustomRepository {

}
