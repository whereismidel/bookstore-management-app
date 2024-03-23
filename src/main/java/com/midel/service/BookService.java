package com.midel.service;

import com.midel.dto.BookDTO;

import java.util.List;
import java.util.UUID;


public interface BookService {

    UUID addBook(BookDTO bookDto);

    BookDTO getBookById(UUID id);

    List<BookDTO> getAllBooks();

    BookDTO updateBook(UUID id, BookDTO bookDto);
    boolean deleteBook(UUID id);
}
