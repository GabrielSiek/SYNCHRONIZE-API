package br.com.synchronize.Authentication;

import br.com.synchronize.Categorias.Role;
import br.com.synchronize.Empresa.Empresa;
import br.com.synchronize.Empresa.EmpresaRepository;
import br.com.synchronize.Infra.Security.TokenService;
import br.com.synchronize.User.User;
import br.com.synchronize.User.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmpresaRepository empresaRepository;

    @Autowired
    private TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody @Valid AuthenticationDTO authenticationDTO) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(authenticationDTO.email(), authenticationDTO.password());
        var auth = this.authenticationManager.authenticate(usernamePassword);

        var token = tokenService.generateToken((User) auth.getPrincipal());

        return ResponseEntity.ok(new LoginResponseDTO(token));
    }

    //arrumar criar conta junto de empresa
//    @PostMapping("/register")
//    public ResponseEntity register(@RequestBody @Valid RegisterUserBossDTO registerUserBossDTO) {
//        if (this.userRepository.findByEmail(registerUserBossDTO.email()) != null)
//            return ResponseEntity.badRequest().body("Email já cadastrado no sistema");
//
//        String encryptedPassword = new BCryptPasswordEncoder().encode(registerUserBossDTO.password());
//
//        Empresa empresa = new Empresa(registerUserBossDTO.nome_empresa());
//
//        empresaRepository.save(empresa);
//
//        Role role = Role.ADMIN;
//        User newUser = new User(registerUserBossDTO.email(), registerUserBossDTO.nome(), encryptedPassword, role, empresa);
//
//        userRepository.save(newUser);
//
//
//        return ResponseEntity.ok().body("Usuário chefe cadastrado com sucesso!");
//    }

        @GetMapping("/verify-token")
    public ResponseEntity verifyToken() {
        return ResponseEntity.ok().body("Usuário com token válido");
    }


}
