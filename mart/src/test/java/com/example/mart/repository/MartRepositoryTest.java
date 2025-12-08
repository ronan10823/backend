package com.example.mart.repository;

import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.example.mart.entity.Item;
import com.example.mart.entity.Member;
import com.example.mart.entity.Order;
import com.example.mart.entity.OrderItem;
import com.example.mart.entity.constant.OrderStatus;

@SpringBootTest
public class MartRepositoryTest {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Test
    public void insertMemberTest() {
        // 5 명의 Member 삽입
        IntStream.rangeClosed(1, 5).forEach(i -> {
            Member member = Member.builder()
                    .name("user" + i)
                    .city("Seoul")
                    .street("724-1" + i)
                    .zipcode("1650" + i)
                    .build();

            memberRepository.save(member);
        });
    }

    @Test
    public void insertItemTest() {
        // 5 명의 Item 삽입
        IntStream.rangeClosed(1, 5).forEach(i -> {
            Item item = Item.builder()
                    .name("user" + i)
                    .price(250000)
                    .quantity(i * 5)
                    .build();

            itemRepository.save(item);
        });
    }

    @Test
    public void orderTest() {
        Member member = memberRepository.findById(2L).get();
        Item item = itemRepository.findById(1L).get();

        Order order = Order.builder()
                .member(member)
                .orderStatus(OrderStatus.ORDER)
                .build();
        orderRepository.save(order);

        OrderItem orderItem = OrderItem.builder()
                .item(item)
                .order(order)
                .orderPrice(200000)
                .count(1)
                .build();
        orderItemRepository.save(orderItem);
    }

    @Transactional(readOnly = true)
    @Test
    public void orderReadTest() {
        // 2번 고객의 주문 내역 조회 (2번 고객이 기준 X = findbyyid 사용 불가 )
        // orderRepository.findById(null); -> 만들면 된댜.

        Member member = Member.builder().id(2L).build();
        Order order = orderRepository.findByMember(member).get();
        System.out.println(order);

        // 주문 상품 정보
        order.getOrderItems().forEach(i -> {
            System.out.println(i);
            // 주문 상품의 상세 정보 조회
            System.out.println(i.getItem());
        });

        // 고객의 상세 정보 조회
        System.out.println(order.getMember());

    }
}
