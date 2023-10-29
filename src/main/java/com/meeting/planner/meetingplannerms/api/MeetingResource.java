package com.meeting.planner.meetingplannerms.api;

import com.meeting.planner.meetingplannerms.dto.MeetingDto;
import com.meeting.planner.meetingplannerms.service.MeetingService;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Objects;

@RestController
@AllArgsConstructor
@RequestMapping("/api/meetings")
public class MeetingResource {

    private final MeetingService meetingService;

    @PostMapping
    public ResponseEntity<MeetingDto> reserveRoom(@RequestBody MeetingDto meetingDto) {
        MeetingDto savedMeetingDto = meetingService.createMeeting(meetingDto);

        if (Objects.isNull(savedMeetingDto)) {
            System.out.printf("No room available or full reservation ");
            return ResponseEntity.notFound().build();
        }

        final URI location = ServletUriComponentsBuilder
                .fromCurrentServletMapping()
                .path("/{id}")
                .buildAndExpand(savedMeetingDto.getId())
                .toUri();

        return ResponseEntity.created(location).body(savedMeetingDto);
    }
}
