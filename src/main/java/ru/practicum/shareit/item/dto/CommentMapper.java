package ru.practicum.shareit.item.dto;

import lombok.extern.slf4j.Slf4j;
import ru.practicum.shareit.item.Comment;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;

@Slf4j
public class CommentMapper {
    private CommentMapper() {
        throw new IllegalStateException("Utility class");
    }

    public static CommentOutDto toDto(Comment comment) {
        return CommentOutDto.builder()
                .id(comment.getId())
                .text(comment.getText())
                .authorName(comment.getAuthor().getName())
                .created(comment.getCreated())
                .build();
    }

    public static Comment toModel(CommentInDto commentInDto, User author, Item item) {
        return Comment.builder()
                .text(commentInDto.getText())
                .author(author)
                .item(item)
                .created(LocalDateTime.now())
                .build();
    }
}
