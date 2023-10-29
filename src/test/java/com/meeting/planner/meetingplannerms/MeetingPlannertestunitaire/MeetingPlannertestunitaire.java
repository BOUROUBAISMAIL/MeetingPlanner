package com.meeting.planner.meetingplannerms.MeetingPlannertestunitaire;

import com.meeting.planner.meetingplannerms.dto.MeetingDto;
import com.meeting.planner.meetingplannerms.dto.RoomDto;
import com.meeting.planner.meetingplannerms.entity.MeetingType;
import com.meeting.planner.meetingplannerms.entity.Room;
import com.meeting.planner.meetingplannerms.repository.MeetingRepository;
import com.meeting.planner.meetingplannerms.repository.RoomRepository;
import com.meeting.planner.meetingplannerms.service.MeetingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;

import java.util.List;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)


public class MeetingPlannertestunitaire {

    @Mock
    private RoomRepository roomRepository;

    @Mock
    private MeetingRepository meetingRepository;



    private MeetingService meetingService;
    // meetingService=new MeetingService(roomRepository,meetingRepository);

    @BeforeEach
    void setUp() {
        meetingService = new MeetingService(roomRepository,meetingRepository);
    }



    @Test
    public void testCreateMeeting_WhenRoomIsAvailable() {
        // Arrange
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

        Room room = new Room();

        when(roomRepository.getAvailableRooms(meetingDto.getDay(), meetingDto.getStartTime().minusHours(1), meetingDto.getStartTime(), meetingDto.getStartTime().plusHours(1), meetingDto.getNumberPersons()))
                .thenReturn(List.of(room));

        // Act
        MeetingDto savedMeetingdto= meetingService.createMeeting(meetingDto);

        assertNotNull(savedMeetingdto);
    }
}
