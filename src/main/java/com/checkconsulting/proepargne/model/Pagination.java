package com.checkconsulting.proepargne.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Pagination<T> {
    private List<T> content;
    private boolean first;
    private boolean last;
    private int totalPages;
    private long totalElements;
    private int pageSize;
    private int pageNumber;
}
