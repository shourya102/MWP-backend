package com.mwp.mwp.controller;


import com.mwp.mwp.model.UserProfile;
import com.mwp.mwp.payload.request.UserProfileRequest;
import com.mwp.mwp.payload.response.MessageResponse;
import com.mwp.mwp.payload.response.ProfileResponse;
import com.mwp.mwp.respository.UserProfileRepository;
import com.mwp.mwp.respository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RestController
@RequestMapping("api/userprofile")
public class UserProfileController {

    @Autowired
    UserProfileRepository userProfileRepository;

    @Autowired
    UserRepository userRepository;

    @GetMapping("/:id")
    public ResponseEntity<?> getProfile(@PathVariable Long id) {
        UserProfile userProfile = userProfileRepository.getReferenceById(id);
        return ResponseEntity.ok(new ProfileResponse(userProfile.getFirstName(), userProfile.getLastName(),
                userProfile.getAge(), userProfile.getEmail(), userProfile.getCountry(), userProfile.getPhoneNumber()));
    }

    @PostMapping("/:id")
    public ResponseEntity<?> postProfile(@RequestBody UserProfileRequest userProfileRequest, @PathVariable Long id) {
        UserProfile userProfile = userProfileRepository.getReferenceById(id);
        userProfile.setFirstName(userProfileRequest.getFirstName());
        userProfile.setLastName(userProfileRequest.getLastName());
        userProfile.setEmail(userProfileRequest.getEmail());
        userProfileRequest.setAge(userProfileRequest.getAge());
        userProfile.setCountry(userProfileRequest.getCountry());
        return ResponseEntity.ok(new MessageResponse("User profile updated successfully."));
    }

}
