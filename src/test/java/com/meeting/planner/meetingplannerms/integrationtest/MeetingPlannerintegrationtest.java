package com.meeting.planner.meetingplannerms.integrationtest;

import com.meeting.planner.meetingplannerms.dto.MeetingDto;
import com.meeting.planner.meetingplannerms.dto.RoomDto;
import com.meeting.planner.meetingplannerms.entity.Meeting;
import com.meeting.planner.meetingplannerms.entity.MeetingType;
import com.meeting.planner.meetingplannerms.repository.MeetingRepository;
import com.meeting.planner.meetingplannerms.service.MeetingService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;


import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureMockMvc
class MeetingPlannerintegrationtest {

    @Autowired
    MeetingService meetingService;

    @Autowired
    MeetingRepository meetingRepository;

    @Test
    void shouldAddStudentToDatabase() {
        // GIVEN
        MeetingDto meetingDto = MeetingDto.builder()
                .id(1L)
                .name("My Meeting")
                .startTime(LocalTime.of(10, 0))
                .typeMeeting(MeetingType.VC)
                .numberPersons(5)
                .day(LocalDate.of(2023, 10, 27))
                .roomDto(
                        RoomDto.builder()
                                .name("Conference Room A")
                                .capacity(10)
                                .equipments("WEBCAM, ECRAN, PIEUVRE")
                                .build()
                )
                .build();

        // WHEN
        meetingService.createMeeting(meetingDto);
        // THEN
        Meeting savedMeeting = meetingRepository.findById(meetingDto.getId()).get();
        assertEquals("My Meeting", meetingDto.getName());

    }
}
