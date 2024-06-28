package com.example.springbootstructurever2.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;

@Getter
@Builder
public class PageResponse<T> implements Serializable {
    private int page;
    private int size;
    private long totalElement;
    private int totalPage;
    private T items;
}
