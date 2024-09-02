package br.com.synchronize.Obra;

import br.com.synchronize.Categorias.Status;
import br.com.synchronize.Empresa.Empresa;
import br.com.synchronize.Empresa.EmpresaRepository;
import br.com.synchronize.Item.Item;
import br.com.synchronize.ItemRelatorio.ItemRelatorio;
import br.com.synchronize.Item.ItemRepository;
import br.com.synchronize.User.User;
import br.com.synchronize.User.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

    //arrumado e usado
    //create
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

                    DateTimeFormatter formatacao = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                    String data = LocalDateTime.now().format(formatacao);

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
                        item.setDataInicio(data);
                        item.getDatas().add(data);
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

    //arrumado e usado
    //read
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
                            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Não autorizado: Você não tem permissão para acessar esta obra.");
                     }
                 }
             } else {
                 throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Não autorizado: Você não tem permissão para acessar esta obra.");
             }
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Obra não encontrada.");
    }

    //arrumado e vai ser usado
    //update
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

                List<Item> novosItens = updateObraDTO.itens().stream()
                        .map(dto -> {
                            Optional<Item> itemTemporario = itemRepository.findById(dto.id());

                                String dataInicio = itemRepository.findById(dto.id()).get().getDataInicio();

                                List<ItemRelatorio> datas = itemTemporario.get().getDatas();
                                DateTimeFormatter formatacao = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                                String data = LocalDateTime.now().format(formatacao);
                                datas.add(data);

                                Item item =  new Item(
                                        dto.id(),
                                        dto.numero(),
                                        dto.local_de_aplicacao(),
                                        dto.nome(),
                                        dto.sistemas(),
                                        dto.tipo(),
                                        dto.quantidade(),
                                        dto.area_total(),
                                        dto.valor(),
                                        dto.valor_etapa(),
                                        dto.preparacao_tipo(),
                                        dto.preparacao_area_total(),
                                        dto.preparacao_valor(),
                                        dto.preparacao_valor_etapa(),
                                        dto.preparacao_desenvolvimento_area(),
                                        dto.preparacao_desenvolvimento_porcentagem(),
                                        dto.protecao_tipo(),
                                        dto.protecao_area_total(),
                                        dto.protecao_valor(),
                                        dto.protecao_valor_etapa(),
                                        dto.protecao_desenvolvimento_area(),
                                        dto.protecao_desenvolvimento_porcentagem(),
                                        dto.desenvolvimento_area(),
                                        dto.desenvolvimento_porcentagem(),
                                        dataInicio,
                                        datas,
                                        null,
                                        obra.getEmpresa(),
                                        obra,
                                        dto.status()
                                );

                                if(dto.status().equals(Status.CONCLUIDO))
                                    item.setDataFinal(data);
                                else if(item.getStatus().equals(Status.CONCLUIDO))
                                    item.setStatus(Status.NAO_CONCLUIDO);

                                return item;
                        })
                        .collect(Collectors.toList());

                    obra.setItens(novosItens);

                    obraRepository.save(obra);

                return ResponseEntity.ok().body("Dados da obra atualizados com sucesso!");
            } catch (Exception ex) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Falha ao alterar dados da obra: " + ex);
            }
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Id de obra ou de encarregado não encontrado");
    }

    //arrumado e vai ser usado
    //delete
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

    //testes

    //arrumado e acho que to usando
    //get valor total
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
