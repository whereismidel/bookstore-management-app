package com.midel.grpc;

import com.midel.grpc.BookServiceOuterClass.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {com.midel.Main.class})
public class BookGrpcServiceIT implements PostgresTestContainer {

    private static BookServiceGrpc.BookServiceBlockingStub blockingStub;

    @BeforeAll
    public static void setup() {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 9090)
                .usePlaintext()
                .build();

        blockingStub = BookServiceGrpc.newBlockingStub(channel);
    }

    @AfterAll
    public static void tearDown() {
        blockingStub = null;
    }

    @Test
    void addAndGetBook() {
        // Create a book to add
        Book bookToAdd = Book.newBuilder()
                .setTitle("Test Book")
                .setAuthor("Test Author")
                .setIsbn("1234567890")
                .setQuantity(5)
                .build();

        // Add the book
        AddBookRequest addBookRequest = AddBookRequest
                .newBuilder()
                .setBook(bookToAdd)
                .build();
        BookRequest addBookResponse = blockingStub.addBook(addBookRequest);

        assertNotNull(addBookResponse);
        assertNotNull(addBookResponse.getId());

        // Get the added book by ID
        BookRequest getBookRequest = BookRequest
                .newBuilder()
                .setId(addBookResponse.getId())
                .build();
        Book responseBook = blockingStub.getBookById(getBookRequest);

        assertNotNull(responseBook);
        assertEquals(bookToAdd.getTitle(), responseBook.getTitle());
        assertEquals(bookToAdd.getAuthor(), responseBook.getAuthor());
        assertEquals(bookToAdd.getIsbn(), responseBook.getIsbn());
        assertEquals(bookToAdd.getQuantity(), responseBook.getQuantity());
    }

    @Test
    void updateBook() {
        // Create a book to update
        Book bookToUpdate = Book.newBuilder()
                .setTitle("Updated Book")
                .setAuthor("Updated Author")
                .setIsbn("0987654321")
                .setQuantity(10)
                .build();

        // Add the book to get an ID
        AddBookRequest addBookRequest = AddBookRequest
                .newBuilder()
                .setBook(bookToUpdate)
                .build();
        BookRequest addBookResponse = blockingStub.addBook(addBookRequest);

        assertNotNull(addBookResponse);
        assertNotNull(addBookResponse.getId());

        // Update the book
        UpdateBookRequest updateBookRequest = UpdateBookRequest
                .newBuilder()
                .setId(addBookResponse.getId())
                .setBook(bookToUpdate)
                .build();

        Book updatedBook = blockingStub.updateBook(updateBookRequest);

        assertNotNull(updatedBook);
        assertEquals(bookToUpdate.getTitle(), updatedBook.getTitle());
        assertEquals(bookToUpdate.getAuthor(), updatedBook.getAuthor());
        assertEquals(bookToUpdate.getIsbn(), updatedBook.getIsbn());
        assertEquals(bookToUpdate.getQuantity(), updatedBook.getQuantity());
    }

    @Test
    void deleteBook() {
        // Create a book to delete
        Book bookToDelete = Book.newBuilder()
                .setTitle("Book to Delete")
                .setAuthor("Delete Author")
                .setIsbn("111222333")
                .setQuantity(20)
                .build();

        // Add the book to get an ID
        AddBookRequest addBookRequest = AddBookRequest
                .newBuilder()
                .setBook(bookToDelete)
                .build();
        BookRequest addBookResponse = blockingStub.addBook(addBookRequest);

        assertNotNull(addBookResponse);
        assertNotNull(addBookResponse.getId());

        // Delete the book
        DeleteBookRequest deleteBookRequest = DeleteBookRequest
                .newBuilder()
                .setId(addBookResponse.getId())
                .build();

        Empty deleteBookResponse = blockingStub.deleteBook(deleteBookRequest);

        assertNotNull(deleteBookResponse);

        // Verify the book is deleted by trying to get it by ID (should throw an exception)
        assertThrows(StatusRuntimeException.class,
                () -> blockingStub.getBookById(BookRequest.newBuilder().setId(addBookResponse.getId()).build()));
    }
}
