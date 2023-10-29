package com.meeting.planner.meetingplannerms.service;

import com.meeting.planner.meetingplannerms.converter.MeetingConverter;
import com.meeting.planner.meetingplannerms.converter.MeetingDtoConverter;
import com.meeting.planner.meetingplannerms.dto.MeetingDto;
import com.meeting.planner.meetingplannerms.entity.Meeting;
import com.meeting.planner.meetingplannerms.entity.Room;
import com.meeting.planner.meetingplannerms.repository.MeetingRepository;
import com.meeting.planner.meetingplannerms.repository.RoomRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@AllArgsConstructor
public class MeetingService {

    private final RoomRepository roomRepository;
    private final MeetingRepository meetingRepository;


    public MeetingDto createMeeting(MeetingDto meetingDto) {

        //première  filtre pour déterminer la liste des salles disponible selon (jour, heure, capacité)

        List<Room> availableRooms = roomRepository.getAvailableRooms(meetingDto.getDay(), meetingDto.getStartTime().minusHours(1), meetingDto.getStartTime(), meetingDto.getStartTime().plusHours(1), meetingDto.getNumberPersons());
        if (availableRooms.isEmpty() ||meetingDto.getStartTime().getHour() < 8 || meetingDto.getStartTime().getHour() > 20) return null;

        List<String> vcEquipements = List.of("WEBCAM", "ECRAN", "PIEUVRE");
        List<String> rcEquipements = List.of("TABLEAU", "ECRAN", "PIEUVRE");
        List<String> specEquipements = List.of("TABLEAU");

        //deuxième filtre pour choisir la bonne salle selon type de reunion(équipements)

        Room room = null;
        switch (meetingDto.getTypeMeeting()) {

            case VC:
                room = getBestRoom(vcEquipements, availableRooms, meetingDto);
                break;
            case RS:
                room = getBestRoom(new ArrayList<>(), availableRooms, meetingDto);
                break;
            case RC:
                room = getBestRoom(rcEquipements, availableRooms, meetingDto);
                break;
            case SPEC:
                room = getBestRoom(specEquipements, availableRooms, meetingDto);
                break;
        }

        if (Objects.isNull(room)) {

            return null;
        } else {

            // the convert() method is a non-static method on the MeetingDtoConverter class. This means that you need
            // to create an instance of the MeetingDtoConverter class before you can call the convert() method.
            MeetingDtoConverter meetingDtoConverter = new MeetingDtoConverter();

            Meeting meeting = meetingDtoConverter.convert(meetingDto);
            meeting.setRoom(room);
            Meeting savedMeeting = meetingRepository.save(meeting);
            MeetingConverter MeetingConverter = new MeetingConverter();
            return MeetingConverter.convert(savedMeeting);
        }

    }



    public Room getBestRoom(List<String> equipments, List<Room> availableRooms, MeetingDto meetingDto) {
        Room bestRoom = null;
        int bestScore = Integer.MIN_VALUE;

        for (Room room : availableRooms) {
            int capacityScore = calculateCapacityScore(room, meetingDto.getNumberPersons());
            int equipmentScore = calculateEquipmentScore(room, equipments);

            int combinedScore = capacityScore + equipmentScore;

            if (capacityScore >= 0 && equipmentScore >= 0 && combinedScore > bestScore) {
                bestRoom = room;
                bestScore = combinedScore;
            }
        }

        return bestRoom;
    }

    private int calculateCapacityScore(Room room, int requiredCapacity) {
        return room.getCapacity() - requiredCapacity;
    }

    private int calculateEquipmentScore(Room room, List<String> requiredEquipments) {
        int equipmentScore = 0;

        for (String equipment : requiredEquipments) {
            if (room.getEquipmentsAsList().contains(equipment)) {
                equipmentScore++;
            } else {
                equipmentScore--;
            }
        }

        return equipmentScore;
    }



}