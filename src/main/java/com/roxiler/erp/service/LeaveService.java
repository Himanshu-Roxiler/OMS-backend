package com.roxiler.erp.service;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.roxiler.erp.dto.auth.UserDto;
import com.roxiler.erp.model.Leaves;
import com.roxiler.erp.model.Users;
import com.roxiler.erp.repository.LeavesRepository;
import com.roxiler.erp.repository.UsersRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class LeaveService {
    
    @Autowired
    private LeavesRepository leavesRequestRepository; 

    @Autowired
    private UsersRepository usersRepository;


    public Leaves saveLeaves(UserDto userDto, Leaves leaves) {  
        Optional<Users> user = usersRepository.findById(userDto.getId());
        if (user.isPresent()) {
            leaves.setUser(user.get());
        } else {
            throw new EntityNotFoundException("User is not found"+ userDto.getId());
        }
        Leaves newLeaves = leavesRequestRepository.save(leaves);
        user.get().setUserLeaves(newLeaves);
        usersRepository.save(user.get());
        return newLeaves;
    }

    public Iterable<Leaves> getAllLeavessIterable(UserDto userDto) {
        Users user = usersRepository.readByEmail(userDto.getEmail());
        return leavesRequestRepository.findAllByUser(user);
    }
    public void deleteLeaves(Integer id) {
        Optional<Leaves> Leaves = leavesRequestRepository.findById(id);
        if (Leaves.isEmpty()) {
            throw new EntityNotFoundException("Leaves " + id + " does not exist");
        }
        if(Leaves.isPresent()) {
            leavesRequestRepository.deleteById(id);
        }
    }

    public Leaves updatLeaves(Leaves updateLeaves, Integer id) {
        Optional<Leaves> optionalLeaves = leavesRequestRepository.findById(id);

        if (optionalLeaves.isEmpty()) {
            throw new EntityNotFoundException("Leaves " + id + " does not exist");
        }

        if (optionalLeaves.isPresent()) {
            Leaves existingLeaves = optionalLeaves.get();
            existingLeaves.setApprovedLeaves(updateLeaves.getApprovedLeaves());
            existingLeaves.setAvailableLeaves(updateLeaves.getAvailableLeaves());
            existingLeaves.setTotalLeaves(updateLeaves.getTotalLeaves());
            if (updateLeaves.getUser() != null) {
                Optional<Users> user = usersRepository.findById(id);
                existingLeaves.setUser(user.get());
            }
            return leavesRequestRepository.save(existingLeaves);
        }
        return updateLeaves;

    }

    public Leaves getLeavesById(Integer id) {
        Optional<Leaves> optionalLeaves = leavesRequestRepository.findById(id);
        if (optionalLeaves.isEmpty()) {
            throw new EntityNotFoundException("Leaves " + id + " does not exist");
        }
        return optionalLeaves.get();
    }


}
