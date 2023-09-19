package com.roxiler.erp.service;

import java.util.Optional;

import com.roxiler.erp.constants.TypeOfLeaveConstants;
import com.roxiler.erp.model.Designation;
import com.roxiler.erp.model.LeavesSystem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.firewall.RequestRejectedException;
import org.springframework.stereotype.Service;

import com.roxiler.erp.dto.auth.UserDto;
import com.roxiler.erp.model.Leaves;
import com.roxiler.erp.model.Users;
import com.roxiler.erp.repository.LeavesRepository;
import com.roxiler.erp.repository.UsersRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
//@Transactional
public class LeaveService {

    @Autowired
    private LeavesRepository leavesRepository;

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private LeaveSystemService leaveSystemService;


    public Leaves saveLeaves(UserDto userDto, Leaves leaves) {
        Optional<Users> user = usersRepository.findById(userDto.getId());
        if (user.isPresent()) {
            leaves.setUser(user.get());
        } else {
            throw new EntityNotFoundException("User is not found" + userDto.getId());
        }
        Leaves newLeaves = leavesRepository.save(leaves);
        user.get().setUserLeaves(newLeaves);
        usersRepository.save(user.get());
        return newLeaves;
    }

    public Leaves getUserLeaves(UserDto userDto) {
        Users user = usersRepository.readByEmail(userDto.getEmail());
        System.out.println("1");
        Leaves leaves = leavesRepository.readByUser(user);
        System.out.println("LEAVE\n" + leaves);
        return leaves;
    }

    public void deleteLeaves(Integer id) {
        Optional<Leaves> Leaves = leavesRepository.findById(id);
        if (Leaves.isEmpty()) {
            throw new EntityNotFoundException("Leaves " + id + " does not exist");
        }
        if (Leaves.isPresent()) {
            leavesRepository.deleteById(id);
        }
    }

    public Leaves updatLeaves(Leaves updateLeaves, Integer id) {
        Optional<Leaves> optionalLeaves = leavesRepository.findById(id);

        if (optionalLeaves.isEmpty()) {
            throw new EntityNotFoundException("Leaves " + id + " does not exist");
        }

        Leaves existingLeaves = optionalLeaves.get();
        existingLeaves.setApprovedLeaves(updateLeaves.getApprovedLeaves());
        existingLeaves.setAvailablePaidLeaves(updateLeaves.getAvailablePaidLeaves());
        existingLeaves.setAvailableUnpaidLeaves(updateLeaves.getAvailableUnpaidLeaves());
        existingLeaves.setAvailableSickLeaves(updateLeaves.getAvailableSickLeaves());
        existingLeaves.setAvailableVacationLeaves(updateLeaves.getAvailableVacationLeaves());
        existingLeaves.setTotalLeaves(updateLeaves.getTotalLeaves());
        if (updateLeaves.getUser() != null) {
            Optional<Users> user = usersRepository.findById(id);
            existingLeaves.setUser(user.get());
        }
        return leavesRepository.save(existingLeaves);

    }

    public Leaves getLeavesById(Integer id) {
        Optional<Leaves> optionalLeaves = leavesRepository.findById(id);
        if (optionalLeaves.isEmpty()) {
            throw new EntityNotFoundException("Leaves " + id + " does not exist");
        }
        return optionalLeaves.get();
    }

    public void createLeaveOnUserCreation(Users user) {

        Leaves leaves = new Leaves();
        LeavesSystem leavesSystem = leaveSystemService.getLeaveSystemByDesignation(user.getDesignation());
        leaves.setApprovedLeaves(0f);
        leaves.setBookedLeaves(0f);
        Float accrual = leavesSystem.getAccrual();
        leaves.setAvailablePaidLeaves(accrual);
        leaves.setAvailableUnpaidLeaves(10f);
        leaves.setAvailableSickLeaves(5f);
        leaves.setTotalLeaves(accrual);
        leaves.setUser(user);

        leavesRepository.save(leaves);
    }

    public void updateLeavesUponRequest(Float noOfDays, Users user, String typeOfLeave) {

        System.out.println("\nUPDATING\n");
        Optional<Leaves> leaves = leavesRepository.findByUser(user);
        if (leaves.isPresent()) {
            if (typeOfLeave.equals(TypeOfLeaveConstants.PAID_LEAVE)) {
                Float availableLeaves = leaves.get().getAvailablePaidLeaves() - noOfDays;
                if (availableLeaves < 0f) {
                    throw new RequestRejectedException("You don't have enough paid leaves available for the application");
                }
                leaves.get().setAvailablePaidLeaves(availableLeaves);
                leaves.get().setBookedLeaves(noOfDays);
                leavesRepository.save(leaves.get());
            } else if (typeOfLeave.equals(TypeOfLeaveConstants.UNPAID_LEAVE)) {
                Float availableLeaves = leaves.get().getAvailableUnpaidLeaves() - noOfDays;
                if (availableLeaves < 0f) {
                    throw new RequestRejectedException("You don't have enough unpaid leaves available for the application");
                }
                leaves.get().setAvailableUnpaidLeaves(availableLeaves);
                leaves.get().setBookedLeaves(noOfDays);
                leavesRepository.save(leaves.get());
            } else if (typeOfLeave.equals(TypeOfLeaveConstants.SICK_LEAVE)) {
                Float availableLeaves = leaves.get().getAvailableSickLeaves() - noOfDays;
                if (availableLeaves < 0f) {
                    throw new RequestRejectedException("You don't have enough sick leaves available for the application");
                }
                leaves.get().setAvailableSickLeaves(availableLeaves);
                leaves.get().setBookedLeaves(noOfDays);
                leavesRepository.save(leaves.get());
            } else if (typeOfLeave.equals(TypeOfLeaveConstants.VACATION_LEAVE)) {
                Float availableLeaves = leaves.get().getAvailableVacationLeaves() - noOfDays;
                if (availableLeaves < 0f) {
                    throw new RequestRejectedException("You don't have enough vacation leaves available for the application");
                }
                leaves.get().setAvailableVacationLeaves(availableLeaves);
                leaves.get().setBookedLeaves(noOfDays);
                leavesRepository.save(leaves.get());
            }
        } else {
            throw new EntityNotFoundException("No leave record found");
        }

    }

    public void updatesLeaveUponApproval(Float noOfDays, Float approvedDays, Integer userId, String typeOfLeave) {

        Optional<Users> user = usersRepository.findById(userId);
        if (user.isEmpty()) {
            throw new EntityNotFoundException("User not found");
        }
        Optional<Leaves> leaves = leavesRepository.findByUser(user.get());
        if (leaves.isPresent()) {
            if (typeOfLeave.equals(TypeOfLeaveConstants.PAID_LEAVE)) {
                Float daysToBeAdded = noOfDays - approvedDays;
                leaves.get().setAvailablePaidLeaves(leaves.get().getAvailablePaidLeaves() + daysToBeAdded);
                leaves.get().setBookedLeaves(leaves.get().getBookedLeaves() - daysToBeAdded);
                leavesRepository.save(leaves.get());
            } else if (typeOfLeave.equals(TypeOfLeaveConstants.UNPAID_LEAVE)) {
                Float daysToBeAdded = noOfDays - approvedDays;
                leaves.get().setAvailableUnpaidLeaves(leaves.get().getAvailablePaidLeaves() + daysToBeAdded);
                leaves.get().setBookedLeaves(leaves.get().getBookedLeaves() - daysToBeAdded);
                leavesRepository.save(leaves.get());
            } else if (typeOfLeave.equals(TypeOfLeaveConstants.SICK_LEAVE)) {
                Float daysToBeAdded = noOfDays - approvedDays;
                leaves.get().setAvailableSickLeaves(leaves.get().getAvailablePaidLeaves() + daysToBeAdded);
                leaves.get().setBookedLeaves(leaves.get().getBookedLeaves() - daysToBeAdded);
                leavesRepository.save(leaves.get());
            } else if (typeOfLeave.equals(TypeOfLeaveConstants.VACATION_LEAVE)) {
                Float daysToBeAdded = noOfDays - approvedDays;
                leaves.get().setAvailableVacationLeaves(leaves.get().getAvailablePaidLeaves() + daysToBeAdded);
                leaves.get().setBookedLeaves(leaves.get().getBookedLeaves() - daysToBeAdded);
                leavesRepository.save(leaves.get());
            }
        } else {
            throw new EntityNotFoundException("No leave record found");
        }
    }
}
