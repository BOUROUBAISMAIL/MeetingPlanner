package com.meeting.planner.meetingplannerms.EndtoEndtest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.meeting.planner.meetingplannerms.dto.MeetingDto;
import com.meeting.planner.meetingplannerms.dto.RoomDto;
import com.meeting.planner.meetingplannerms.entity.MeetingType;
import com.meeting.planner.meetingplannerms.repository.MeetingRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class MeetingPlannerEndtoEndtest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    MeetingRepository meetingRepository;

    @Test
    void reserveRoom() throws Exception {
        MeetingDto meetingDto = MeetingDto.builder()
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

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        String json = objectMapper.writeValueAsString(meetingDto);

        mockMvc.perform(post("/api/meetings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated());
    }
}
