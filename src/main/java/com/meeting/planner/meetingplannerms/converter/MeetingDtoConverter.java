package com.meeting.planner.meetingplannerms.converter;

import com.meeting.planner.meetingplannerms.dto.MeetingDto;
import com.meeting.planner.meetingplannerms.entity.Meeting;
import org.springframework.core.convert.converter.Converter;

import java.util.Objects;

public class MeetingDtoConverter implements Converter<MeetingDto, Meeting> {


    @Override

    public Meeting convert(MeetingDto meetingDto) {
        if (Objects.isNull(meetingDto)) {
            return null;
        }

        return Meeting.builder()
                .id(meetingDto.getId())
                .name(meetingDto.getName())
                .day(meetingDto.getDay())
                .numberPersons(meetingDto.getNumberPersons())
                .typeMeeting(meetingDto.getTypeMeeting())
                .startTime(meetingDto.getStartTime())
                .build();
    }
}
