package com.rasteronelab.lis.core.api;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("PagedResponse")
class PagedResponseTest {

    @Test
    @DisplayName("of() should map Spring Page to PagedResponse with correct fields")
    void of_shouldMapPageToPagedResponse() {
        List<String> content = List.of("item1", "item2", "item3");
        PageRequest pageable = PageRequest.of(0, 10);
        Page<String> page = new PageImpl<>(content, pageable, 25);

        PagedResponse<String> response = PagedResponse.of(page);

        assertThat(response.getContent()).containsExactly("item1", "item2", "item3");
        assertThat(response.getPage()).isZero();
        assertThat(response.getSize()).isEqualTo(10);
        assertThat(response.getTotalElements()).isEqualTo(25);
        assertThat(response.getTotalPages()).isEqualTo(3);
        assertThat(response.isFirst()).isTrue();
        assertThat(response.isLast()).isFalse();
    }

    @Test
    @DisplayName("of() should map empty page correctly")
    void of_shouldMapEmptyPage() {
        PageRequest pageable = PageRequest.of(0, 10);
        Page<String> page = new PageImpl<>(Collections.emptyList(), pageable, 0);

        PagedResponse<String> response = PagedResponse.of(page);

        assertThat(response.getContent()).isEmpty();
        assertThat(response.getPage()).isZero();
        assertThat(response.getSize()).isEqualTo(10);
        assertThat(response.getTotalElements()).isZero();
        assertThat(response.getTotalPages()).isZero();
        assertThat(response.isFirst()).isTrue();
        assertThat(response.isLast()).isTrue();
    }

    @Test
    @DisplayName("of() should correctly identify last page")
    void of_shouldIdentifyLastPage() {
        List<String> content = List.of("item1");
        PageRequest pageable = PageRequest.of(2, 10);
        Page<String> page = new PageImpl<>(content, pageable, 21);

        PagedResponse<String> response = PagedResponse.of(page);

        assertThat(response.getPage()).isEqualTo(2);
        assertThat(response.isFirst()).isFalse();
        assertThat(response.isLast()).isTrue();
    }
}
