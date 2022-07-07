package com.example.travelermileageservice.web.event.request;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

@Data
public class EventPostReqeust {

    @NotNull
    private EventType type;
    @NotNull
    private EventActionType action;
    @NotNull
    private UUID reviewId;
    private String content;
    private List<UUID> attachedPhotoIds;
    private UUID userId;
    private UUID placeId;
}
