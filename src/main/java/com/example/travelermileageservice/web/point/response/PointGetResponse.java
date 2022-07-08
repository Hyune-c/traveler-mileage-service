package com.example.travelermileageservice.web.point.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@AllArgsConstructor(staticName = "of")
@Data
public class PointGetResponse {

    private final UUID userId;
    private final Integer point;
}
