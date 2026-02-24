package com.skishop.web.dto;

/**
 * Data Transfer Object for product search requests.
 * Migrated from Struts ProductSearchForm to Spring Boot.
 */
public class ProductSearchRequest {

    private String keyword;
    private String categoryId;
    private String sort;
    private int page = 1;
    private int size = 10;

    public ProductSearchRequest() {
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page > 0 ? page : 1;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size > 0 ? size : 10;
    }
}
