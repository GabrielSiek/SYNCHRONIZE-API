package br.com.synchronize.Item;

import br.com.synchronize.Empresa.Empresa;
import br.com.synchronize.Obra.Obra;
import br.com.synchronize.Obra.ObraRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@RestController
public class ItemController {

    @Autowired
    ItemService itemService;

    @Autowired
    ObraRepository obraRepository;

    @Autowired
    ItemRepository itemRepository;

    //arrumado e vai ser usado
    //create
    @PostMapping("/{obra_id}/register-item")
    public ResponseEntity registerItem(@PathVariable String obra_id, @RequestBody @Valid RegisterItemDTO registerItemDTO) {
        Optional<Obra> optionalObra = obraRepository.findById(obra_id);

        if (optionalObra.isPresent()) {
            try {
                Obra obra = optionalObra.get();
                Empresa empresa = obra.getEmpresa();

                Item newItem = new Item(registerItemDTO.nome(), empresa, obra);

                itemRepository.save(newItem);

                return ResponseEntity.ok().body("Item cadastrado com sucesso!");
            } catch (Exception ex) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao cadastrar item: " + ex);
            }
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Obra não cadastrada no sistema com id: " + obra_id);
    }

    //arrumado e deve ser udado
    //read
    @GetMapping("/{item_id}/item-info")
    public ItemDTO getItem(@RequestBody String item_id) {
        ItemDTO item = itemService.obterPorId(item_id);
        if(item != null)
            return item;

        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Item não encontrado com id fornecido");
    }

    //arrumar e vai ser usado
    //update
//    @PutMapping("/{item_id}/item-update")
//    public ResponseEntity updateItem(@PathVariable String item_id, @RequestBody @Valid ItemDTO updateItemDTO) {
//        Optional<Item> optionalItem = itemRepository.findById(item_id);
//        Optional<Obra> optionalObra = obraRepository.findById(updateItemDTO.obra_id());
//
//
//        if (optionalItem.isPresent() && optionalObra.isPresent()) {
//            Item item = optionalItem.get();
//            Obra obra = optionalObra.get();
//
//            try {
//                if (!updateItemDTO.nome().equals(item.getNome()))
//                    item.setNome(updateItemDTO.nome());
//
//                if (!obra.equals(item.getObra()))
//                    item.setObra(obra);
//
//                if (!updateItemDTO.status().equals(item.getStatus()))
//                    item.setStatus(updateItemDTO.status());
//
//                itemRepository.save(item);
//
//                return ResponseEntity.ok().body("Dados do item atualizados com sucesso!");
//            } catch (Exception ex) {
//                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Falha ao mudar dados do item: " + ex);
//            }
//        }
//        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Item não encontrado com o ID: " + item_id);
//    }

    //arrumado e vai ser usado
    //delete
    @DeleteMapping("/{item_id}/item-delete")
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



