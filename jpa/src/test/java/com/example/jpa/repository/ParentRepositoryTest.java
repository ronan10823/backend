package com.example.jpa.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import com.example.jpa.entity.Child;
import com.example.jpa.entity.Parent;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Transactional // 각각의 테스트 메소드가 실행될 때 하나의 작업으로 처리
@SpringBootTest
public class ParentRepositoryTest {

    @Autowired
    private ParentRepository parentRepository;

    @Autowired
    private ChildRepository childRepository;

    @PersistenceContext
    private EntityManager em;

    @Test
    public void persistenceStateTest() {

        // 1. 비영속 상태(new/ transient)
        Parent p = Parent.builder().name("new 상태").build();
        System.out.println("1) 비영속 상태 : " + p);

        // 2. 영속(managed)
        em.persist(p);
        System.out.println("2) 영속 상태 진입 :" + p);
        // 3. 영속 상태에 있는 엔티티 변경 -> Dirty Check
        p.changeName("이름 변경");
        System.out.println("3) 영속 상태에서 값 변경 : " + p);

        // 4. db에 반영 :flush
        em.flush();
        System.out.println("4) flush 후 DB 반영 완료");

        // 5. 준영속(detached)
        em.detach(p);
        p.changeName("detach 상태에서 이름 변경"); // update 구문 안 일어남(Dirty checking 안 일어났기에)
        System.out.println("5) detach 상태에서 이름 변경 : " + p);

        em.flush(); // select

        // 6. 다시 영속성 상태로 병합(merge)
        Parent merged = em.merge(p);
        merged.changeName("merge 후 다시 영속 상태");
        System.out.println("6) merge 결과 영속 엔티티 : " + merged);

        em.flush(); // update 반영

    }

    @Commit
    @Test
    public void testInsert() {

        Parent parent = Parent.builder().name("parent1").build();
        parentRepository.save(parent);

        Child child1 = Child.builder().name("first").parent(parent).build();
        Child child2 = Child.builder().name("second").parent(parent).build();
        Child child3 = Child.builder().name("third").parent(parent).build();
        childRepository.save(child1);
        childRepository.save(child2);
        childRepository.save(child3);
    }

    @Transactional(readOnly = true) // dirty checking 하지 말기
    @Test
    public void testRead() {

        Parent parent = parentRepository.findById(1L).get();
        // parent.changeName("변경 이름");
        // if you don't want to be watched by dirty checking, you can input
        // transactional.
        System.out.println(parent);
        // let's check the child
        parent.getChilds().forEach(child -> System.out.println(child));

    }

    @Commit
    @Test
    public void testUpdate() {

        Parent parent = parentRepository.findById(1L).get();
        parent.changeName("변경 이름"); // dirty checking 하면서 자동으로 save 적용

    }

    // cascade
    @Commit
    @Test
    public void testCascadeInsert() {
        // 부모 저장 시 관련있는 자식들도 같이 저장
        Parent parent = Parent.builder().name("parent2").build();
        parent.getChilds().add(Child.builder().name("child1").parent(parent).build());
        parent.getChilds().add(Child.builder().name("child2").parent(parent).build());

        parentRepository.save(parent);

        // childRepository.save(child1);
    }

    @Commit
    @Test
    public void testCascadeInsert2() {
        // 부모 저장 시 관련있는 자식들도 같이 저장
        Parent parent = Parent.builder().name("parent4").build();
        Child child1 = Child.builder().name("child3").build();
        child1.setParent(parent);

        Child child2 = Child.builder().name("child4").build();
        child2.setParent(parent);

        parentRepository.save(parent);

        // childRepository.save(child1);
    }

    @Commit
    @Test
    public void testCascadeDelete() {
        // 부모 삭제 시 관련있는 자식들도 같이 삭제
        // 외래키 제약조건에서
        // 삭제 시 1. 자식 2. 부모 순서로 삭제해야 오류가 없음

        // Parent parent = parentRepository.findById(6L).get();
        // parentRepository.delete(parent);

        parentRepository.deleteById(6L);
    }

    @Commit
    @Test
    public void testOrphanDelete() {
        // 부모 삭제 시 관련있는 자식들도 같이 삭제
        // 외래키 제약조건에서
        // 삭제 시 1. 자식 2. 부모 순서로 삭제해야 오류가 없음

        Parent p = parentRepository.findById(4L).get();
        // 자식 조회
        p.getChilds().forEach(c -> System.out.println(c));
        p.getChilds().remove(0); // list에서 0번 자식 제거
        parentRepository.save(p);
    }
}
