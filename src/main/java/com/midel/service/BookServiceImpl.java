package com.midel.service;

import com.midel.dto.BookDTO;
import com.midel.dto.BookMapper;
import com.midel.entity.Book;
import com.midel.repository.BookRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    @Override
    public UUID addBook(BookDTO bookDto) {

        Book book = bookMapper.toBook(bookDto);
        if (bookDto.id() != null && bookDto.id().isBlank()) {
            book.setId(null);
        }
        return bookRepository.save(book).getId();
    }

    @Override
    public BookDTO getBookById(UUID id) {
        Book book = bookRepository.findBookById(id).orElse(null);

        return bookMapper.toBookDTO(book);
    }

    @Override
    public List<BookDTO> getAllBooks() {
        List<Book> bookList = bookRepository.findAll();

        return bookMapper.toDTOList(bookList);
    }

    @Override
    public BookDTO updateBook(UUID id, BookDTO bookDto) {
        Book book = bookMapper.toBook(bookDto);
        book.setId(id);

        System.out.println(book);
        if (book.getId() == null) {
            return null;
        }

        if (bookRepository.findBookById(id).isEmpty()){
            return null;
        }

        Book updatedBook = bookRepository.save(book);
        return bookMapper.toBookDTO(updatedBook);
    }

    @Override
    public boolean deleteBook(UUID id) {
        Book book = bookRepository.findById(id).orElse(null);

        if (book == null) {
            return false;
        }

        bookRepository.delete(book);
        return true;
    }
}
