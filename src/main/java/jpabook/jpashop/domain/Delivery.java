package jpabook.jpashop.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
public class Delivery {

    @Id
    @GeneratedValue
    @Column(name = "delivery_id")
    private Long id;

    // 양방향 연관관계에서 한쪽은 @JsonIgnore
    @JsonIgnore
    @OneToOne(mappedBy = "delivery", fetch = FetchType.LAZY) // 연관관계 주인 아니고 거울...?
    private Order order;

    @Embedded
    private Address address;

    @Enumerated(EnumType.STRING)
    private DeliveryStatus status;
}
