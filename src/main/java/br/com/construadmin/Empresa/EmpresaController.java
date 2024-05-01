package br.com.construadmin.Empresa;

import br.com.construadmin.Infra.Security.TokenService;
import br.com.construadmin.User.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
public class EmpresaController {

    @Autowired
    EmpresaRepository empresaRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private EmpresaService empresaService;


    //create
//    @PostMapping("/register-company")
//    public ResponseEntity registerCompany(@RequestBody @Valid RegisterEmpresaDTO registerEmpresaDTO) {
//        User user = userRepository.findById(registerEmpresaDTO.admin_id()).orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));
//
//        if (user.getEmpresa() != null)
//            throw new IllegalArgumentException("Usuário com empresa já registrada");
//
//        Empresa newEmpresa = new Empresa(registerEmpresaDTO.nome());
//
//        this.empresaRepository.save(newEmpresa);
//        user.setEmpresa(newEmpresa);
//
//        userRepository.save(user); // Salvar as alterações no usuário
//
//        return ResponseEntity.ok().build();
//    }

    //read
    @GetMapping("/{empresa_id}/company-info")
    public EmpresaDTO getEmpresa(@PathVariable String empresa_id) {
        return empresaService.obterPorId(empresa_id);
    }

    //update
    @PutMapping("/{empresa_id}/company-update")
    public ResponseEntity updateCompanyName(@PathVariable String empresa_id, @RequestBody @Valid UpdateEmpresaDTO updateEmpresaDTO) {
        Optional<Empresa> optionalEmpresa = empresaRepository.findById(empresa_id);

        if (optionalEmpresa.isPresent()) {
            Empresa empresa = optionalEmpresa.get();

            try {
                if (!updateEmpresaDTO.nome().equals(empresa.getNome()))
                    empresa.setNome(updateEmpresaDTO.nome());

                empresaRepository.save(empresa);

                return ResponseEntity.ok().body("Dados da empresa atualizados com sucesso!");
            } catch (Exception ex) {
                return ResponseEntity.ok().body("Falha ao alterar os dados da empresa: " + ex);
            }

        }

        return ResponseEntity.badRequest().body("Empresa não encontrada com o id: " + empresa_id);
    }

    //delete
    @DeleteMapping("/{empresa_id}/company-delete")
    public ResponseEntity deleteCompany(@PathVariable String empresa_id) {
        Optional<Empresa> optionalEmpresa = empresaRepository.findById(empresa_id);

        if (optionalEmpresa.isPresent()) {
            Empresa empresa = optionalEmpresa.get();
            try {
                empresaRepository.delete(empresa);
                return ResponseEntity.ok().body("Empresa deletada com sucesso!");
            } catch (Exception ex) {
                return ResponseEntity.badRequest().body("Erro ao deletar empresa: " + ex);
            }
        }

        return ResponseEntity.badRequest().body("Empresa não encontrada para o ID: " + empresa_id);
    }
}
