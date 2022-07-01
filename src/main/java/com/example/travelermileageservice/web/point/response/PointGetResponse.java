package com.example.travelermileageservice.web.point.response;

import lombok.Data;

import java.util.UUID;

@Data
public class PointGetResponse {

    private final UUID userId;
    private final Long point;
}
