package io.glory.coremvc.response;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CollectionResponseJTest {

    @DisplayName("ofList")
    @Test
    void ofList() throws Exception {
        // given
        var content = List.of("a", "b", "c");

        // when
        var response = new CollectionResponse(content);

        // then
        System.out.println("==> response = " + response);
        assertThat(response).isNotNull()
                .extracting(
                        CollectionResponse::getContent,
                        CollectionResponse::getTotalCount
                )
                .containsExactly(
                        content,
                        content.size()
                );
    }

    @DisplayName("ofSet")
    @Test
    void ofSet() throws Exception {
        // given
        var content = new HashSet<>(List.of("a", "b", "c", "a"));

        // when
        var response = new CollectionResponse(content);

        // then
        System.out.println("==> response = " + response);
        assertThat(response).isNotNull()
                .extracting(
                        CollectionResponse::getContent,
                        CollectionResponse::getTotalCount
                )
                .containsExactly(
                        content,
                        content.size()
                );
    }

    @DisplayName("ofMap")
    @Test
    void ofMap() throws Exception {
        // given
        var content = Map.of("a", 1, "b", 2, "c", 3);

        // when
        var response = new CollectionResponse(content);

        // then
        System.out.println("==> response = " + response);
        assertThat(response).isNotNull()
                .extracting(
                        CollectionResponse::getContent,
                        CollectionResponse::getTotalCount
                )
                .containsExactly(
                        content,
                        content.size()
                );
    }

}
