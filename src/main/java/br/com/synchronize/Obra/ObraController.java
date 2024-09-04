package br.com.synchronize.Obra;

import br.com.synchronize.Categorias.Status;
import br.com.synchronize.Empresa.Empresa;
import br.com.synchronize.Empresa.EmpresaRepository;
import br.com.synchronize.Item.Item;
import br.com.synchronize.Item.ItemRepository;
import br.com.synchronize.ItemRelatorio.ItemRelatorioDTO;
import br.com.synchronize.User.User;
import br.com.synchronize.User.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


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

    @Autowired
    ItemRepository itemRepository;

    @PostMapping("/register-obra")
    public ResponseEntity registerJob(@RequestBody @Valid RegisterObraDTO registerObraDTO, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        Optional<Empresa> optionalEmpresa = empresaRepository.findById(user.getEmpresa().getId());
        Optional<User> optionalUser = userRepository.findById(registerObraDTO.encarregado_id());

        if (optionalEmpresa.isPresent() && optionalUser.isPresent()) {
            try {
                Empresa empresa = optionalEmpresa.get();
                User encarregado = optionalUser.get();

                Obra newObra = new Obra(registerObraDTO.nome(), empresa, encarregado);

                if (!registerObraDTO.items().isEmpty()) {

                    List<Item> items = registerObraDTO.items().stream().map(itemDTO -> {
                        Item item = new Item();
                        // Defina os atributos do item a partir do DTO
                        item.setNumero(itemDTO.getNumero());
                        item.setLocalDeAplicacao(itemDTO.getLocalDeAplicacao());
                        item.setNome(itemDTO.getNome());
                        item.setSistemas(itemDTO.getSistemas());
                        item.setTipo(itemDTO.getTipo());
                        item.setQuantidade(itemDTO.getQuantidade());
                        item.setAreaTotal(itemDTO.getAreaTotal());
                        item.setValor(itemDTO.getValor());
                        item.setValorEtapa(itemDTO.getValorEtapa());
                        item.setPreparacaoTipo(itemDTO.getPreparacaoTipo());
                        item.setPreparacaoAreaTotal(itemDTO.getPreparacaoAreaTotal());
                        item.setPreparacaoValor(itemDTO.getPreparacaoValor());
                        item.setPreparacaoValorEtapa(itemDTO.getPreparacaoValorEtapa());
                        item.setProtecaoTipo(itemDTO.getProtecaoTipo());
                        item.setProtecaoAreaTotal(itemDTO.getProtecaoAreaTotal());
                        item.setProtecaoValor(itemDTO.getProtecaoValor());
                        item.setProtecaoValorEtapa(itemDTO.getProtecaoValorEtapa());
                        item.setEmpresa(newObra.getEmpresa());
                        item.setStatus(Status.NAO_CONCLUIDO);
                        item.setPreparacaoDesenvolvimentoArea(0.0);
                        item.setPreparacaoDesenvolvimentoPorcentagem(0.0);
                        item.setProtecaoDesenvolvimentoArea(0.0);
                        item.setProtecaoDesenvolvimentoPorcentagem(0.0);
                        item.setDesenvolvimentoArea(0.0);
                        item.setDesenvolvimentoPorcentagem(0.0);
                        item.startDatas();
                        item.setObra(newObra);
                        return item;
                    }).collect(Collectors.toList());

                    newObra.setItens(items);
                }

                obraRepository.save(newObra);
                return ResponseEntity.status(HttpStatus.ACCEPTED).body("Obra cadastrada com sucesso!");
            } catch (Exception ex) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao cadastrar obra");
            }
        } else
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Empresa ou encarregado não cadastrado no sistema");
    }

    @GetMapping("/obra/{obra_id}")
    public ObraDTO getObra(@PathVariable String obra_id, Authentication authentication) {
        Optional<Obra> optionalObra = obraRepository.findById(obra_id);
        User user = (User) authentication.getPrincipal();;

        if(optionalObra.isPresent()) {
             Obra obra = optionalObra.get();

             if(user.getEmpresa().getId().equals(obra.getEmpresa().getId())){
                 switch (user.getRole()) {
                     case ADMIN, FINANCEIRO -> {
                         return obraService.obterPorId(obra_id);
                     }
                     case ENCARREGADO -> {
                         if (obra.getEncarregado().getId().equals(user.getId()))
                             return obraService.obterPorId(obra_id);
                         else
                            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Não autorizado: Você não tem permissão para acessar esta obra.");
                     }
                 }
             } else {
                 throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Não autorizado: Você não tem permissão para acessar esta obra.");
             }
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Obra não encontrada.");
    }

    @GetMapping("/obra/itens-relatorio/{obra_id}")
    public List<ItemRelatorioDTO> getItemRelatorios(@PathVariable String obra_id, @RequestBody LocalDate data_inicio, @RequestBody LocalDate data_final) {
        Optional<Obra> optionalObra = obraRepository.findById(obra_id);

        if(optionalObra.isPresent()) {
            List<Item> itens = optionalObra.get().getItens();
            return itens.stream()
                    .flatMap(item -> item.getDatas().stream()
                            .map(data ->
                                new ItemRelatorioDTO(
                                        data.getId(),
                                        data.getData(),
                                        data.getPreparacaoDesenvolvimentoArea(),
                                        data.getPreparacaoDesenvolvimentoPorcentagem(),
                                        data.getProtecaoDesenvolvimentoArea(),
                                        data.getProtecaoDesenvolvimentoPorcentagem(),
                                        data.getDesenvolvimentoArea(),
                                        data.getDesenvolvimentoPorcentagem())))
                    .filter(dto -> {

                        LocalDate data = dto.data();

                        return (data.isEqual(data_inicio) || data.isAfter(data_inicio) &&
                                data.isEqual(data_final) || data.isBefore(data_final));
                    })
                    .collect(Collectors.toList());
        }

        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Obra não encontrada com ID fornecido");
    }

    @PutMapping("/obra/{obra_id}/update")
    public ResponseEntity updateJob(@PathVariable String obra_id, @RequestBody @Valid UpdateObraDTO updateObraDTO) {
        Optional<Obra> optionalObra = obraRepository.findById(obra_id);

        if (optionalObra.isPresent()) {
            Obra obra = optionalObra.get();

            try {

                // Verifica e atualiza os campos da obra
                if (!updateObraDTO.nome().equals(obra.getNome())) {
                    obra.setNome(updateObraDTO.nome());
                }

                if (!updateObraDTO.status().equals(obra.getStatus())) {
                    obra.setStatus(updateObraDTO.status());
                    if(obra.getStatus().equals(Status.CONCLUIDO)) {
                        DateTimeFormatter formatacao = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                        String data = LocalDateTime.now().format(formatacao);
                        obra.setDataFinal(data);
                    } else if(!obra.getDataFinal().equals(null)) {
                        obra.setDataFinal(null);
                    }
                }
                    obraRepository.save(obra);

                return ResponseEntity.ok().body("Dados da obra atualizados com sucesso!");
            } catch (Exception ex) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Falha ao alterar dados da obra: " + ex);
            }
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Id de obra ou de encarregado não encontrado");
    }

    //vai ser usado
    @DeleteMapping("/obra/{obra_id}/delete")
    public ResponseEntity deleteJob(@PathVariable String obra_id) {
        Optional<Obra> optionalObra = obraRepository.findById(obra_id);

        if (optionalObra.isPresent()) {
            Obra obra = optionalObra.get();
            try {
                obraRepository.delete(obra);

                return ResponseEntity.ok().body("Obra deletada com sucesso!");
            } catch (Exception ex) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao deletar obra do sistema: " + ex);
            }
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Obra não encontrada para o ID: " + obra_id);
    }

    //acho que to usando
    @GetMapping("/obra/{obra_id}/valor-total")
    public Double getValorTotal(@PathVariable String obra_id) {
        Optional <Obra> optionalObra = obraRepository.findById(obra_id);

        if(optionalObra.isPresent()){
            Obra obra = optionalObra.get();

            return obra.getValorTotal();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Obra não encontrada com id fornecido");
        }
    }
}
