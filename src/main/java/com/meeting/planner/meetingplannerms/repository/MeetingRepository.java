package com.meeting.planner.meetingplannerms.repository;

import com.meeting.planner.meetingplannerms.entity.Meeting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MeetingRepository extends JpaRepository<Meeting, Long> {


}
