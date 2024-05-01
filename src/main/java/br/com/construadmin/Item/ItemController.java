package br.com.construadmin.Item;

import br.com.construadmin.Categorias.UpdateStatus;
import br.com.construadmin.Empresa.Empresa;
import br.com.construadmin.Obra.Obra;
import br.com.construadmin.Obra.ObraRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
public class ItemController {

    @Autowired
    ItemService itemService;

    @Autowired
    ObraRepository obraRepository;

    @Autowired
    ItemRepository itemRepository;

    //create
    @PostMapping("/{obra_id}/register-item")
    public ResponseEntity registerItem(@PathVariable String obra_id, @RequestBody @Valid RegisterItemDTO registerItemDTO) {
        Optional<Obra> optionalObra = obraRepository.findById(obra_id);

        if (optionalObra.isPresent()) {
            try {
                Obra obra = optionalObra.get();
                Empresa empresa = obra.getEmpresa();

                Item newItem = new Item(registerItemDTO.nome(), empresa, obra, registerItemDTO.valor());

                itemRepository.save(newItem);

                return ResponseEntity.ok().body("Item cadastrado com sucesso!");
            } catch (Exception ex) {
                return ResponseEntity.badRequest().body("Erro ao cadastrar item: " + ex);
            }
        }

        return ResponseEntity.badRequest().body("Obra não cadastrada no sistema com id: " + obra_id);
    }

    //read
    @GetMapping("/{item_id}/item-info")
    public ItemDTO getItem(@RequestBody String item_id) {
        return itemService.obterPorId(item_id);
    }

    //update
    @PutMapping("/{item_id}/item-update")
    public ResponseEntity updateItem(@PathVariable String item_id, @RequestBody @Valid UpdateItemDTO updateItemDTO) {
        Optional<Item> optionalItem = itemRepository.findById(item_id);
        Optional<Obra> optionalObra = obraRepository.findById(updateItemDTO.obra_id());


        if (optionalItem.isPresent() && optionalObra.isPresent()) {
            Item item = optionalItem.get();
            Obra obra = optionalObra.get();

            try {
                if (!updateItemDTO.nome().equals(item.getNome()))
                    item.setNome(updateItemDTO.nome());

                if (!obra.equals(item.getObra()))
                    item.setObra(obra);

                if (!updateItemDTO.status().equals(item.getStatus()))
                    item.setStatus(updateItemDTO.status());

                if (!updateItemDTO.valor().equals(item.getValor()))
                    item.setValor(updateItemDTO.valor());

                itemRepository.save(item);

                return ResponseEntity.ok().body("Dados do item atualizados com sucesso!");
            } catch (Exception ex) {
                return ResponseEntity.badRequest().body("Falha ao mudar dados do item: " + ex);
            }
        }
        return ResponseEntity.badRequest().body("Item não encontrado com o ID: " + item_id);
    }

    //update status item
    @PutMapping("/{item_id}/item-update-status")
    public ResponseEntity uptdateItemStatus(@PathVariable String item_id, @RequestBody @Valid UpdateStatus updateStatus) {
        Optional<Item> optionalItem = itemRepository.findById(item_id);

        if (optionalItem.isPresent()) {
            Item item = optionalItem.get();

            try {
                item.setStatus(updateStatus.status());

                itemRepository.save(item);

                return ResponseEntity.ok().body("Status do item atualizado com sucesso!");
            } catch (Exception ex) {
                return ResponseEntity.badRequest().body("Falha ao alterar status do item: " + ex);
            }
        }

        return ResponseEntity.badRequest().body("Item não encontrada com o ID: " + item_id);
    }


    //delete
    @DeleteMapping("/{item_id}/item-delete")
    public ResponseEntity deleteItem(@PathVariable String item_id) {
        Optional<Item> optionalItem = itemRepository.findById(item_id);

        if (optionalItem.isPresent()) {
            Item item = optionalItem.get();
            itemRepository.delete(item);

            return ResponseEntity.ok().body("Item deletado com sucesso!");
        }

        return ResponseEntity.badRequest().body("Item não encontrado para o ID: " + item_id);
    }
}



