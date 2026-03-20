package com.rasteronelab.lis.core.api;

import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Paginated response wrapper for list endpoints.
 *
 * Usage:
 *   Page<Patient> page = repository.findAll(pageable);
 *   PagedResponse<PatientResponse> response = PagedResponse.of(page.map(mapper::toResponse));
 */
public class PagedResponse<T> {

    private List<T> content;
    private int page;
    private int size;
    private long totalElements;
    private int totalPages;
    private boolean first;
    private boolean last;

    public static <T> PagedResponse<T> of(Page<T> page) {
        return PagedResponse.<T>builder()
                .content(page.getContent())
                .page(page.getNumber())
                .size(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .first(page.isFirst())
                .last(page.isLast())
                .build();
    }

    public PagedResponse() {
    }

    public PagedResponse(List<T> content, int page, int size, long totalElements, int totalPages, boolean first, boolean last) {
        this.content = content;
        this.page = page;
        this.size = size;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
        this.first = first;
        this.last = last;
    }

    public List<T> getContent() {
        return this.content;
    }

    public int getPage() {
        return this.page;
    }

    public int getSize() {
        return this.size;
    }

    public long getTotalElements() {
        return this.totalElements;
    }

    public int getTotalPages() {
        return this.totalPages;
    }

    public boolean isFirst() {
        return this.first;
    }

    public boolean isLast() {
        return this.last;
    }

    public void setContent(List<T> content) {
        this.content = content;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public void setTotalElements(long totalElements) {
        this.totalElements = totalElements;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public void setFirst(boolean first) {
        this.first = first;
    }

    public void setLast(boolean last) {
        this.last = last;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PagedResponse that = (PagedResponse) o;
        return java.util.Objects.equals(this.content, that.content) &&
               java.util.Objects.equals(this.page, that.page) &&
               java.util.Objects.equals(this.size, that.size) &&
               java.util.Objects.equals(this.totalElements, that.totalElements) &&
               java.util.Objects.equals(this.totalPages, that.totalPages) &&
               java.util.Objects.equals(this.first, that.first) &&
               java.util.Objects.equals(this.last, that.last);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(this.content, this.page, this.size, this.totalElements, this.totalPages, this.first, this.last);
    }

    @Override
    public String toString() {
        return "PagedResponse{content=" + content +
               ", page=" + page +
               ", size=" + size +
               ", totalElements=" + totalElements +
               ", totalPages=" + totalPages +
               ", first=" + first +
               ", last=" + last +
               "}";
    }

    public static <T> PagedResponseBuilder<T> builder() {
        return new PagedResponseBuilder<>();
    }

    public static class PagedResponseBuilder<T> {
        private List<T> content;
        private int page;
        private int size;
        private long totalElements;
        private int totalPages;
        private boolean first;
        private boolean last;

        PagedResponseBuilder() {
        }

        public PagedResponseBuilder<T> content(List<T> content) {
            this.content = content;
            return this;
        }

        public PagedResponseBuilder<T> page(int page) {
            this.page = page;
            return this;
        }

        public PagedResponseBuilder<T> size(int size) {
            this.size = size;
            return this;
        }

        public PagedResponseBuilder<T> totalElements(long totalElements) {
            this.totalElements = totalElements;
            return this;
        }

        public PagedResponseBuilder<T> totalPages(int totalPages) {
            this.totalPages = totalPages;
            return this;
        }

        public PagedResponseBuilder<T> first(boolean first) {
            this.first = first;
            return this;
        }

        public PagedResponseBuilder<T> last(boolean last) {
            this.last = last;
            return this;
        }

        public PagedResponse<T> build() {
            return new PagedResponse<>(this.content, this.page, this.size, this.totalElements, this.totalPages, this.first, this.last);
        }
    }

}
