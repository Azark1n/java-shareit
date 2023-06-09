package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.User;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item,Integer> {
    @Query("select i from Item i where " +
            "(upper(i.name) like upper(concat('%', ?1, '%')) " +
            "or upper(i.description) like upper(concat('%', ?1, '%'))" +
            "and i.available = true)")
    List<Item> findByNameLikeIgnoreCaseOrDescriptionLikeIgnoreCase(String text);

    List<Item> findByOwnerOrderById(User owner);

    List<Item> findByRequest(ItemRequest request);

}
