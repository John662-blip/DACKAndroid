package com.example.doancuoiky.CallAPI.Response.genera;

import java.util.List;

public class ProductBody {
    private List<Product> content;
    private PageableInfo pageable;
    private boolean last;
    private int totalElements;
    private int totalPages;
    private int size;
    private int number;
    private SortInfo sort;
    private int numberOfElements;
    private boolean first;
    private boolean empty;

    public List<Product> getContent() {
        return content;
    }

    public void setContent(List<Product> content) {
        this.content = content;
    }

    public PageableInfo getPageable() {
        return pageable;
    }

    public void setPageable(PageableInfo pageable) {
        this.pageable = pageable;
    }

    public boolean isLast() {
        return last;
    }

    public void setLast(boolean last) {
        this.last = last;
    }

    public int getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(int totalElements) {
        this.totalElements = totalElements;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public SortInfo getSort() {
        return sort;
    }

    public void setSort(SortInfo sort) {
        this.sort = sort;
    }

    public int getNumberOfElements() {
        return numberOfElements;
    }

    public void setNumberOfElements(int numberOfElements) {
        this.numberOfElements = numberOfElements;
    }

    public boolean isFirst() {
        return first;
    }

    public void setFirst(boolean first) {
        this.first = first;
    }

    public boolean isEmpty() {
        return empty;
    }

    public void setEmpty(boolean empty) {
        this.empty = empty;
    }

    // Getter & Setter
}