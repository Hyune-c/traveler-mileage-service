package com.example.travelermileageservice.web.point;

import com.example.travelermileageservice.domain.point.service.PointGetService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PointController.class)
class PointControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PointGetService pointGetService;

    @DisplayName("포인트 조회 - 성공")
    @Nested
    class PointGetSuccess {

        private final String url = "/point";
        private final UUID userId = UUID.fromString("3ede0ef2-92b7-4817-a5f3-0c575361f745");

        @DisplayName("성공")
        @Test
        void ok() throws Exception {
            // given

            // when
            final ResultActions result = mockMvc.perform(
                    get(url).param("userId", userId.toString())
            );

            // then
            result.andExpect(status().is2xxSuccessful())
                    .andExpect(jsonPath("$.userId").value(userId.toString()))
                    .andExpect(jsonPath("$.point").isNumber());
        }

    }
}
