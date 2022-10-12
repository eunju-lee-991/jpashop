package jpabook.jpashop.repository;

import jpabook.jpashop.domain.item.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor // final 붙은 변수를 필요로하는 생성자 생성, 그리고 생성자가 한 개면 스프링부트가 자동으로 autowired, 그리고 jpa에서 PersistenceContext 대신 autowired로도 엔티티 매니저 주입
public class ItemRepository {

    private final EntityManager em;

    public void save(Item item) {
        if (item.getId() == null) { // id값이 없다는 것은 디비에 저장된 게 아니라 새로 생성된 객체라는 의미
            em.persist(item);
        } else {
            em.merge(item); // 병합! *병합은 모든 필드를 교체하므로 병합 시 값이 없으면 null로 업데이트 할 위험
        }
    }

    public Item findOne(Long id) {
        return em.find(Item.class, id);
    }

    public List<Item> findAll(){
        return em.createQuery("select i from Item i", Item.class).getResultList();
    }
}
