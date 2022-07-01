package com.example.travelermileageservice.web.event;

import com.example.travelermileageservice.web.event.request.EventPostReqeust;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class EventController {

    @PostMapping("/events")
    public void event(@RequestBody @Valid final EventPostReqeust reqeust) {
        switch (reqeust.getAction()) {
            case ADD:
                break;
            case MOD:
                break;
            case DELETE:
                break;
        }
    }
}
