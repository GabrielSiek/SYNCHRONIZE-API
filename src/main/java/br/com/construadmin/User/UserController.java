package br.com.construadmin.User;

import br.com.construadmin.Categorias.UpdateRole;
import br.com.construadmin.Empresa.Empresa;
import br.com.construadmin.Empresa.EmpresaRepository;
import br.com.construadmin.Obra.ObraDTO;
import br.com.construadmin.Obra.ObraService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

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

    //create primeiro usuario junto de empresa
    //create
    @PostMapping("{empresa_id}/register-user")
    public ResponseEntity registerWorker(@PathVariable String empresa_id, @RequestBody @Valid RegisterUserDTO registerUserDTO) {
        if (this.userRepository.findByEmail(registerUserDTO.email()) != null)
            return ResponseEntity.badRequest().body("Usuário já cadastrado no sistema");

        Optional<Empresa> optionalEmpresa = empresaRepository.findById(empresa_id);

        if (optionalEmpresa.isPresent()) {
            Empresa empresa = optionalEmpresa.get();
            try {
                String encryptedPassword = new BCryptPasswordEncoder().encode(registerUserDTO.password());

                User newWorker = new User(registerUserDTO.email(), registerUserDTO.nome(), encryptedPassword, registerUserDTO.role(), empresa);

                this.userRepository.save(newWorker);

                return ResponseEntity.ok().body("Usuário cadastrado com sucesso!");
            } catch (Exception ex) {
                return ResponseEntity.badRequest().body("Erro ao cadastrar usuário");
            }
        }
        return ResponseEntity.badRequest().body("Empresa não cadastrado no sistema com id: " + empresa_id);
    }

    //read
    @GetMapping("/{user_id}/user-info")
    public UserDTO getUser(@PathVariable String user_id) {
        return userService.obterPorId(user_id);
    }

    //read worker obras
    @GetMapping("/{user_id}/user-obras")
    public List<ObraDTO> obterObrasPorId(@PathVariable String user_id) {
        return obraService.obterObrasPorWorkerId(user_id);
    }

    //update
    @PutMapping("/{user_id}/user-update")
    public ResponseEntity updateUser(@PathVariable String user_id, @RequestBody @Valid UptadeUserDTO uptadeUserDTO) {
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
                return ResponseEntity.badRequest().body("Falha ao mudar dados do usuário: " + ex);
            }
        }
        return ResponseEntity.badRequest().body("Usuário não encontrado com o ID: " + user_id);
    }

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
            return ResponseEntity.badRequest().body("Usuário não encontrado com o ID: " + user_id);
        }
    }

    //delete
    @DeleteMapping("/{user_id}/user-delete")
    public ResponseEntity deleteUser(@PathVariable String user_id) {
        Optional<User> optionalUser = userRepository.findById(user_id);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            userRepository.delete(user);

            return ResponseEntity.ok().body("Usuário deletado com sucesso!");
        }

        return ResponseEntity.badRequest().body("Usuário não encontrado para o ID: " + user_id);
    }
}

