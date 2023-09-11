package com.roxiler.erp.service;

import java.util.Optional;

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

    public Iterable<Holiday> getAllHolidays() {
        return holidayRepository.findAll();
    }

    public Iterable<Holiday> getHolidayListFromOrg(UserDto userDto) {
        Optional<Organization> org = organizationRepository.findById(userDto.getOrgId());

        if(org.isEmpty()) {
            throw new EntityNotFoundException("Organization is not found");
        } else {
            return holidayRepository.getHolidaysFromOrg(org.get());
        }
    }

    public Holiday saveHoliday(CreateHolidayDto createHolidayDto, UserDto userDto) {
        Optional<Organization> org = organizationRepository.findById(userDto.getOrgId());

        if(org.isEmpty()) {
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

        if(holidayOptional.isEmpty()) {
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

        if(holidayOptional.isEmpty()) {
            throw new EntityNotFoundException("Holiday not found");
        } else {
            // Optional<Organization> orgOptional = organizationRepository.findById(holidayOptional.get().getOrganization().getId());
            // orgOptional.get().getHolidays().remove(holidayOptional.get());
            // organizationRepository.save(orgOptional.get());
            holidayRepository.deleteById(id);
        }
    }
}
