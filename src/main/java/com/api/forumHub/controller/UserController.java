package com.api.forumHub.controller;


import com.api.forumHub.domain.user.UserDetailResponse;
import com.api.forumHub.domain.user.UserRequest;
import com.api.forumHub.domain.user.UserResponseDTO;
import com.api.forumHub.domain.user.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/users")
@SecurityRequirement(name = "bearer-key")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public ResponseEntity<UserResponseDTO> createAdmin(@RequestBody @Valid UserRequest request, UriComponentsBuilder uriBuilder) {
        UserResponseDTO newAdmin = userService.createAdminUser(request);

        URI uri = uriBuilder.path("/user/{id}").buildAndExpand(newAdmin.id()).toUri();

        return ResponseEntity.created(uri).body(newAdmin);
    }

    @PostMapping
    @Transactional
    public ResponseEntity<UserResponseDTO> created(@RequestBody @Valid UserRequest request, UriComponentsBuilder uriBuilder) {
        UserResponseDTO newUser = userService.createUser(request);

        URI uri = uriBuilder.path("/user/{id}").buildAndExpand(newUser.id()).toUri();

        return ResponseEntity.created(uri).body(newUser);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDetailResponse> getUser(@PathVariable Long id) {

        return ResponseEntity.ok(userService.getUser(id));
    }

    @GetMapping
    public ResponseEntity<Page<UserResponseDTO>> listUsers(@PageableDefault(size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {

        return ResponseEntity.ok(userService.listUsers(pageable));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

}
