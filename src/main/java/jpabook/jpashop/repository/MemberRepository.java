package jpabook.jpashop.repository;

import jpabook.jpashop.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;


@RequiredArgsConstructor
@Repository //Spring이 자동으로 빈으로 관리 ComponentScan
public class MemberRepository  {

//    @PersistenceContext //스프링부트에서는 autowired로 인젝션 대체 가능
//    private EntityManager em; // Spring이 엔티티 매니저 만들어서 주입

    private final EntityManager em;

    public void save(Member member) {
        em.persist(member);
    }

    public Member fineOne(Long id) {
        return em.find(Member.class, id); //단건조회. (타입, pk)
    }

    public List<Member> findAll(){
        return em.createQuery("select m from Member m", Member.class)
                .getResultList();
    }

    public List<Member> findByName(String name) {
        return em.createQuery("select m from Member m where m.name = :name", Member.class)
                .setParameter("name", name)
                .getResultList();
    }
}
