package com.api.forumHub.domain.user;

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

    public Page<UserResponseDTO> listUsers (Pageable pageable) {
        return userRepository.findAll(pageable)
                .map(UserMapper::toDto);
    }

    public UserResponseDTO createAdminUser(UserRequest request) {
        User newAdmin = new User(
                request.name(),
                request.email(),
                passwordEncoder.encode(request.password()),
                Role.ROLE_ADMIN
        );
        userRepository.save(newAdmin);
        return UserMapper.toDto(newAdmin);
    }

    public UserResponseDTO createUser(UserRequest request) {

        User newUser = UserMapper.toEntity(request, passwordEncoder);
        userRepository.save(newUser);

        return UserMapper.toDto(newUser) ;
    }

    public UserDetailResponse getUser(Long id) {
        User user = userRepository.findById(id).orElse(null);
         return user != null ? UserMapper.toDetailDto(user) : null;
    }

    public void deleteUser(Long id) {
        userRepository.findById(id)
                .ifPresentOrElse(
                        userRepository::delete,
                        () -> { throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found by id.");
                        }
                );
    }
}
