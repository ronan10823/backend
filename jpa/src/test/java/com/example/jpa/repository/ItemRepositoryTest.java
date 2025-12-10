package com.example.jpa.repository;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.jpa.entity.Item;
import com.example.jpa.entity.constant.ItemSellStatus;

@SpringBootTest
public class ItemRepositoryTest {

    @Autowired
    private ItemRepository itemRepository;

    @Test
    public void insertTest() {
        for (int i = 1; i < 10; i++) {
            Item item = Item.builder()
                    .code("P00" + i)
                    .itemPrice(1000 * i)
                    .stockNumber(10)
                    .itemDetail("Item Detail" + i)
                    .status(ItemSellStatus.SELL)
                    .itemNm("Item" + i)
                    .build();
            itemRepository.save(item);
        }
    }

    @Test
    public void updateTest() {
        // item 상태
        Item item = itemRepository.findById("P005").get();
        item.changeStatus(ItemSellStatus.SOLDOUT);
        itemRepository.save(item);
    }

    @Test
    public void updateTest2() {
        // item 재고 수량 변경 -
        Item item = itemRepository.findById("P006").get();
        item.changeStockNumber(5);
        itemRepository.save(item);
    }

    @Test
    public void deleteTest() {
        itemRepository.deleteById("P008");
    }

    @Test
    public void readTest() {
        System.out.println(itemRepository.findById("P008").get());
    }

    @Test
    public void readTest2() {
        itemRepository.findAll().forEach(item -> System.out.println(item));
    }

    @Test
    public void aggrTest() {
        List<Object[]> result = itemRepository.aggr();
        for (Object[] objects : result) {

            System.out.println(Arrays.toString(objects)); // [9, 45000, 5000.0, 9000, 1000]

            System.out.println("아이템의 수 " + objects[0]);
            System.out.println("합계 " + objects[1]);
            System.out.println("평균 " + objects[2]);
            System.out.println("최대 " + objects[3]);
            System.out.println("최소 " + objects[4]);
        }
    }

}
