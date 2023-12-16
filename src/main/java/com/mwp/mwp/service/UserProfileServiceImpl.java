package com.mwp.mwp.service;

import com.mwp.mwp.model.UserProfile;
import com.mwp.mwp.respository.UserProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserProfileServiceImpl implements UserProfileService {

    @Autowired
    UserProfileRepository userProfileRepository;

    @Override
    public void updateProfile(UserProfile userProfile) {
        userProfileRepository.findById(userProfile.getId());

    }
}
