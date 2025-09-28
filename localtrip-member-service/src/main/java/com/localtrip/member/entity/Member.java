package com.localtrip.member.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

/**
 * 회원 엔티티
 */
@Entity
@Table(name = "member")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    
    @Column(name = "member_id", length = 20, nullable = false, unique = true)
    private String memberId;
    
    @Column(name = "password", nullable = false)
    private String password;
    
    @Column(name = "email", nullable = false, unique = true)
    private String email;
    
    @Column(name = "member_type", nullable = false)
    private int memberType = 1; // 1: 일반회원
    
    @CreationTimestamp
    @Column(name = "member_created_at", nullable = false, updatable = false)
    private LocalDateTime memberCreatedAt;
    
    @Builder
    public Member(String memberId, String password, String email, int memberType) {
        this.memberId = memberId;
        this.password = password;
        this.email = email;
        this.memberType = memberType;
    }
    
    /**
     * 비밀번호 업데이트
     */
    public void updatePassword(String encodedPassword) {
        this.password = encodedPassword;
    }
    
    /**
     * 이메일 업데이트
     */
    public void updateEmail(String email) {
        this.email = email;
    }
    
    /**
     * 회원 타입 업데이트
     */
    public void updateMemberType(int memberType) {
        this.memberType = memberType;
    }
}
