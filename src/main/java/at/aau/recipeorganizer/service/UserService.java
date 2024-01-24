package at.aau.recipeorganizer.service;

import at.aau.recipeorganizer.data.Role;
import at.aau.recipeorganizer.data.SignupRequest;
import at.aau.recipeorganizer.data.User;
import at.aau.recipeorganizer.repository.RoleRepository;
import at.aau.recipeorganizer.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserSignUpResult registerUser(SignupRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.username())) return UserSignUpResult.USERNAME_TAKEN;
        if (userRepository.existsByEmail(signUpRequest.email())) return UserSignUpResult.EMAIL_TAKEN;

        var user = new User(signUpRequest.username(), signUpRequest.email(), passwordEncoder.encode(signUpRequest.password()));
        user.roles.add(roleRepository.findByName(Role.ERole.ROLE_USER).orElseThrow());
        userRepository.save(user);

        return UserSignUpResult.SUCCESS;
    }

    public Optional<User> getUserFromUserName(String userName) {
        return userRepository.findByUsername(userName);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public enum UserSignUpResult {
        USERNAME_TAKEN, EMAIL_TAKEN, SUCCESS
    }
}
