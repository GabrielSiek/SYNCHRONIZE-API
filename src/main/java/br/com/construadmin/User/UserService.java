package br.com.construadmin.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public UserDTO obterPorId(String workerId) {
        Optional<User> optionalUser = userRepository.findById(workerId);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            return new UserDTO(user.getEmail(), user.getNome(), user.getPassword(), user.getRole(), user.getObras());
        }

        return null;
    }
}
