package com.zhou.lums.controller;


import com.zhou.lums.exception.ResourceNotFoundException;
import com.zhou.lums.model.User;
import com.zhou.lums.model.User.Role;
import com.zhou.lums.payload.JwtAuthenticationResponse;
import com.zhou.lums.payload.PasswordRequest;
import com.zhou.lums.payload.UserIdentityAvailability;
import com.zhou.lums.payload.UserSummary;
import com.zhou.lums.respository.UserRepository;
import com.zhou.lums.security.CurrentUser;
import com.zhou.lums.security.JwtTokenProvider;
import com.zhou.lums.security.UserPrincipal;
import com.zhou.lums.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    private UserRepository userRepository;
    private UserService userService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    public UserController(UserRepository userRepository, UserService userService) {
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @GetMapping("/user/{userId}")
    // @PreAuthorize("hasRole('USER')")
    public UserSummary getUserById(@PathVariable(value = "userId") long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        UserSummary userSummary = new UserSummary(user.getId(), user.getUsername(), user.getName(), user.isBlocked(), user.getRole(), user.getEmail());

        return userSummary;
    }

    @PostMapping("/jwt/refresh")
    public ResponseEntity<?> refresh(@CurrentUser UserPrincipal currentUser) {
        String jwt = tokenProvider.refreshToken(currentUser);
        return ResponseEntity.ok(new JwtAuthenticationResponse(jwt, currentUser.getRole()));

    }

    @GetMapping("/user/me")
    // @PreAuthorize("hasRole('USER')")
    public UserSummary getCurrentUser(@CurrentUser UserPrincipal currentUser) {
        UserSummary userSummary = new UserSummary(currentUser.getId(), currentUser.getUsername(), currentUser.getName(), currentUser.isBlocked(), currentUser.getRole());
        return userSummary;
    }

    @GetMapping("/user/checkUsernameAvailability")
    public UserIdentityAvailability checkUsernameAvailability(@CurrentUser UserPrincipal currentUser, @RequestParam(value = "username") String username) {
        Boolean isAvailable = !userRepository.existsByUsername(username);
        return new UserIdentityAvailability(isAvailable);
    }

    @GetMapping("/user/checkEmailAvailability")
    public UserIdentityAvailability checkEmailAvailability(@RequestParam(value = "email") String email) {
        Boolean isAvailable = !userRepository.existsByEmail(email);
        return new UserIdentityAvailability(isAvailable);
    }

    @GetMapping("/users/{username}")
    public UserSummary getUserProfile(@PathVariable(value = "username") String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));

        UserSummary userSummary = new UserSummary(user.getId(), user.getUsername(), user.getName(), user.isBlocked(), user.getRole(), user.getEmail());

        return userSummary;
    }

    @GetMapping("/users/role/{role}")
    public List<UserSummary> getUserByRole(@PathVariable(value = "role") Role role) {
        return userRepository.findAllByRole(role)
                .stream()
                .map(user -> new UserSummary(user.getId(), user.getUsername(), user.getName(), user.isBlocked(), user.getRole(), user.getEmail()))
                .collect(Collectors.toList());
    }


    @GetMapping("/users")
    public List<UserSummary> getUsers() {
        List<UserSummary> userList = userRepository.findAllOrderedById()
                .stream()
                .map(user -> new UserSummary(user.getId(), user.getUsername(), user.getName(), user.isBlocked(), user.getRole(), user.getEmail()))
                .collect(Collectors.toList());
        return userList;
    }

    @GetMapping("/users/count")
    public ResponseEntity<?> getUserCount() {
        Map<String, Long> responseObj = new HashMap<>();
        responseObj.put("count", userRepository.countById());
        return ResponseEntity.ok(responseObj);
    }

    @PostMapping("/users/password")
    @PreAuthorize("hasRole('SUPERUSER')")
    public ResponseEntity<?> changePassword(@RequestParam(value = "user_id") long userId,
                                            @Valid @RequestBody PasswordRequest passwordRequest,
                                            @CurrentUser UserPrincipal currentUser) {
        return userService.changePassword(userId, currentUser, passwordRequest);
    }

    @PostMapping("/users/block/{memberId}")
    // @PreAuthorize("hasRole('ADMIN')")
    @PreAuthorize("hasAnyRole('ADMIN','SUPERUSER')")
    public ResponseEntity<?> blockUser(
            @CurrentUser UserPrincipal currentUser,
            @PathVariable(value = "memberId") long memberId) {
        User user = userService.findUserById(memberId);
        User admin = userService.findCurrentUser(currentUser);
        return userService.blockUser(user, admin);
    }

    @PostMapping("/users/unblock/{memberId}")
    @PreAuthorize("hasAnyRole('ADMIN','SUPERUSER')")
    public ResponseEntity<?> unblockUser(
            @CurrentUser UserPrincipal currentUser,
            @PathVariable(value = "memberId") long memberId) {
        User user = userService.findUserById(memberId);
        User admin = userService.findCurrentUser(currentUser);
        return userService.unblockUser(user, admin);
    }

    @PostMapping("/users/email/{memberId}")
    @PreAuthorize("hasAnyRole('ADMIN','SUPERUSER')")
    public ResponseEntity<?> updateUserEmail(
            @PathVariable(value = "memberId") long memberId,
            @RequestParam(value = "new_email") String newEmail,
            @CurrentUser UserPrincipal currentUser) {
        return userService.updateUserEmail(newEmail, memberId, currentUser);
    }

    @PostMapping("/users/{memberId}/modify_role")
    @PreAuthorize("hasAnyRole('ADMIN','SUPERUSER')")
    public ResponseEntity<?> changeUserRole(
            @CurrentUser UserPrincipal currentUser,
            @PathVariable("memberId") long memberId,
            @RequestParam("newRole") Role newRole) {
        return userService.changeUserRole(currentUser, memberId, newRole);
    }

}
