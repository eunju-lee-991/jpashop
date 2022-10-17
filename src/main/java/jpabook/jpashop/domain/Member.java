package jpabook.jpashop.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Member {

    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    private String name;

    @Embedded
    private Address address;

    @JsonIgnore // 양방향 연관관계에서 한쪽은 @JsonIgnore
    @OneToMany(mappedBy = "member") // order 테이블에 있는 member에 의해 매핑되었다는 뜻
    private List<Order> orders = new ArrayList<>();
}
