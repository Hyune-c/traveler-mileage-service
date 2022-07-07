package com.example.travelermileageservice.web.event;

import com.example.travelermileageservice.config.exception.BusinessException;
import com.example.travelermileageservice.config.exception.ErrorCode;
import com.example.travelermileageservice.domain.review.service.ReviewAddService;
import com.example.travelermileageservice.domain.review.service.ReviewDeleteService;
import com.example.travelermileageservice.domain.review.service.ReviewModService;
import com.example.travelermileageservice.domain.review.service.dto.ReviewAddDto;
import com.example.travelermileageservice.domain.review.service.dto.ReviewModDto;
import com.example.travelermileageservice.web.event.request.EventPostReqeust;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
public class EventController {

    private final ReviewAddService reviewAddService;
    private final ReviewModService reviewModService;
    private final ReviewDeleteService reviewDeleteService;

    @PostMapping("/events")
    public UUID event(@RequestBody @Valid final EventPostReqeust reqeust) {
        switch (reqeust.getAction()) {
            case ADD:
                final ReviewAddDto dto = ReviewAddDto.of(reqeust.getReviewId(),
                        reqeust.getContent(), reqeust.getAttachedPhotoIds(), reqeust.getUserId(), reqeust.getPlaceId());
                return reviewAddService.add(dto);
            case MOD:
                return reviewModService.mod(ReviewModDto.of(reqeust.getReviewId(), reqeust.getContent(), reqeust.getAttachedPhotoIds()));
            case DELETE:
                reviewDeleteService.delete(reqeust.getReviewId());
                return null;
            default:
                throw new BusinessException(ErrorCode.NOT_FOUND);
        }
    }
}
