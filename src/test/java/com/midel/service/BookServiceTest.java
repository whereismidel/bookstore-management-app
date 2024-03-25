package com.midel.service;

import com.midel.Main;
import com.midel.dto.BookDTO;
import com.midel.dto.BookMapper;
import com.midel.entity.Book;
import com.midel.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = Main.class)
@TestPropertySource(properties = {"grpc.server.port=-1"})
public class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private BookMapper bookMapper;

    private BookServiceImpl bookService;

    private BookDTO testBookDTO;
    private Book testBook;
    @BeforeEach
    void setUp() {
        bookService = new BookServiceImpl(bookRepository, bookMapper);
        testBookDTO = new BookDTO("", "Test Book", "Test Author", "ISBN123", 5);
        testBook = new Book(UUID.randomUUID(), "Test Book", "Test Author", "ISBN123", 5);
    }

    @Test
    void testAddBook() {
        // Arrange
        when(bookMapper.toBook(testBookDTO)).thenReturn(testBook);
        when(bookRepository.save(testBook)).thenReturn(testBook);

        // Act
        UUID result = bookService.addBook(testBookDTO);

        // Assert
        assertEquals(testBook.getId(), result);
        verify(bookMapper, times(1)).toBook(testBookDTO);
        verify(bookRepository, times(1)).save(testBook);
    }

    @Test
    void testGetBookById() {
        // Arrange
        UUID bookId = UUID.randomUUID();
        when(bookRepository.findBookById(bookId)).thenReturn(Optional.of(testBook));
        when(bookMapper.toBookDTO(testBook)).thenReturn(testBookDTO);

        // Act
        BookDTO result = bookService.getBookById(bookId);

        // Assert
        assertEquals(testBookDTO, result);
        verify(bookRepository, times(1)).findBookById(bookId);
        verify(bookMapper, times(1)).toBookDTO(testBook);
    }

    @Test
    void testGetAllBooks() {
        // Arrange
        List<Book> bookList = new ArrayList<>();
        bookList.add(testBook);
        when(bookRepository.findAll()).thenReturn(bookList);
        when(bookMapper.toDTOList(bookList)).thenReturn(List.of(testBookDTO));

        // Act
        List<BookDTO> result = bookService.getAllBooks();

        // Assert
        assertEquals(1, result.size());
        assertEquals(testBookDTO, result.get(0));
        verify(bookRepository, times(1)).findAll();
        verify(bookMapper, times(1)).toDTOList(bookList);
    }

    @Test
    void testUpdateBook() {
        // Arrange
        UUID bookId = UUID.randomUUID();
        when(bookMapper.toBook(testBookDTO)).thenReturn(testBook);
        when(bookRepository.findBookById(bookId)).thenReturn(Optional.of(testBook));
        when(bookRepository.save(testBook)).thenReturn(testBook);
        when(bookMapper.toBookDTO(testBook)).thenReturn(testBookDTO);

        // Act
        BookDTO result = bookService.updateBook(bookId, testBookDTO);

        // Assert
        assertEquals(testBookDTO, result);
        verify(bookMapper, times(1)).toBook(testBookDTO);
        verify(bookRepository, times(1)).findBookById(bookId);
        verify(bookRepository, times(1)).save(testBook);
        verify(bookMapper, times(1)).toBookDTO(testBook);
    }

    @Test
    void testDeleteBook() {
        // Arrange
        UUID bookId = UUID.randomUUID();
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(testBook));

        // Act
        boolean result = bookService.deleteBook(bookId);

        // Assert
        assertTrue(result);
        verify(bookRepository, times(1)).findById(bookId);
        verify(bookRepository, times(1)).delete(testBook);
    }
}
