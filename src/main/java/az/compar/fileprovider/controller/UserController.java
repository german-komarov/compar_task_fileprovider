package az.compar.fileprovider.controller;

import az.compar.fileprovider.entity.User;
import az.compar.fileprovider.service.UserService;
import az.compar.fileprovider.util.dto.user.CreateUserDto;
import az.compar.fileprovider.util.dto.user.GetUserDto;
import az.compar.fileprovider.util.exceptions.ValidationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@CrossOrigin("*")
public class UserController extends BaseController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<Object> registerUser(CreateUserDto createUserDto) {
        try {
            User user = this.userService.createUser(createUserDto);
            return ResponseEntity.ok(new GetUserDto(user));
        } catch (ValidationException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
