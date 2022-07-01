package com.example.travelermileageservice.web.point;

import com.example.travelermileageservice.web.point.response.PointGetResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class PointController {

    @GetMapping("/point")
    public PointGetResponse get(@RequestParam final UUID userId) {
        return new PointGetResponse(userId, 10L);
    }
}
