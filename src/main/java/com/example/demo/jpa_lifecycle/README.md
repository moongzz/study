# JPA Entity Lifecycle & Dirty Checking 예제

## 1️⃣ 엔티티 상태

| 상태 | 설명 |
|------|------|
| **비영속(Transient)** | 영속성 컨텍스트와 관련 없는 상태 |
| **영속(Persistent)** | 엔티티가 영속성 컨텍스트에 저장되어 관리되는 상태 |
| **준영속(Detached)** | 영속성 컨텍스트에서 분리된 상태 |
| **삭제(Removed)** | 엔티티가 영속성 컨텍스트와 DB에서 삭제된 상태 |
| **병합(Merge)** | 준영속 상태의 엔티티를 다시 영속 상태로 변환 |

---
## 2️⃣ 예제 코드
~~~ java
Member member = new Member("짱구");
System.out.println("1. Transient 상태: " + member.getId() + ", " + member.getName());

em.persist(member);
System.out.println("2. Persistent 상태: " + member.getId() + ", " + member.getName());

em.flush(); // SQL INSERT 실행
System.out.println("3. DB에 반영됨");

em.detach(member);
System.out.println("4. Detached 상태");

// 변경해도 DB 반영 안 됨
member.setName("맹구");

em.merge(member);
System.out.println("5. merge로 다시 Persistent 상태");

Member findMember = em.find(Member.class, member.getId());
em.remove(findMember);
System.out.println("6. Removed 상태");
~~~
---
## 3️⃣ 실행 결과
~~~
1. Transient 상태: null, 짱구
Hibernate: 
    insert 
    into
        member
        (name, id) 
    values
        (?, default)
2. Persistent 상태: 1, 짱구
3. DB에 반영됨
4. Detached 상태
Hibernate: 
    select
        m1_0.id,
        m1_0.name 
    from
        member m1_0 
    where
        m1_0.id=?
5. merge로 다시 Persistent 상태
6. Removed 상태
Hibernate: 
    delete 
    from
        member 
    where
        id=?
~~~

### ⚠️ 의도치 않은 동작 발생

`flush()` 시점이 아닌 `em.persist(member)` 직후 INSERT 쿼리가 실행됨
- 원인: ID 생성 전략이 IDENTITY이기 때문
  - JPA는 PK 값을 DB에서 자동 증가로 받아야 함
  - `persist(member)` 호출 시점에서 PK를 알 수 없으므로 Hibernate가 즉시 INSERT를 날려 DB에서 키를 가져오도록 설계됨
- 해결/대안: SEQUENCE 또는 AUTO 전략을 쓰면 `flush()` 시점까지 INSERT를 미룸

## 4️⃣ Dirty Checking
- 영속 상태의 엔티티가 변경되면 트랜잭션 커밋 또는 `flush()` 시점에 DB에 자동 반영
- 조건:
  1. 영속성 컨텍스트에서 관리되는 엔티티여야 함
  2. 트랜잭션 내에서 변경이 이루어져야 함
- 준영속/비영속 상태에서는 Dirty Checking 적용 X

### Dirty Checking 예제
~~~ java
member.setName("철수"); // 영속 상태에서 변경
System.out.println("3-1. Dirty Checking 준비됨");
em.flush();             // UPDATE 쿼리 실행
System.out.println("3-2. Dirty Checking 반영됨: " + em.find(Member.class, member.getId()).getName());

em.detach(member);      // 준영속 상태로 변경
System.out.println("4. Detached 상태");

member.setName("맹구"); // 변경해도 DB 반영 안 됨
em.flush();
System.out.println("변경 이름: 맹구, DB 이름: " + em.find(Member.class, member.getId()).getName());
~~~

### 실행 결과
~~~
3-1. Dirty Checking 준비됨
Hibernate: update member set name=? where id=?
3-2. Dirty Checking 반영됨: 철수
4. Detached 상태
변경 이름: 맹구, 저장된 이름: 철수
~~~
- 영속 상태에서는 UPDATE 쿼리를 명시하지 않아도 자동 반영됨
- 준영속 상태에서는 병경 사항이 DB에 반영되지 않음