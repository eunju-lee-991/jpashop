package jpabook.jpashop.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order {

    @Id
    @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member; // order가 연관관계 주인!

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY,  cascade = CascadeType.ALL)
    @JoinColumn(name = "delivery_id")// 1:1 관계인 경우에는 자주 조회하는 테이블에 foreign key 넣음. 그리고 foreign key가 있는 쪽이 연관관계 주인..?
    private Delivery delivery;

    private LocalDateTime orderDate;

    @Enumerated(EnumType.STRING)
    private OrderStatus status; // 주문상태 enum [ORDER, CANCEL]

    //==연관관계 편의 메서드==// 연관관계 메서드는 핵심적인 쪽에..
    // 멤버가 주문을 하면 멤버의 orders에도 값을 넣어줘야함
    // 그래서 원래는 멤버.겟오더.add오더   오더.셋멤버(멤버) 이런 식으로 해줘야하는데 그걸 양방향으로
    public void setMember(Member member) {
        this.member = member;
        member.getOrders().add(this);
    }

    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    public void setDelivery(Delivery delivery) {
        this.delivery = delivery;
        delivery.setOrder(this);
    }

    //==생성 메서드==//
    public static Order createOrder(Member member, Delivery delivery, OrderItem... orderItems) {
        Order order = new Order();
        order.setMember(member);
        order.setDelivery(delivery);
        for (OrderItem orderItem :
                orderItems) {
            order.addOrderItem(orderItem);
        }
        order.setStatus(OrderStatus.ORDER);
        order.setOrderDate(LocalDateTime.now());
        return order;
    }

    //==business method==//
    /**
     *  주문 취소
     */
    public void cancel(){
        if (delivery.getStatus() == DeliveryStatus.COMP) {
            throw new IllegalStateException("you cannot cancel your order completed");
        }
        this.setStatus(OrderStatus.CANCEL);
        for (OrderItem orderItem :
                orderItems) {
            orderItem.cancel();
        }
    }

    //==조회 로직==//
    /**
     *  전체 주문 가격 조회
     */
    public int getTotalPrice(){
//        int totalPrice = 0;
//        for (OrderItem orderItem :
//                orderItems) {
//            totalPrice += orderItem.getTotalPrice();
//        }
//        return totalPrice;
        return orderItems.stream().mapToInt(OrderItem::getTotalPrice).sum();
    }
}
