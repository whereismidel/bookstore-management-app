package com.midel.grpc;


import com.midel.dto.BookDTO;
import com.midel.dto.BookMapper;
import com.midel.service.BookService;
import io.grpc.stub.StreamObserver;

import java.util.List;
import java.util.UUID;

import com.midel.grpc.BookServiceGrpc.*;
import com.midel.grpc.BookServiceOuterClass.*;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;

import static io.grpc.Status.INVALID_ARGUMENT;
import static io.grpc.Status.NOT_FOUND;

@GrpcService
@RequiredArgsConstructor
public class BookGrpcServiceImpl extends BookServiceImplBase {

    private final BookService bookService;
    private final BookMapper bookMapper;

    @Override
    public void addBook(AddBookRequest request,
                        StreamObserver<BookRequest> responseObserver) {

        BookDTO bookDTO = bookMapper.toBookDTO(request.getBook());
        UUID id = bookService.addBook(bookDTO);

        BookRequest response = BookRequest
                .newBuilder()
                .setId(id.toString())
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void getAllBooks(Empty request,
                            StreamObserver<BookList> responseObserver) {

        List<BookDTO> books = bookService.getAllBooks();

        BookList bookList = BookList
                .newBuilder()
                .addAllBooks(bookMapper.toProtoList(books))
                .build();

        responseObserver.onNext(bookList);
        responseObserver.onCompleted();
    }

    @Override
    public void getBookById(BookRequest request,
                            StreamObserver<Book> responseObserver) {

        UUID id;
        try {
            id = UUID.fromString(request.getId());
        } catch (IllegalArgumentException e) {
            responseObserver.onError(
                    INVALID_ARGUMENT.withDescription("Invalid UUID format for id: " + request.getId())
                            .asRuntimeException()
            );
            return;
        }

        BookDTO bookDTO = bookService.getBookById(id);
        if (bookDTO == null) {
            responseObserver.onError(
                    NOT_FOUND.withDescription("Book with id = " + id + " was not found.")
                            .asRuntimeException()
            );
            return;
        }

        Book response = bookMapper.toProtoBook(bookDTO);
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void updateBook(UpdateBookRequest request,
                           StreamObserver<Book> responseObserver) {

        UUID id;
        try {
            id = UUID.fromString(request.getId());
        } catch (IllegalArgumentException e) {
            responseObserver.onError(
                    INVALID_ARGUMENT.withDescription("Invalid UUID format for id: " + request.getId())
                            .asRuntimeException()
            );
            return;
        }

        BookDTO bookDTO = bookMapper.toBookDTO(request.getBook());
        BookDTO updatedBook = bookService.updateBook(id, bookDTO);
        if (updatedBook == null) {
            responseObserver.onError(
                    NOT_FOUND.withDescription("Book with id = " + id + " was not found.")
                            .asRuntimeException()
            );
            return;
        }

        Book response = bookMapper.toProtoBook(updatedBook);
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void deleteBook(DeleteBookRequest request,
                           StreamObserver<Empty> responseObserver) {

        UUID id;
        try {
            id = UUID.fromString(request.getId());
        } catch (IllegalArgumentException e) {
            responseObserver.onError(
                    INVALID_ARGUMENT.withDescription("Invalid UUID format for id: " + request.getId())
                            .asRuntimeException()
            );
            return;
        }

        if (!bookService.deleteBook(id)){
            responseObserver.onError(
                    NOT_FOUND.withDescription("Book with id = " + id + " was not found.")
                            .asRuntimeException()
            );
            return;
        }

        responseObserver.onNext(Empty.newBuilder().build());
        responseObserver.onCompleted();
    }
}
