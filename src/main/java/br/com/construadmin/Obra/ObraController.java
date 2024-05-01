package br.com.construadmin.Obra;

import br.com.construadmin.Categorias.UpdateStatus;
import br.com.construadmin.Empresa.Empresa;
import br.com.construadmin.Empresa.EmpresaRepository;
import br.com.construadmin.User.User;
import br.com.construadmin.User.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController

public class ObraController {

    @Autowired
    ObraRepository obraRepository;

    @Autowired
    ObraService obraService;

    @Autowired
    EmpresaRepository empresaRepository;

    @Autowired
    UserRepository userRepository;

    //create
    @PostMapping("/{empresa_id}/register-job")
    public ResponseEntity registerJob(@PathVariable String empresa_id, @RequestBody @Valid RegisterObraDTO registerObraDTO) {
        Optional<Empresa> optionalEmpresa = empresaRepository.findById(empresa_id);
        Optional<User> optionalUser = userRepository.findById(registerObraDTO.encarregado_id());

        if (optionalEmpresa.isPresent() && optionalUser.isPresent()) {
            try {
                Empresa empresa = optionalEmpresa.get();
                User encarregado = optionalUser.get();

                Obra newObra = new Obra(registerObraDTO.nome(), empresa, encarregado);

                obraRepository.save(newObra);

                return ResponseEntity.ok().body("Obra cadastrada com sucesso!");
            } catch (Exception ex) {
                return ResponseEntity.badRequest().body("Erro ao cadastrar obra");
            }
        } else
            return ResponseEntity.badRequest().body("Empresa ou encarregado não cadastrado no sistema");
    }

    //read
    @GetMapping("/{obra_id}/job-info")
    public ObraDTO getObra(@PathVariable String obra_id) {
        return obraService.obterPorId(obra_id);
    }

    //update
    @PutMapping("/{obra_id}/job-update")
    public ResponseEntity updateJob(@PathVariable String obra_id, @RequestBody @Valid UpdateObraDTO updateObraDTO) {
        Optional<Obra> optionalObra = obraRepository.findById(obra_id);
        Optional<User> optionalUser = userRepository.findById(updateObraDTO.encarregado_id());

        if (optionalObra.isPresent() && optionalUser.isPresent()) {
            Obra obra = optionalObra.get();
            User user = optionalUser.get();

            try {
                if (!updateObraDTO.nome().equals(obra.getNome()))
                    obra.setNome(updateObraDTO.nome());

                if (!updateObraDTO.status().equals(obra.getStatus()))
                    obra.setStatus(updateObraDTO.status());

                if (!user.equals(obra.getEncarregado()))
                    obra.setEncarregado(user);

                obraRepository.save(obra);

                return ResponseEntity.ok().body("Dados da obra atualizados com sucesso!");
            } catch (Exception ex) {
                return ResponseEntity.badRequest().body("Falha ao alterar dados do usuário: " + ex);
            }
        }

        return ResponseEntity.badRequest().body("Id de obra ou de encarregado não encotrado");
    }

    //update status obra
    @PutMapping("/{obra_id}/job-update-status")
    public ResponseEntity updateJobStatus(@PathVariable String obra_id, @RequestBody @Valid UpdateStatus updateStatus) {
        Optional<Obra> optionalObra = obraRepository.findById(obra_id);

        if (optionalObra.isPresent()) {
            Obra obra = optionalObra.get();

            try {
                obra.setStatus(updateStatus.status());

                if (!updateStatus.status().equals(obra.getStatus()))
                    obraRepository.save(obra);

                return ResponseEntity.ok().body("Status da obra atualizada com sucesso!");
            } catch (Exception ex) {
                return ResponseEntity.badRequest().body("Falha ao alterar status da obra: " + ex);
            }
        }

        return ResponseEntity.badRequest().body("Obra não encontrada com o ID: " + obra_id);
    }

    //delete
    @DeleteMapping("/{obra_id}/job-delete")
    public ResponseEntity deleteJob(@PathVariable String obra_id) {
        Optional<Obra> optionalObra = obraRepository.findById(obra_id);

        if (optionalObra.isPresent()) {
            Obra obra = optionalObra.get();
            try {
                obraRepository.delete(obra);

                return ResponseEntity.ok().body("Obra deletada com sucesso!");
            } catch (Exception ex) {
                return ResponseEntity.badRequest().body("Erro ao deletar obra do sistema: " + ex);
            }
        }

        return ResponseEntity.badRequest().body("Obra não encontrada para o ID: " + obra_id);
    }
}
