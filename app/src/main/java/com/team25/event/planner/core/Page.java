package com.team25.event.planner.core;

import java.util.List;

import lombok.Data;
import lombok.Getter;

@Data
@Getter
public class Page<T> {
    private List<T> content;
    private Pageable pageable;
    private boolean last;
    private int totalPages;
    private long totalElements;
    private boolean first;
    private int size;
    private int number;
    private Sort sort;
    private int numberOfElements;
    private boolean empty;

    @Data
    public static class Sort {
        private boolean sorted;
        private boolean empty;
        private boolean unsorted;
    }

    @Data
    public static class Pageable {
        private int pageSize;   // Broj stavki po stranici
        private int pageNumber; // Trenutna stranica
        private boolean isUnpaged; // Da li je stranica bez paginacije
    }

}
