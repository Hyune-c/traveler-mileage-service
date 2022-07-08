package com.example.travelermileageservice.web.point;

import com.example.travelermileageservice.domain.point.service.PointGetService;
import com.example.travelermileageservice.web.point.response.PointGetResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RequiredArgsConstructor
@RestController
public class PointController {

    private final PointGetService pointGetService;

    @GetMapping("/point")
    public PointGetResponse get(@RequestParam final UUID userId) {
        return PointGetResponse.of(userId, pointGetService.get(userId));
    }
}
