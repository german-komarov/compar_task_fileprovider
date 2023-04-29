package az.compar.fileprovider.service;

import az.compar.fileprovider.entity.User;
import az.compar.fileprovider.repository.UserRepository;
import az.compar.fileprovider.util.dto.user.CreateUserDto;
import az.compar.fileprovider.util.exceptions.UserNotFoundException;
import az.compar.fileprovider.util.exceptions.ValidationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class, isolation = Isolation.REPEATABLE_READ)
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User getByEmail(String email) throws UserNotFoundException {
        return userRepository
                .findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("There is no user with email = " + email));
    }

    public User createUser(CreateUserDto createUserDto) throws ValidationException {
        if (!createUserDto.getPassword().equals(createUserDto.getConfirmPassword())) {
            throw new ValidationException("Password and its confirm are not matched");
        }
        if (userRepository.findByEmail(createUserDto.getEmail()).isPresent()) {
            throw new ValidationException("User with such email already exists");
        }
        User user = new User();
        user.setEmail(createUserDto.getEmail());
        user.setPassword(passwordEncoder.encode(createUserDto.getPassword()));
        user = userRepository.save(user);
        return user;
    }
}