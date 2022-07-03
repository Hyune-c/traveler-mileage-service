package com.example.travelermileageservice.domain.review.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor(staticName = "of")
public class ReviewAddDto {

    @NotNull
    private UUID reviewId;
    @NotNull
    private String content;
    @NotNull
    private List<UUID> attachedPhotoIds;
    @NotNull
    private UUID userId;
    @NotNull
    private UUID placeId;
}
