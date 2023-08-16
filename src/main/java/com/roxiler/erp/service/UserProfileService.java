package com.roxiler.erp.service;

import com.roxiler.erp.constants.PermissionConstants;
import com.roxiler.erp.dto.profile.CreateUserProfileDto;
import com.roxiler.erp.dto.profile.UpdateUserProfileDto;
import com.roxiler.erp.interfaces.RequiredPermission;
import com.roxiler.erp.model.UserProfile;
import com.roxiler.erp.model.Users;
import com.roxiler.erp.repository.UserProfileRepository;
import com.roxiler.erp.repository.UsersRepository;
import jakarta.persistence.EntityNotFoundException;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.firewall.RequestRejectedException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class UserProfileService {

    @Autowired
    private UserProfileRepository userProfileRepository;

    @Autowired
    private UsersRepository usersRepository;

    @RequiredPermission(permission = PermissionConstants.PROFILE)
    public Iterable<UserProfile> getAllUsers() {
        Iterable<UserProfile> users = userProfileRepository.findAll();

        return users;
    }

    @RequiredPermission(permission = PermissionConstants.PROFILE)
    public UserProfile saveUser(CreateUserProfileDto userProfile, Integer userId) {

        Optional<Users> user = usersRepository.findById(userId);
        if (user.isPresent()) {

            UserProfile newUserProfile = new UserProfile();
            newUserProfile.setFirstName(userProfile.getFirstName());
            newUserProfile.setLastName(userProfile.getLastName());
            newUserProfile.setDateOfBirth(userProfile.getDateOfBirth());
            newUserProfile.setGender(userProfile.getGender());
            newUserProfile.setAadharCard(userProfile.getAadharCard());
            newUserProfile.setPanCard(userProfile.getPanCard());
            newUserProfile.setWorkPhoneNumber(userProfile.getWorkPhoneNumber());
            newUserProfile.setPersonalPhoneNumber(userProfile.getPersonalPhoneNumber());
            newUserProfile.setMonthlyCtc(userProfile.getMonthlyCtc());
            newUserProfile.setLanguage(userProfile.getLanguage());
            newUserProfile.setUser(user.get());
            UserProfile userProfile1 = userProfileRepository.save(newUserProfile);

            user.get().setUserProfile(userProfile1);
            usersRepository.save(user.get());

            return userProfile1;
        } else {
            throw new EntityNotFoundException("User not found");
        }
    }

    @RequiredPermission(permission = PermissionConstants.PROFILE)
    public UserProfile updateUser(UpdateUserProfileDto userProfile, Integer id, Integer uId) {

        if (!Objects.equals(id, uId)) {
            throw new AuthorizationServiceException("You are not allowed to perform this action");
        }
        Optional<UserProfile> userProf = userProfileRepository.findById(id);
        if (userProf.isPresent()) {
            Integer userId = userProf.get().getUser().getId();
            Optional<Users> user = usersRepository.findById(userId);
            if (user.isPresent()) {
                if (user.get().getId() != 1) {
                    throw new AuthorizationServiceException("You are not allowed to perform this action");
                }
            }
            UserProfile userProfileToUpdate = userProf.get();
            userProfileToUpdate.setFirstName(userProfile.getFirstName());
            userProfileToUpdate.setLastName(userProfile.getLastName());
            userProfileToUpdate.setDateOfBirth(userProfile.getDateOfBirth());
            userProfileToUpdate.setGender(userProfile.getGender());
            userProfileToUpdate.setAadharCard(userProfile.getAadharCard());
            userProfileToUpdate.setPanCard(userProfile.getPanCard());
            userProfileToUpdate.setWorkPhoneNumber(userProfile.getWorkPhoneNumber());
            userProfileToUpdate.setPersonalPhoneNumber(userProfile.getPersonalPhoneNumber());
            userProfileToUpdate.setMonthlyCtc(userProfile.getMonthlyCtc());
            userProfileToUpdate.setLanguage(userProfile.getLanguage());

            UserProfile updatedUser = userProfileRepository.save(userProfileToUpdate);

            return updatedUser;
        } else {
            throw new EntityNotFoundException("User not found");
        }
    }

    @RequiredPermission(permission = PermissionConstants.PROFILE)
    public String deleteUser(Integer id, Integer userId) {

        if (!Objects.equals(id, userId)) {
            throw new AuthorizationServiceException("You are not allowed to perform this action");
        }
        String deletedBy = SecurityContextHolder.getContext().getAuthentication().getName();
        userProfileRepository.softDeleteById(id, deletedBy);

        return "User deleted Successfully";
    }
}
