package com.example.travelermileageservice.web.event;

import com.example.travelermileageservice.domain.review.service.ReviewAddService;
import com.example.travelermileageservice.domain.review.service.ReviewDeleteService;
import com.example.travelermileageservice.domain.review.service.ReviewModService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EventController.class)
class EventControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReviewAddService reviewAddService;

    @MockBean
    private ReviewModService reviewModService;

    @MockBean
    private ReviewDeleteService reviewDeleteService;

    @BeforeEach
    void beforeEach() {
        when(reviewAddService.add(any()))
                .thenReturn(UUID.randomUUID());
    }

    @DisplayName("리뷰 작성 - 성공")
    @Nested
    class ReviewAddSuccess {

        private final String url = "/events";

        @DisplayName("성공")
        @Test
        void ok() throws Exception {
            // given
            final String body = "{\n" +
                    "  \"type\": \"REVIEW\",\n" +
                    "  \"action\": \"ADD\",\n" +
                    "  \"reviewId\": \"240a0658-dc5f-4878-9381-ebb7b2667772\",\n" +
                    "  \"content\": \"좋아요!\",\n" +
                    "  \"attachedPhotoIds\": [\n" +
                    "    \"e4d1a64e-a531-46de-88d0-ff0ed70c0bb8\",\n" +
                    "    \"afb0cef2-851d-4a50-bb07-9cc15cbdc332\"\n" +
                    "  ],\n" +
                    "  \"userId\": \"3ede0ef2-92b7-4817-a5f3-0c575361f745\",\n" +
                    "  \"placeId\": \"2e4baf1c-5acb-4efb-a1af-eddada31b00f\"\n" +
                    "}";

            // when
            final ResultActions result = mockMvc.perform(
                    post(url)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(body)
            );

            // then
            result.andExpect(status().is2xxSuccessful());
        }

        @DisplayName("빈 첨부 사진")
        @Test
        void blankAttachedPicture() throws Exception {
            // given
            final String body = "{\n" +
                    "  \"type\": \"REVIEW\",\n" +
                    "  \"action\": \"ADD\",\n" +
                    "  \"reviewId\": \"240a0658-dc5f-4878-9381-ebb7b2667772\",\n" +
                    "  \"content\": \"좋아요!\",\n" +
                    "  \"attachedPhotoIds\": [],\n" +
                    "  \"userId\": \"3ede0ef2-92b7-4817-a5f3-0c575361f745\",\n" +
                    "  \"placeId\": \"2e4baf1c-5acb-4efb-a1af-eddada31b00f\"\n" +
                    "}";

            // when
            final ResultActions result = mockMvc.perform(
                    post(url)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(body)
            );

            // then
            result.andExpect(status().is2xxSuccessful());
        }
    }
}
