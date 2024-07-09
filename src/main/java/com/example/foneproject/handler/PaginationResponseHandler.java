package com.example.foneproject.handler;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
public class PaginationResponseHandler<T> {
    List<T> content;
    CustomPageable pageable;

    public PaginationResponseHandler(Page<T> page) {
        this.content = page.getContent();
        this.pageable = new CustomPageable(
                page.getPageable().getPageNumber(),
                page.getPageable().getPageSize(),
                page.getTotalPages(),
                page.getTotalElements()
        );
    }

    @Data
    @AllArgsConstructor
    static class CustomPageable {
        int pageNumber;
        int pageSize;
        int totalPages;
        long totalElements;
    }
}
