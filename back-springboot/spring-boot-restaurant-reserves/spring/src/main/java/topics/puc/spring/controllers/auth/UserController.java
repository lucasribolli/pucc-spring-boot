package topics.puc.spring.controllers.auth;

import topics.puc.spring.models.user.User;
import topics.puc.spring.repository.UserRepository;
import topics.puc.spring.payload.request.UpdateUserRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserRepository userRepository;
    private String tag = UserController.class.getSimpleName() + " /users";

    @Autowired
    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PutMapping(
        consumes = {MediaType.APPLICATION_JSON_VALUE},
        produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    @Transactional
    public void updateUser(
        @RequestParam
        String email,
        @RequestBody UpdateUserRequest user
    ) {
        System.out.println(tag + "updateUser...");
        User dbUser = userRepository.findByEmail(email);
        dbUser.setUsername(user.getUsername());
        userRepository.save(dbUser);
    }

    @DeleteMapping()
    @Transactional
    public ResponseEntity<String> deleteUserByEmail(
            @RequestParam
            String email
    ) {
        userRepository.deleteByEmail(email);
        return ResponseEntity.ok("Resource deleted successfully " + email);
    }

//    @GetMapping(
//        produces = {MediaType.APPLICATION_JSON_VALUE}
//    )
//    @Transactional
//    public ResponseEntity<User> getUserByEmail(
//            @RequestParam
//            String email
//    ) {
//        System.out.println(tag + "getUserByEmail...");
//        User userData = userRepository.findByEmail(email);
//
//        if (userData != null) {
//            return new ResponseEntity<>(userData, HttpStatus.OK);
//        } else {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
//    }

    @GetMapping(
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    @Transactional
    public ResponseEntity<List<User>> getAllUsers() {
        try {
            System.out.println(tag + "getAllUsers...");
            List<User> userData = userRepository.findAll();

            return new ResponseEntity<>(userData, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}