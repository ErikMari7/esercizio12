package co.develhope.esercizio12;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserRepository userRepository;
    @PostMapping("/post")
    public @ResponseBody User createUser(@RequestBody User user){
        return userRepository.save(user);
    }
    @GetMapping("/getall")
    public List<User> getAllUsers(){
        return userRepository.findAll();
    }
    @GetMapping("/get/{id}")
    public User getUserById(@PathVariable Long id){
        Optional<User> user = userRepository.findById(id);
        if(user.isPresent()){
            return user.get();
        } else {
            return null;
        }
    }
    @PutMapping("/put/{id}")
    public User changeActive(@PathVariable Long id,@RequestParam boolean isActive){
        Optional<User> user = userRepository.findById(id);
        if(user.isPresent()){
            User user1 = user.get();
            user1.setActive(isActive);
            return userRepository.save(user1);
        }
        return null;
    }
    @DeleteMapping("/delete/{id}")
    public void deleteUser(@PathVariable Long id){
        userRepository.deleteById(id);
    }

}
