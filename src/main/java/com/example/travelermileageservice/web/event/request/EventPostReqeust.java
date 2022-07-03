package com.example.travelermileageservice.web.event.request;

import com.example.travelermileageservice.web.event.EventActionType;
import com.example.travelermileageservice.web.event.EventType;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

@Data
public class EventPostReqeust {

    private EventType type;
    private EventActionType action;
    @NotNull
    private UUID reviewId;
    private String content;
    private List<UUID> attachedPhotoIds;
    private UUID userId;
    private UUID placeId;
}
