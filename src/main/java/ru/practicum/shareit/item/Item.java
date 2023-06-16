package ru.practicum.shareit.item;

import lombok.*;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.User;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder(toBuilder = true)
@Table(name = "items")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @NotNull
    @NotBlank
    String name;

    @NotNull
    @NotBlank
    String description;

    @NotNull
    @Column(name = "is_available")
    Boolean available;

    @NotNull
    @ManyToOne
    User owner;

    //ItemRequest request;
}

