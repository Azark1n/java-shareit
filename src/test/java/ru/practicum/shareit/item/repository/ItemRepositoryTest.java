package ru.practicum.shareit.item.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ItemRepositoryTest {
    @Autowired
    UserRepository userRepository;
    @Autowired
    ItemRepository itemRepository;
    private User user1;
    private Item item1;
    private Item item2;

    @BeforeEach
    void setUp() {
        user1 = User.builder()
                .id(1)
                .name("user1")
                .email("user1@mail.com")
                .build();
        userRepository.save(user1);

        item1 = Item.builder()
                .id(1)
                .name("item1")
                .description("description1")
                .available(true)
                .owner(user1)
                .build();
        itemRepository.save(item1);

        item2 = Item.builder()
                .id(2)
                .name("none")
                .description("description")
                .available(true)
                .owner(user1)
                .build();
        itemRepository.save(item2);
    }

    @Test
    void findByNameLikeIgnoreCaseOrDescriptionLikeIgnoreCaseItem() {
        List<Item> itemList = itemRepository.findByNameLikeIgnoreCaseOrDescriptionLikeIgnoreCase("em");
        assertNotNull(itemList);
        assertEquals(1, itemList.size());
        assertEquals(item1, itemList.get(0));
    }
    @Test
    void findByNameLikeIgnoreCaseOrDescriptionLikeIgnoreCaseItemAndNone() {
        List<Item> itemList = itemRepository.findByNameLikeIgnoreCaseOrDescriptionLikeIgnoreCase("descr");
        assertNotNull(itemList);
        assertEquals(2, itemList.size());
        assertEquals(itemList, List.of(item1, item2));
    }
}