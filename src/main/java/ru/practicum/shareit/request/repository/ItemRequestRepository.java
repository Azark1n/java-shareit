package ru.practicum.shareit.request.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.User;

import java.util.List;

public interface ItemRequestRepository extends JpaRepository<ItemRequest,Integer> {
    List<ItemRequest> findByRequesterNotOrderByCreatedDesc(User requester, Pageable pageable);

    List<ItemRequest> findByRequesterOrderByCreatedDesc(User requester);
}