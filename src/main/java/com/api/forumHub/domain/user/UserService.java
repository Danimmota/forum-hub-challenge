package com.api.forumHub.domain.user;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserResponseDTO createAdminUser(UserRequest request) {
        User newAdmin = new User();
        newAdmin.setName(request.name());
        newAdmin.setEmail(request.email());
        newAdmin.setPassword(passwordEncoder.encode(request.password()));
        newAdmin.setRole(Role.ROLE_ADMIN);

        userRepository.save(newAdmin);
        return UserMapper.toDto(newAdmin);
    }

    public UserResponseDTO createUser(UserRequest request) {

        User newUser = UserMapper.toEntity(request, passwordEncoder);
        userRepository.save(newUser);

        return UserMapper.toDto(newUser) ;
    }

    public Page<UserResponseDTO> listUsers (Pageable pageable) {
        return userRepository.findAll(pageable)
                .map(UserMapper::toDto);
    }

    public UserDetailResponse getUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found!"));

         return UserMapper.toDetailDto(user);
    }

    public void deleteUser(Long id) {
        userRepository.findById(id)
                .ifPresentOrElse(
                        userRepository::delete,
                        () -> { throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found by id." + id);
                        }
                );
    }
}
