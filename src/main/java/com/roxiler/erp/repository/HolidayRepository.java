package com.roxiler.erp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.roxiler.erp.model.Holiday;
import com.roxiler.erp.model.Organization;

public interface HolidayRepository extends JpaRepository<Holiday, Integer> {

    @Query("select holi from Holiday holi where holi.organization= :organization")
    Iterable<Holiday> getHolidaysFromOrg(Organization organization);

    @Query("SELECT holiday from Holiday holiday WHERE holiday.organization = :organization AND holiday.holidayDate > CURRENT_DATE")
    Iterable<Holiday> findUpcomingHolidaysInOrg(Organization organization);
}
