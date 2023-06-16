package ru.practicum.shareit.booking;

import lombok.*;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder(toBuilder = true)
@Table(name = "bookings")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @NotNull
    @Column(name = "start_date")
    LocalDateTime start;

    @NotNull
    @Column(name = "end_date")
    LocalDateTime end;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "item_id")
    Item item;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "booker_id")
    User booker;

    @NotNull
    @Enumerated(EnumType.STRING)
    BookingStatus status;
}
