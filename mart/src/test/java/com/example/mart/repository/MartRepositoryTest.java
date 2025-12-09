package com.example.mart.repository;

import java.util.List;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import com.example.mart.entity.Category;
import com.example.mart.entity.CategoryItem;
import com.example.mart.entity.Delivery;
import com.example.mart.entity.Item;
import com.example.mart.entity.Member;
import com.example.mart.entity.Order;
import com.example.mart.entity.OrderItem;
import com.example.mart.entity.constant.DeliveryStatus;
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

    @Autowired
    private DeliveryRepository deliveryRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CategoryItemRepository categoryItemRepository;

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

    @Commit
    @Transactional
    @Test
    public void orderCascadeTest() {
        Member member = memberRepository.findById(3L).get();
        Item item = itemRepository.findById(2L).get();

        Order order = Order.builder()
                .member(member)
                .orderStatus(OrderStatus.ORDER)
                .build();

        OrderItem orderItem = OrderItem.builder()
                .item(item)
                // .order(order)
                .orderPrice(200000)
                .count(1)
                .build();

        // order.getOrderItems().add(orderItem);

        order.addOrderItem(orderItem);
        orderRepository.save(order);

        // orderItemRepository.save(orderItem);

    }

    @Commit
    @Transactional
    @Test
    public void testUpdate() {

        // 3번 고객의 city 변경
        // 먼저 찾아라.
        Member member = memberRepository.findById(3L).get();
        // city 변경하는 메소드 -> 엔티티에서 생성
        member.changeCity("부산"); // managed entity는 변경 사항 감지 기능(dirty checking)

        // memberRepository.save(member)
        // 3번 item 수량 변경 -> 10개
        Item item = itemRepository.findById(3L).get();
        item.changeQuantity(10);

        // order 주문 상태 변경 -> cancel
        Order order = orderRepository.findById(1L).get();
        order.changeOrderStatus(OrderStatus.CANCEL);

    }

    @Commit
    @Transactional
    @Test
    public void testDelete() {
        // 주문 제거
        // order, order_item 제거

        // 방법1: 자식 삭제 -> 부모 삭제

        // 방법2: 부모 삭제 시 자식 같이 삭제(sql ON DELETE CASCADE와 동일한 개념)
        orderRepository.deleteById(4L);

    }

    @Commit
    @Transactional
    @Test
    public void testOrphanDelete() {

        // 주문 조회
        Order order = orderRepository.findById(5L).get();
        System.out.println();

        // 주문 아이템 조회 [OrderItem(id=5, orderPrice=200000, count=1)]
        System.out.println(order.getOrderItems());

        // 리스트에서
        order.getOrderItems().remove(0);
        System.out.println("삭제 후 " + order.getOrderItems()); // 삭제 후 []
        orderRepository.save(order); // order item 쪽이 사라지게 하려는 목적, dirty checking으로 save가 아니어도 사라지기는 한다.
    }

    @Commit
    @Transactional
    @Test
    public void testDelivery() {

        Member member = memberRepository.findById(2L).get();
        Item item = itemRepository.findById(1L).get();

        Delivery delivery = Delivery.builder()
                .city("서울")
                .street("114")
                .zipcode("11061")
                .deliveryStatus(DeliveryStatus.COMP)
                .build();
        deliveryRepository.save(delivery); // 얘는 저장해야 한다.

        Order order = Order.builder()
                .member(member)
                .delivery(delivery)
                .orderStatus(OrderStatus.ORDER)
                .build();

        OrderItem orderItem = OrderItem.builder()
                .item(item)
                .order(order)
                .orderPrice(200000)
                .count(1)
                .build();

        order.addOrderItem(orderItem);
        orderRepository.save(order);
    }

    @Commit
    @Transactional
    @Test
    public void testCascadeDelivery() {

        Member member = memberRepository.findById(2L).get();
        Item item = itemRepository.findById(1L).get();

        Delivery delivery = Delivery.builder()
                .city("서울")
                .street("114")
                .zipcode("11061")
                .deliveryStatus(DeliveryStatus.COMP)
                .build();
        // deliveryRepository.save(delivery); // P) 얘를 빼고 싶다. .

        Order order = Order.builder()
                .member(member)
                .delivery(delivery)
                .orderStatus(OrderStatus.ORDER)
                .build();

        OrderItem orderItem = OrderItem.builder()
                .item(item)
                .order(order)
                .orderPrice(200000)
                .count(1)
                .build();

        order.addOrderItem(orderItem);
        orderRepository.save(order); // p) 여기에서 delivery도 같이 저장시키게 하고 싶다.
    }

    @Transactional(readOnly = true)
    @Test
    public void orderReadTest2() {

        // order info select(조회)
        Order order = orderRepository.findById(3L).get();
        // Granted with order -> member, orderItems, delivery (in Order Entity)
        System.out.println(order); // Order(id=3, orderStatus=ORDER)
        // findbyid -> select statement (SQL)

        // Customer select(조회)
        System.out.println(order.getMember().getName());

        // order item select(조회)
        System.out.println(order.getOrderItems());
        System.out.println(order.getOrderItems().get(0));

        // delivery status select(조회)
        System.out.println(order.getDelivery());
        // findbyid -> left join statement (SQL)
    }

    @Transactional(readOnly = true)
    @Test
    public void memberReadTest() {

        // member info select(조회)
        Member member = memberRepository.findById(2L).get();
        // Granted with member -> member, orderItems, delivery (in Member Entity)
        // findbyid -> select

        // member select(조회)
        System.out.println(member);
        // member -> select

        // order select(조회)
        System.out.println(member.getOrders()); // select

        List<Order> orders = member.getOrders(); // left join
        // member.getOrders return List<Order>.
        // So, 'orders' can pick you up the info of order.
        orders.forEach(order -> {
            System.out.println(order.getDelivery()); // select
            System.out.println(order.getMember()); // left join
            System.out.println(order.getOrderItems()); // select
        });

    }

    @Transactional(readOnly = true)
    @Test
    public void orderItemReadTest() {

        // member info select(조회)
        OrderItem orderItem = orderItemRepository.findById(3L).get();
        // Granted with orderItem -> member, orderItems, delivery (in Member Entity)

        // member select(조회, orderItem)
        System.out.println(orderItem);

        // order select(조회) (in orderItem)
        System.out.println(orderItem.getOrder());

        Order order = orderItem.getOrder();
        // Delivery select
        System.out.println(order.getDelivery()); // select
        // Customer select
        System.out.println(order.getMember()); // left join
        // Order Item select
        System.out.println(order.getOrderItems()); // select

        // Item select ( in orderItem )
        System.out.println(orderItem.getItem()); // select

    }

    // --------------------------------=
    // ManyToMany 설정을 JPA에게 시킨 경우
    // 테스트 구문
    // --------------------------------

    @Test
    public void categoryTest() {

        Item item = itemRepository.findById(3L).get();

        Category category = Category.builder().name("가전제품").build();
        // category.getItems().add(item); // 3번 제품 = 가전제품
        categoryRepository.save(category);

        category = Category.builder().name("생활용품").build();
        // category.getItems().add(item); // 3번 제품 카테고리 += 생활용품
        categoryRepository.save(category);
    }

    @Test
    public void categoryReadTest() {

        Category category = categoryRepository.findById(1L).get();

        // category select
        System.out.println(category);

        // Item in category select
        // System.out.println(category.getItems());
    }

    @Transactional(readOnly = true)
    @Test
    public void itemReadTest() {

        Item item = itemRepository.findById(3L).get();

        // item select
        System.out.println(item);

        // category of item select
        // System.out.println(item.getCategories());
    }

    // --------------------------------=
    // ManyToMany 설정을 ManyToOne 관계로 작성 후
    // 테스트 구문
    // --------------------------------

    @Test
    public void categoryTest2() {

        Item item = itemRepository.findById(4L).get();

        // 카테고리 생성
        Category category = Category.builder().name("신혼용품").build();
        categoryRepository.save(category);

        CategoryItem categoryItem = CategoryItem.builder()
                .category(category)
                .item(item)
                .build();

        categoryItemRepository.save(categoryItem);

        category = categoryRepository.findById(1L).get();
        categoryItem = CategoryItem.builder()
                .category(category)
                .item(item)
                .build();

        categoryItemRepository.save(categoryItem);

    }

    @Transactional(readOnly = true)
    @Test
    public void categoryItemReadTest2() {

        CategoryItem categoryItem = categoryItemRepository.findById(1L).get();

        // 카테고리 아이템 조회
        System.out.println(categoryItem);

        // 카테고리 정보 조회
        System.out.println(categoryItem.getCategory());

        // 아이템 정보 조회
        System.out.println(categoryItem.getItem());

        // 양방향 연 뒤
        Category category = categoryRepository.findById(1L).get();
        System.out.println(category);
        System.out.println(category.getCategoryItems());

        Item item = itemRepository.findById(3L).get();
        System.out.println(item);
        System.out.println(item.getCategoryItems());
    }

}
