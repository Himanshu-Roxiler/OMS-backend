package com.roxiler.erp.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import com.google.gson.Gson;
import com.roxiler.erp.interfaces.ApprovedLeaveBreakup;
import com.roxiler.erp.interfaces.LeaveBreakup;
import com.roxiler.erp.model.LeavesTracker;
import com.roxiler.erp.model.Users;
import com.roxiler.erp.repository.LeavesTrackerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.roxiler.erp.dto.auth.UserDto;
import com.roxiler.erp.dto.holiday.CreateHolidayDto;
import com.roxiler.erp.dto.holiday.UpdateHolidayDto;
import com.roxiler.erp.model.Holiday;
import com.roxiler.erp.model.Organization;
import com.roxiler.erp.repository.HolidayRepository;
import com.roxiler.erp.repository.OrganizationRepository;
import com.roxiler.erp.repository.UsersRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class HolidayService {

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private OrganizationRepository organizationRepository;

    @Autowired
    private HolidayRepository holidayRepository;

    @Autowired
    private LeavesTrackerRepository leavesTrackerRepository;

    public Iterable<Holiday> getAllHolidays() {
        return holidayRepository.findAll();
    }

    public Iterable<Holiday> getHolidayListFromOrg(UserDto userDto) {
        Optional<Organization> org = organizationRepository.findById(userDto.getOrgId());

        if (org.isEmpty()) {
            throw new EntityNotFoundException("Organization is not found");
        } else {
            return holidayRepository.getHolidaysFromOrg(org.get());
        }
    }

    public void createHolidaysOnOrgCreation(UserDto userDto) {
        CreateHolidayDto createHolidayDto = new CreateHolidayDto();

        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            String inputDate = "2023-10-02";
            Date date = inputFormat.parse(inputDate);
            createHolidayDto.setHolidayDate(date);
            createHolidayDto.setHolidayName("Gandhi Jayanti");
            createHolidayDto.setHolidayDescription("Birth anniversary of Mahatma Gandhi");
            saveHoliday(createHolidayDto, userDto);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        try {
            String inputDate = "2023-10-24";
            Date date = inputFormat.parse(inputDate);
            createHolidayDto.setHolidayDate(date);
            createHolidayDto.setHolidayName("Dussehra");
            createHolidayDto.setHolidayDescription("Celebration of victory of good over evil and end of Navratri");
            saveHoliday(createHolidayDto, userDto);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        try {
            String inputDate = "2023-12-25";
            Date date = inputFormat.parse(inputDate);
            createHolidayDto.setHolidayDate(date);
            createHolidayDto.setHolidayName("Christmas");
            createHolidayDto.setHolidayDescription("Birthday of Jesus Christ");
            saveHoliday(createHolidayDto, userDto);
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    public Holiday saveHoliday(CreateHolidayDto createHolidayDto, UserDto userDto) {
        Optional<Organization> org = organizationRepository.findById(userDto.getOrgId());

        if (org.isEmpty()) {
            throw new EntityNotFoundException("Organization is not found");
        } else {
            Holiday holiday = new Holiday();
            holiday.setHolidayDate(createHolidayDto.getHolidayDate());
            holiday.setHolidayName(createHolidayDto.getHolidayName());
            holiday.setHolidayDescription(createHolidayDto.getHolidayDescription());
            holiday.setOrganization(org.get());
            org.get().getHolidays().add(holiday);
            organizationRepository.save(org.get());
            return holidayRepository.save(holiday);
        }
    }

    public Holiday updateHoliday(UpdateHolidayDto updateHolidayDto, Integer id) {
        Optional<Holiday> holidayOptional = holidayRepository.findById(id);

        if (holidayOptional.isEmpty()) {
            throw new EntityNotFoundException("Holiday not found");
        } else {
            Holiday holiday = holidayOptional.get();
            holiday.setHolidayDate(updateHolidayDto.getHolidayDate());
            holiday.setHolidayName(updateHolidayDto.getHolidayName());
            holiday.setHolidayDescription(updateHolidayDto.getHolidayDescription());
            return holidayRepository.save(holiday);
        }
    }

    public void deleteHoliday(Integer id) {
        Optional<Holiday> holidayOptional = holidayRepository.findById(id);

        if (holidayOptional.isEmpty()) {
            throw new EntityNotFoundException("Holiday not found");
        } else {
            // Optional<Organization> orgOptional = organizationRepository.findById(holidayOptional.get().getOrganization().getId());
            // orgOptional.get().getHolidays().remove(holidayOptional.get());
            // organizationRepository.save(orgOptional.get());
            holidayRepository.deleteById(id);
        }
    }

    public Iterable<Map<String, Object>> upcomingLeavesAndHolidays(UserDto userDto) {

        Users user = usersRepository.readByEmail(userDto.getEmail());
        Iterable<Holiday> holidays = holidayRepository.findUpcomingHolidaysInOrg(user.getOrganization());
        Iterable<LeavesTracker> leavesTrackers = leavesTrackerRepository.findUpcomingLeaves(user.getId());
        List<Map<String, Object>> upcomingLeavesAndHolidays = new ArrayList<>();
        for (Holiday holiday : holidays) {
            Map<String, Object> obj = new HashMap<>();
            SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd yyyy");
            dateFormat.setTimeZone(TimeZone.getTimeZone("IST"));
            Date date = new Date(holiday.getHolidayDate().getTime());
            String formattedDate = dateFormat.format(date);
            obj.put("date", formattedDate);
            obj.put("normalizedDate", holiday.getHolidayDate().getTime());
            obj.put("reason", holiday.getHolidayName());
            obj.put("isLeave", false);
            upcomingLeavesAndHolidays.add(obj);
        }
        for (LeavesTracker leavesTracker : leavesTrackers) {

            Gson gson = new Gson();
            if (leavesTracker.getIsApproved() != null && leavesTracker.getIsApproved()) {
                ApprovedLeaveBreakup[] approvedLeaveBreakups = gson.fromJson(leavesTracker.getApprovedLeaveBreakups(), ApprovedLeaveBreakup[].class);
                for (ApprovedLeaveBreakup approvedLeaveBreakup : approvedLeaveBreakups) {
                    Map<String, Object> obj = new HashMap<>();
                    Long startDate = approvedLeaveBreakup.getStartDate().getTime();
                    Date currDate = new Date();
                    if (startDate > currDate.getTime()) {
                        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd yyyy");
                        dateFormat.setTimeZone(TimeZone.getTimeZone("IST"));
                        Date date = new Date(approvedLeaveBreakup.getStartDate().getTime());
                        String formattedDate = dateFormat.format(date);
                        obj.put("date", formattedDate);
                        obj.put("reason", "Approved Leave");
                        obj.put("normalizedDate", approvedLeaveBreakup.getStartDate().getTime());
                        obj.put("isLeave", true);
                        obj.put("leaveRequestId", leavesTracker.getId());
                        upcomingLeavesAndHolidays.add(obj);
                    }
                }
            } else {
                LeaveBreakup[] leaveBreakups = gson.fromJson(leavesTracker.getLeaveBreakups(), LeaveBreakup[].class);
                for (LeaveBreakup leaveBreakup : leaveBreakups) {
                    Map<String, Object> obj = new HashMap<>();
                    Long startDate = leaveBreakup.getStartDate().getTime();
                    Date currDate = new Date();
                    if (startDate > currDate.getTime()) {
                        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd yyyy");
                        dateFormat.setTimeZone(TimeZone.getTimeZone("IST"));
                        Date date = new Date(leaveBreakup.getStartDate().getTime());
                        String formattedDate = dateFormat.format(date);
                        obj.put("date", formattedDate);
                        obj.put("reason", "Leave Approval Pending");
                        obj.put("normalizedDate", leaveBreakup.getStartDate().getTime());
                        obj.put("isLeave", true);
                        obj.put("leaveRequestId", leavesTracker.getId());
                        upcomingLeavesAndHolidays.add(obj);
                    }
                }
            }
        }

        upcomingLeavesAndHolidays.sort((map1, map2) -> {
            long epoch1 = Long.parseLong(map1.get("normalizedDate").toString());
            long epoch2 = Long.parseLong(map2.get("normalizedDate").toString());

            return Long.compare(epoch1, epoch2);
        });

        return upcomingLeavesAndHolidays;
    }

    public Iterable<Map<String, Object>> upcomingHolidays(UserDto userDto) {

        Users user = usersRepository.readByEmail(userDto.getEmail());
        Iterable<Holiday> holidays = holidayRepository.findUpcomingHolidaysInOrg(user.getOrganization());
        List<Map<String, Object>> upcomingHolidays = new ArrayList<>();
        for (Holiday holiday : holidays) {
            Map<String, Object> obj = new HashMap<>();
            SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd yyyy");
            dateFormat.setTimeZone(TimeZone.getTimeZone("IST"));
            Date date = new Date(holiday.getHolidayDate().getTime());
            String formattedDate = dateFormat.format(date);
            obj.put("date", formattedDate);
            obj.put("normalizedDate", holiday.getHolidayDate().getTime());
            obj.put("reason", holiday.getHolidayName());
            obj.put("isLeave", false);
            upcomingHolidays.add(obj);
        }

        upcomingHolidays.sort((map1, map2) -> {
            long epoch1 = Long.parseLong(map1.get("normalizedDate").toString());
            long epoch2 = Long.parseLong(map2.get("normalizedDate").toString());

            return Long.compare(epoch1, epoch2);
        });

        return upcomingHolidays;
    }

    public Iterable<Map<String, Object>> upcomingLeaves(UserDto userDto) {

        Users user = usersRepository.readByEmail(userDto.getEmail());
        Iterable<LeavesTracker> leavesTrackers = leavesTrackerRepository.findUpcomingLeaves(user.getId());
        List<Map<String, Object>> upcomingLeavesAndHolidays = new ArrayList<>();
        for (LeavesTracker leavesTracker : leavesTrackers) {

            Gson gson = new Gson();
            if (leavesTracker.getIsApproved() != null && leavesTracker.getIsApproved()) {
                ApprovedLeaveBreakup[] approvedLeaveBreakups = gson.fromJson(leavesTracker.getApprovedLeaveBreakups(), ApprovedLeaveBreakup[].class);
                for (ApprovedLeaveBreakup approvedLeaveBreakup : approvedLeaveBreakups) {
                    Map<String, Object> obj = new HashMap<>();
                    Long startDate = approvedLeaveBreakup.getStartDate().getTime();
                    Date currDate = new Date();
                    if (startDate > currDate.getTime()) {
                        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd yyyy");
                        dateFormat.setTimeZone(TimeZone.getTimeZone("IST"));
                        Date date = new Date(approvedLeaveBreakup.getStartDate().getTime());
                        String formattedDate = dateFormat.format(date);
                        obj.put("date", formattedDate);
                        obj.put("reason", "Approved Leave");
                        obj.put("normalizedDate", approvedLeaveBreakup.getStartDate().getTime());
                        obj.put("isLeave", true);
                        obj.put("leaveRequestId", leavesTracker.getId());
                        upcomingLeavesAndHolidays.add(obj);
                    }
                }
            } else {
                LeaveBreakup[] leaveBreakups = gson.fromJson(leavesTracker.getLeaveBreakups(), LeaveBreakup[].class);
                for (LeaveBreakup leaveBreakup : leaveBreakups) {
                    Map<String, Object> obj = new HashMap<>();
                    Long startDate = leaveBreakup.getStartDate().getTime();
                    Date currDate = new Date();
                    if (startDate > currDate.getTime()) {
                        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd yyyy");
                        dateFormat.setTimeZone(TimeZone.getTimeZone("IST"));
                        Date date = new Date(leaveBreakup.getStartDate().getTime());
                        String formattedDate = dateFormat.format(date);
                        obj.put("date", formattedDate);
                        obj.put("reason", "Leave Approval Pending");
                        obj.put("normalizedDate", leaveBreakup.getStartDate().getTime());
                        obj.put("isLeave", true);
                        obj.put("leaveRequestId", leavesTracker.getId());
                        upcomingLeavesAndHolidays.add(obj);
                    }
                }
            }
        }

        upcomingLeavesAndHolidays.sort((map1, map2) -> {
            long epoch1 = Long.parseLong(map1.get("normalizedDate").toString());
            long epoch2 = Long.parseLong(map2.get("normalizedDate").toString());

            return Long.compare(epoch1, epoch2);
        });

        return upcomingLeavesAndHolidays;
    }
}
