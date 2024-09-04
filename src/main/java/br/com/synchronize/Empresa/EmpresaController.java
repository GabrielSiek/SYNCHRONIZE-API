package br.com.synchronize.Empresa;

import br.com.synchronize.Categorias.Role;
import br.com.synchronize.Infra.Security.TokenService;
import br.com.synchronize.Item.Item;
import br.com.synchronize.Item.ItemSimplesDTO;
import br.com.synchronize.Obra.ObraInfosDTO;
import br.com.synchronize.Obra.Obra;
import br.com.synchronize.User.UserInfoDTO;
import br.com.synchronize.User.UserSimplesDTO;
import br.com.synchronize.User.User;
import br.com.synchronize.User.UserRepository;
import jakarta.validation.Valid;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
public class EmpresaController {

    private static final Logger logger = LoggerFactory.getLogger(EmpresaController.class);

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

    //arrumado e vai ser usado
    //read
    @GetMapping("/empresa/{empresa_id}/info")
    public EmpresaDTO getEmpresa(@PathVariable String empresa_id) {
        EmpresaDTO empresa = empresaService.obterPorId(empresa_id);

        if(empresa != null)
            return empresa;

        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Empresa não encontrada com id fornecido");
    }

    //arrumado e usado
    //read
    @GetMapping("/obras")
    public List<ObraInfosDTO> getObrasInfo(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        Optional<Empresa> optionalEmpresa = empresaRepository.findById(user.getEmpresa().getId());

        if(optionalEmpresa.isPresent()) {
            Empresa empresa = optionalEmpresa.get();

            return getInfosObraDTO(authentication, empresa);
        }

        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Empresa não encontrada com id fornecido");
    }

    private static List<ObraInfosDTO> getInfosObraDTO(Authentication authentication, Empresa empresa) {
        User user = (User) authentication.getPrincipal();
        List<Obra> obras = new ArrayList<>();

        switch (user.getRole()){
            case ADMIN, FINANCEIRO:
                obras = empresa.getObras();
                break;

            case ENCARREGADO:
                obras = empresa.getObras().stream().filter(obra -> obra.getEncarregado().equals(user)).toList();
                break;
        }

        List<ObraInfosDTO> obrasInfo = new ArrayList<>();

        final int[] id = {1};
        obras.forEach(o -> {
                    obrasInfo.add(new ObraInfosDTO(id[0], o.getId(),o.getNome(), o.getValorTotal() ,o.getEncarregado().getNome(), o.getEncarregado().getId() , o.getItens().size(), o.getStatus()));
                    id[0]++;
                }
        );
        return obrasInfo;
    }

    //get itens
    @GetMapping("/itens")
    public List<ItemSimplesDTO> getItensInfoBasicas (Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        Optional<Empresa> optionalEmpresa = empresaRepository.findById(user.getEmpresa().getId());


        if (optionalEmpresa.isPresent()) {
            Empresa empresa = optionalEmpresa.get();
            List<ItemSimplesDTO> itens = new ArrayList<>();
            AtomicInteger numero = new AtomicInteger(1);

            try {
                switch (user.getRole()) {
                    case ADMIN, FINANCEIRO -> {
                        return empresa.getObras().stream()
                                .flatMap(obra -> obra.getItens().stream()
                                        .map(item -> new ItemSimplesDTO(
                                                numero.getAndIncrement(),
                                                item.getNome(),
                                                item.getLocalDeAplicacao(),
                                                item.getAreaTotal(),
                                                item.getValor(),
                                                item.getObra().getId(),
                                                item.getObra().getNome(),
                                                item.getObra().getEncarregado().getNome(),
                                                item.getObra().getEncarregado().getId(),
                                                item.getPreparacaoDesenvolvimentoArea(),
                                                item.getDesenvolvimentoPorcentagem(),
                                                item.getProtecaoDesenvolvimentoArea(),
                                                item.getProtecaoDesenvolvimentoPorcentagem(),
                                                item.getDesenvolvimentoPorcentagem(),
                                                item.getDesenvolvimentoArea(),
                                                item.getDataInicio(),
                                                item.getDataUltima(),
                                                item.getDataFinal(),
                                                item.getStatus()
                                        )))
                                .collect(Collectors.toList());
                    }
                    case ENCARREGADO -> {
                        return user.getObras().stream()
                                .flatMap(obra -> obra.getItens().stream()
                                        .map(item -> new ItemSimplesDTO(
                                                numero.getAndIncrement(),
                                                item.getNome(),
                                                item.getLocalDeAplicacao(),
                                                item.getAreaTotal(),
                                                item.getValor(),
                                                item.getObra().getId(),
                                                item.getObra().getNome(),
                                                item.getObra().getEncarregado().getNome(),
                                                item.getObra().getEncarregado().getId(),
                                                item.getPreparacaoDesenvolvimentoArea(),
                                                item.getDesenvolvimentoPorcentagem(),
                                                item.getProtecaoDesenvolvimentoArea(),
                                                item.getProtecaoDesenvolvimentoPorcentagem(),
                                                item.getDesenvolvimentoPorcentagem(),
                                                item.getDesenvolvimentoArea(),
                                                item.getDataInicio(),
                                                item.getDataUltima(),
                                                item.getDataFinal(),
                                                item.getStatus()
                                        )))
                                .collect(Collectors.toList());
                    }
                }
            } catch (Exception ex) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erro ao buscar itens");
            }
        }

        throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Empresa não cadastrada no sistema");
    }

    //get funcionarios
    @GetMapping("/funcionarios")
    public List<UserInfoDTO> getFuncionarios (Authentication authentication) {
        User user = (User) authentication.getPrincipal();

        logger.info("User Role: " + user.getRole());

        switch (user.getRole()) {
            case ENCARREGADO -> {
                logger.warn("Usuário sem permissão. Role: ENCARREGADO");
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Usuário sem permissão.");
            }
            case ADMIN, FINANCEIRO -> {
                try {
                    Empresa empresa = user.getEmpresa();
                    AtomicInteger numero = new AtomicInteger(1);
                    return empresaService.getFuncionarios(empresa.getId());

                } catch (Exception ex) {
                    logger.error("Erro ao obter funcionários", ex);
                    throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erro ao obter funcionários");
                }
            }
        }
        logger.error("Usuário sem Role válida. Role: " + user.getRole());
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Usuário com Role inválido");
    }

    //arrumado e usado
    //get
    @GetMapping("/encarregados")
    public List<UserSimplesDTO> getEncarregadosInfoBasicas (Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        Optional<Empresa> optionalEmpresa = empresaRepository.findById(user.getEmpresa().getId());

        if(optionalEmpresa.isPresent()) {
            Empresa empresa = optionalEmpresa.get();

            try {
                List<User> encarregados = empresa.getFuncionarios().stream().filter(f -> f.getRole().equals(Role.ENCARREGADO)).toList();
                List<UserSimplesDTO> encarregadosDTO = new ArrayList<>();
                encarregados.forEach(e -> encarregadosDTO.add(new UserSimplesDTO(e.getNome(), e.getId())));
                return encarregadosDTO;
            } catch (Exception ex) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erro a buscar encarregados");
            }
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Empresa não encontrada com id fornecido");
    }

    //arrumado e vai ser usado
    //update
    @PostMapping("/empresa/{empresa_id}/update")
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
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Falha ao alterar os dados da empresa: " + ex);
            }

        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Empresa não encontrada com o id: " + empresa_id);
    }

//delete
//    @DeleteMapping("/{empresa_id}/company-delete")
//    public ResponseEntity deleteCompany(@PathVariable String empresa_id) {
//        Optional<Empresa> optionalEmpresa = empresaRepository.findById(empresa_id);
//
//        if (optionalEmpresa.isPresent()) {
//            Empresa empresa = optionalEmpresa.get();
//            try {
//                empresaRepository.delete(empresa);
//                return ResponseEntity.ok().body("Empresa deletada com sucesso!");
//            } catch (Exception ex) {
//                return ResponseEntity.badRequest().body("Erro ao deletar empresa: " + ex);
//            }
//        }
//
//        return ResponseEntity.badRequest().body("Empresa não encontrada para o ID: " + empresa_id);
//    }

    //arrumado e vai ser usado
    @GetMapping("/{empresa_id}/valor-total")
    public Double getValores(@PathVariable String empresa_id) {
        Optional<Empresa> optionalEmpresa = empresaRepository.findById(empresa_id);

        if(optionalEmpresa.isPresent()) {
            Empresa empresa = optionalEmpresa.get();
            double[] valor = {0};

            List<Obra> obras = empresa.getObras();
            obras.forEach(o -> valor[0] += o.getValorTotal());

            return valor[0];
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Empresa não encontrada");
        }
    }

}
