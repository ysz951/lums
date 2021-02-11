package com.zhou.lums.controller;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.zhou.lums.exception.ResourceNotFoundException;
import com.zhou.lums.model.User;
import com.zhou.lums.model.User.Role;
import com.zhou.lums.payload.PasswordRequest;
import com.zhou.lums.payload.UserIdentityAvailability;
import com.zhou.lums.payload.UserSummary;
import com.zhou.lums.respository.UserRepository;
import com.zhou.lums.security.CurrentUser;
import com.zhou.lums.security.UserPrincipal;
import com.zhou.lums.service.UserService;



@RestController
@RequestMapping("/api")
public class UserController {

    private UserRepository userRepository;

    private UserService userService;

    @Autowired
    public UserController(UserRepository userRepository, UserService userService) {
        this.userRepository = userRepository;
        this.userService = userService;
    }

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

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

        UserSummary userSummary = new UserSummary(user.getId(), user.getUsername(), user.getName(), user.isBlocked(), user.getRole());

        return userSummary;
    }

    @GetMapping("/users")
    public List<UserSummary> getUsers() {
        List<UserSummary> userList= userRepository.findAll()
                .stream()
                .map(user -> new UserSummary(user.getId(), user.getUsername(), user.getName(), user.isBlocked(), user.getRole()))
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
    public ResponseEntity<?> changePassword(@CurrentUser UserPrincipal currentUser,
            @Valid @RequestBody PasswordRequest passwordRequest) {
        return userService.changePassword(currentUser, passwordRequest);
    }

    @PostMapping("/users/block/{memberId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> blockUser(
            @CurrentUser UserPrincipal currentUser,
            @PathVariable(value = "memberId") long memberId) {
        return userService.blockUser(currentUser, memberId);
    }

    @PostMapping("/users/unblock/{memberId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> unblockUser(
            @CurrentUser UserPrincipal currentUser,
            @PathVariable(value = "memberId") long memberId) {
        return userService.unblockUser(currentUser, memberId);
    }

    @PostMapping("/users/email/{memberId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateUserEmail(@PathVariable(value = "memberId") long memberId, @RequestParam(value = "new_email") String newEmail) {
        return userService.updateUserEmail(newEmail, memberId);
    }

    @PostMapping("/users/{memberId}/modify_role")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> changeUserRole(
            @CurrentUser UserPrincipal currentUser,
            @PathVariable("memberId") long memberId,
            @RequestParam("newRole") Role newRole) {

        return userService.changeUserRole(currentUser, memberId, newRole);
    }

}
