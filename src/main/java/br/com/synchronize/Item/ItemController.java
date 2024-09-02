package br.com.synchronize.Item;

import br.com.synchronize.Categorias.Status;
import br.com.synchronize.ItemRelatorio.ItemRelatorio;
import br.com.synchronize.ItemRelatorio.ItemRelatorioService;
import br.com.synchronize.Obra.ObraRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@RestController
public class ItemController {

    @Autowired
    ItemService itemService;

    @Autowired
    ItemRelatorioService itemRelatorioService;

    @Autowired
    ObraRepository obraRepository;

    @Autowired
    ItemRepository itemRepository;

    //arrumado e vai ser usado
    //create
//    @PostMapping("/{obra_id}/register-item")
//    public ResponseEntity registerItem(@PathVariable String obra_id, @RequestBody @Valid RegisterItemDTO registerItemDTO) {
//        Optional<Obra> optionalObra = obraRepository.findById(obra_id);
//
//        if (optionalObra.isPresent()) {
//            try {
//                Obra obra = optionalObra.get();
//                Empresa empresa = obra.getEmpresa();
//
//                Item newItem = new Item(registerItemDTO.nome(), empresa, obra);
//
//                itemRepository.save(newItem);
//
//                return ResponseEntity.ok().body("Item cadastrado com sucesso!");
//            } catch (Exception ex) {
//                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao cadastrar item: " + ex);
//            }
//        }
//
//        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Obra não cadastrada no sistema com id: " + obra_id);
//    }

    @GetMapping("/item/{item_id}/info")
    public ItemDTO getItem(@RequestBody String item_id) {
        ItemDTO item = itemService.obterPorId(item_id);
        if(item != null)
            return item;

        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Item não encontrado com id fornecido");
    }

    @PutMapping("/item/{item_id}/update")
    public ResponseEntity updateItem(@RequestBody UpdateItemValoresDTO updateItemValoresDTO, @PathVariable String item_id) {
        Optional<Item> optionalItem = itemRepository.findById(item_id);

        if(optionalItem.isPresent()) {
            Item item = optionalItem.get();

            try {
                Double preparacao_desenvolvimento_area = updateItemValoresDTO.preparacao_desenvolvimento_area();
                Double preparacao_desenvolvimento_porcentagem = updateItemValoresDTO.preparacao_desenvolvimento_porcentagem();
                Double protecao_desenvolvimento_area = updateItemValoresDTO.protecao_desenvolvimento_area();
                Double protecao_desenvolvimento_porcentagem = updateItemValoresDTO.protecao_desenvolvimento_porcentagem();
                Double desenvolvimento_area = updateItemValoresDTO.desenvolvimento_area();
                Double desenvolvimento_porcentagem = updateItemValoresDTO.desenvolvimento_porcentagem();
                Status status = updateItemValoresDTO.status();

                item.setPreparacaoDesenvolvimentoArea(preparacao_desenvolvimento_area);
                item.setPreparacaoDesenvolvimentoPorcentagem(preparacao_desenvolvimento_porcentagem);
                item.setProtecaoDesenvolvimentoArea(protecao_desenvolvimento_area);
                item.setProtecaoDesenvolvimentoPorcentagem(protecao_desenvolvimento_porcentagem);
                item.setDesenvolvimentoArea(desenvolvimento_area);
                item.setDesenvolvimentoPorcentagem(desenvolvimento_porcentagem);

                DateTimeFormatter formatacao = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                String data = LocalDate.now().format(formatacao);

                Optional<ItemRelatorio> optionalItemRelatorio = item.getDatas().stream()
                                .filter(itemRelatorio -> itemRelatorio.getData().equals(data))
                                        .findFirst();

                //atualizando valores
                if(optionalItemRelatorio.isPresent()){
                    ItemRelatorio itemRelatorio = optionalItemRelatorio.get();
                    itemRelatorio.setPreparacaoDesenvolvimentoArea(updateItemValoresDTO.preparacao_desenvolvimento_area());

                }
                else {
                    item.addItemRelatorio(
                            updateItemValoresDTO.preparacao_desenvolvimento_area(),
                            updateItemValoresDTO.preparacao_desenvolvimento_porcentagem(),
                            updateItemValoresDTO.protecao_desenvolvimento_area(),
                            updateItemValoresDTO.protecao_desenvolvimento_porcentagem(),
                            updateItemValoresDTO.desenvolvimento_area(),
                            updateItemValoresDTO.desenvolvimento_porcentagem()
                    );
                }

                if (updateItemValoresDTO.status().equals(Status.CONCLUIDO))
                    item.setStatus(Status.CONCLUIDO);
                else
                    item.setStatus(Status.NAO_CONCLUIDO);

                item.setDataFinal();


                itemRepository.save(item);

                return ResponseEntity.ok().body("Item atualizado com sucesso");
            } catch (Exception ex){
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao atualizar item");
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Item não encontrado pelo ID fornecido");
    }


    @DeleteMapping("/item/{item_id}/delete")
    public ResponseEntity deleteItem(@PathVariable String item_id) {
        Optional<Item> optionalItem = itemRepository.findById(item_id);

        if (optionalItem.isPresent()) {
            Item item = optionalItem.get();
            itemRepository.delete(item);

            return ResponseEntity.ok().body("Item deletado com sucesso!");
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Item não encontrado para o ID: " + item_id);
    }
}



