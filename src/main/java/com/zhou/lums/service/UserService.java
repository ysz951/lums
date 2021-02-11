package com.zhou.lums.service;

import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.zhou.lums.exception.ResourceNotFoundException;
import com.zhou.lums.model.User;
import com.zhou.lums.model.User.Role;
import com.zhou.lums.payload.ApiResponse;
import com.zhou.lums.payload.PasswordRequest;
import com.zhou.lums.respository.UserRepository;
import com.zhou.lums.security.JwtTokenProvider;
import com.zhou.lums.security.UserPrincipal;

@Service
@Transactional
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LogService logService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider tokenProvider;

    public ResponseEntity<?> changePassword (
            UserPrincipal currentUser,
            PasswordRequest passwordRequest) {
        String oldPassword = passwordRequest.getOldPassword(),
                newPassword = passwordRequest.getNewPassword();
        User user = userRepository.findById(currentUser.getId()).get();
        Map<String, String> responseObj = new HashMap<>();
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            System.out.println("error");
            responseObj.put("password", "Incorrect old password");
            return new ResponseEntity<>(responseObj, HttpStatus.BAD_REQUEST);
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        responseObj.put("message", "password has been changed");
        return ResponseEntity.ok(responseObj);

    }

    public ResponseEntity<?> blockUser(UserPrincipal currentUser, long memberId) {
//        if (userRepository.updateUserBlock(true, memberId) == 0) throw new ResourceNotFoundException("User", "id", memberId);
//        logService.logBlockUser(admin, user, isBlocked);
        User user = userRepository.findById(memberId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", memberId));
        user.setBlocked(true);
        userRepository.save(user);
        User admin = userRepository.findById(currentUser.getId()).get();
        logService.logBlockUser(admin, user, true);
        return ResponseEntity.ok(new ApiResponse(true, "blocked user"));
    }

    public ResponseEntity<?> unblockUser(UserPrincipal currentUser, long memberId) {
//        if (userRepository.updateUserBlock(false, memberId) == 0) throw new ResourceNotFoundException("User", "id", memberId);
        User user = userRepository.findById(memberId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", memberId));
        user.setBlocked(false);
        userRepository.save(user);
        User admin = userRepository.findById(currentUser.getId()).get();
        logService.logBlockUser(admin, user, false);
        return ResponseEntity.ok(new ApiResponse(true, "unblocked user"));
    }

    public ResponseEntity<?> updateUserEmail(String newEmail, long memberId) {
        User user = userRepository.findById(memberId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", memberId));
        user.setEmail(newEmail);
        userRepository.save(user);
        return ResponseEntity.ok(new ApiResponse(true, "update email"));
    }

    public ResponseEntity<?> changeUserRole(UserPrincipal currentUser, long memberId, Role newRole) {
        User user = userRepository.findById(memberId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", memberId));
        User admin = userRepository.findById(currentUser.getId()).get();
        Role prevRole = user.getRole();
        user.setRole(newRole);
        userRepository.save(user);
        logService.logModifyRole(admin, user, prevRole, newRole);
        return ResponseEntity.ok(new ApiResponse(true, "change user role"));
    }
}
