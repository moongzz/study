package com.example.demo.jpa_lifecycle;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


@Component
public class JpaLifecycleExample implements CommandLineRunner {

    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional
    public void run(String... args) {
        // 1. Transient (비영속)
        Member member = new Member("짱구");
        System.out.println("1. Transient 상태: " + member.getId() + ", " + member.getName());

        // 2. Persistent (영속)
        em.persist(member);
        System.out.println("2. Persistent 상태: " + member.getId() + ", " + member.getName());

        // 3. Flush 시점에 DB 반영
        em.flush(); // SQL INSERT 실행
        System.out.println("3. DB에 반영됨");

        // 3-1. Dirty Checking
        member.setName("철수");
        System.out.println("3-1. Dirty Checking 준비됨");

        // flush() 또는 트랜잭션 커밋 시점에 UPDATE 쿼리 실행
        em.flush();
        System.out.println("3-2. Dirty Checking 반영됨: " + em.find(Member.class, member.getId()).getName());

        // 4. Detach (준영속)
        em.detach(member);
//        em.close();
//        em.clear();
        System.out.println("4. Detached 상태");

        // 변경해도 DB 반영 안 됨
        member.setName("맹구");
        em.flush();
        System.out.println("변경 이름: 맹구, DB 이름: " + em.find(Member.class, member.getId()).getName());

        // 5. 다시 영속 상태
        em.merge(member);
        System.out.println("5. merge로 다시 Persistent 상태");

        // 6. Remove (삭제)
        Member findMember = em.find(Member.class, member.getId());
        em.remove(findMember);
        System.out.println("6. Removed 상태");
    }
}
