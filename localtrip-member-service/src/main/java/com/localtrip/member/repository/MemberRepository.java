package com.localtrip.member.repository;

import com.localtrip.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 회원 Repository
 */
@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    
    /**
     * 아이디로 회원 조회
     */
    Optional<Member> findByMemberId(String memberId);
    
    /**
     * 이메일로 회원 조회
     */
    Optional<Member> findByEmail(String email);
    
    /**
     * 아이디 존재 여부 확인
     */
    boolean existsByMemberId(String memberId);
    
    /**
     * 이메일 존재 여부 확인
     */
    boolean existsByEmail(String email);
}
