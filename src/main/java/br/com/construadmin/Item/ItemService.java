package br.com.construadmin.Item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ItemService {

    @Autowired
    private ItemRepository itemRepository;

    public ItemDTO obterPorId(String itemId) {
        Optional<Item> optionalItem = itemRepository.findById(itemId);

        if (optionalItem.isPresent()) {
            Item item = optionalItem.get();
            return new ItemDTO(item.getNome(), item.getEmpresa(), item.getObra());
        }

        return null;
    }
}
