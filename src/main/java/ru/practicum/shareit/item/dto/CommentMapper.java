package ru.practicum.shareit.item.dto;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.item.Comment;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;

@UtilityClass
public class CommentMapper {
    public static CommentDto toDto(Comment model) {
        return CommentDto.builder()
                .id(model.getId())
                .text(model.getText())
                .authorName(model.getAuthor().getName())
                .created(model.getCreated())
                .build();
    }

    public static Comment toModel(CommentDto dto, User author, Item item) {
        return Comment.builder()
                .text(dto.getText())
                .author(author)
                .item(item)
                .created(LocalDateTime.now())
                .build();
    }
}
