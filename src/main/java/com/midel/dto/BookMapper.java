package com.midel.dto;

import com.midel.entity.Book;
import com.midel.grpc.BookServiceOuterClass;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BookMapper {
    Book toBook(BookDTO bookDTO);
    @Mapping(target = "id", ignore = true)
    BookDTO toBookDTO(BookServiceOuterClass.Book book);
    BookDTO toBookDTO(Book book);
    BookServiceOuterClass.Book toProtoBook(BookDTO bookDTO);


    List<Book> toModelList(List<BookDTO> dtos);
    List<BookDTO> toDTOList(List<Book> entities);
    List<BookServiceOuterClass.Book> toProtoList(List<BookDTO> dtos);
}
