package com.roxiler.erp.service;

import com.roxiler.erp.model.UserProfile;
import com.roxiler.erp.repository.UserProfileRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserProfileService {

    @Autowired
    private UserProfileRepository userProfileRepository;

    public Iterable<UserProfile> getAllUsers() {
        Iterable<UserProfile> users = userProfileRepository.findAll();

        return users;
    }

    public UserProfile saveUser(UserProfile userProfile) {
        UserProfile user = userProfileRepository.save(userProfile);

        return user;
    }

    public String updateUser(UserProfile userProfile, Integer id) {


        Optional<UserProfile> user = userProfileRepository.findById(id);

        user.ifPresent(userProfile1 -> {
            userProfile1.setFirstName(userProfile.getFirstName());
            userProfile1.setLastName(userProfile.getLastName());
        });

        if(user.isEmpty()) {
           throw new EntityNotFoundException();
        }

        UserProfile updatedUser = userProfileRepository.save(user.get());

        return "User updated successfully";
    }

    public String deleteUser(Integer id) {

        String deletedBy = SecurityContextHolder.getContext().getAuthentication().getName();
        userProfileRepository.softDeleteById(id, deletedBy);

        return "User deleted Successfully";
    }
}
