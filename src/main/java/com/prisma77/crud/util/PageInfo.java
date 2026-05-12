package com.prisma77.crud.util;

import java.util.List;

public class PageInfo<T> {
    private List<T> items;       // 현재 페이지 데이터
    private int currentPage;     // 현재 페이지 번호
    private int totalPages;      // 전체 페이지 수
    private long totalItems;     // 전체 데이터 수
    private int pageSize;        // 페이지당 항목 수

    // 기본 생성자
    public PageInfo() {}

    // 전체 생성자
    public PageInfo(List<T> items, int currentPage, int totalPages, long totalItems, int pageSize) {
        this.items = items;
        this.currentPage = currentPage;
        this.totalPages = totalPages;
        this.totalItems = totalItems;
        this.pageSize = pageSize;
    }

    // Getter, Setter
    public List<T> getItems() { return items; }
    public void setItems(List<T> items) { this.items = items; }

    public int getCurrentPage() { return currentPage; }
    public void setCurrentPage(int currentPage) { this.currentPage = currentPage; }

    public int getTotalPages() { return totalPages; }
    public void setTotalPages(int totalPages) { this.totalPages = totalPages; }

    public long getTotalItems() { return totalItems; }
    public void setTotalItems(long totalItems) { this.totalItems = totalItems; }

    public int getPageSize() { return pageSize; }
    public void setPageSize(int pageSize) { this.pageSize = pageSize; }

    // 편의 메서드들
    public boolean hasPrevious() {
        return currentPage > 1;
    }

    public boolean hasNext() {
        return currentPage < totalPages;
    }

    public int getPreviousPage() {
        return hasPrevious() ? currentPage - 1 : 1;
    }

    public int getNextPage() {
        return hasNext() ? currentPage + 1 : totalPages;
    }

    public boolean isEmpty() {
        return items == null || items.isEmpty();
    }
}
