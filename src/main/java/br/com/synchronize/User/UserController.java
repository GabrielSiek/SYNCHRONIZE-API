package br.com.synchronize.User;

import br.com.synchronize.Categorias.UpdateRole;
import br.com.synchronize.Empresa.Empresa;
import br.com.synchronize.Empresa.EmpresaRepository;
import br.com.synchronize.Obra.ObraDTO;
import br.com.synchronize.Obra.ObraService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmpresaRepository empresaRepository;

    @Autowired
    private ObraService obraService;

    //arrumado e vai ser usado
    //create
    @PostMapping("/register-user")
    public ResponseEntity registerWorker(@RequestBody @Valid RegisterUserDTO registerUserDTO, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        if (this.userRepository.findByEmail(registerUserDTO.email()) != null)
            return ResponseEntity.badRequest().body("Usuário já cadastrado no sistema");

        Optional<Empresa> optionalEmpresa = empresaRepository.findById(user.getEmpresa().getId());

        if (optionalEmpresa.isPresent()) {
            Empresa empresa = optionalEmpresa.get();
            try {
                String encryptedPassword = new BCryptPasswordEncoder().encode(registerUserDTO.password());

                User newWorker = new User(registerUserDTO.email(), registerUserDTO.nome(), encryptedPassword, registerUserDTO.role(), empresa);

                this.userRepository.save(newWorker);

                return ResponseEntity.ok().body("Usuário cadastrado com sucesso!");
            } catch (Exception ex) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao cadastrar usuário: " + ex);
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Empresa não cadastrada no sistema com id: " + user.getEmpresa().getId());
    }

    //arrumado e acho que é usado
    //read
    @GetMapping("/{user_id}/user-info")
    public UserDTO getUser(@PathVariable String user_id) {
        UserDTO user = userService.obterPorId(user_id);
        if(user != null)
            return user;

        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado");
    }

    //arrumado e vai ser usado
    //read worker obras
    @GetMapping("/{user_id}/user-obras")
    public List<ObraDTO> obterObrasPorId(@PathVariable String user_id) {
        List<ObraDTO> obras = obraService.obterObrasPorUserId(user_id);
        if(obras != null)
            return obras;

        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado");
    }

    //arrumado e vai ser usado
    //update
    @PutMapping("/{user_id}/user-update")
    public ResponseEntity updateUser(@PathVariable String user_id, @RequestBody @Valid UpdateUserDTO uptadeUserDTO) {
        Optional<User> optionalUser = userRepository.findById(user_id);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            try {
                if (!uptadeUserDTO.email().equals(user.getEmail()))
                    user.setEmail(uptadeUserDTO.email());

                if (!uptadeUserDTO.nome().equals(user.getNome()))
                    user.setNome(uptadeUserDTO.nome());

                String encryptedPassword = new BCryptPasswordEncoder().encode(uptadeUserDTO.password());
                if (!encryptedPassword.equals(user.getPassword()))
                    user.setPassword(encryptedPassword);

                if (!uptadeUserDTO.role().equals(user.getRole()))
                    user.setRole(uptadeUserDTO.role());

                userRepository.save(user);

                return ResponseEntity.ok().body("Dados do usuário atualizados com sucesso!");
            } catch (Exception ex) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Falha ao mudar dados do usuário: " + ex);
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado com o ID: " + user_id);
    }

    //arrumado e vai ser usado
    //update user role
    @PutMapping("/{user_id}/user-update-role")
    public ResponseEntity updateUserRole(@PathVariable String user_id, @RequestBody @Valid UpdateRole updateRole) {
        Optional<User> optionalUser = userRepository.findById(user_id);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            try {
                if (!updateRole.role().equals(user.getRole()))
                    user.setRole(updateRole.role());
                userRepository.save(user);
                return ResponseEntity.ok("Role do usuário atualizado com sucesso!");
            } catch (IllegalArgumentException ex) {
                return ResponseEntity.badRequest().body("Role de usuário inválido: " + updateRole.role());
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado com o ID: " + user_id);
        }
    }


    //arrumado e vai ser udado
    //delete
    @DeleteMapping("/{user_id}/user-delete")
    public ResponseEntity deleteUser(@PathVariable String user_id) {
        Optional<User> optionalUser = userRepository.findById(user_id);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            userRepository.delete(user);

            return ResponseEntity.ok().body("Usuário deletado com sucesso!");
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado para o ID: " + user_id);
    }
}

