package br.com.synchronize.Item;

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
            return new ItemDTO(
                    item.getId(),
                    item.getNumero(),
                    item.getLocalDeAplicacao(),
                    item.getNome(),
                    item.getSistemas(),
                    item.getTipo(),
                    item.getQuantidade(),
                    item.getAreaTotal(),
                    item.getValor(),
                    item.getValorEtapa(),
                    item.getPreparacaoTipo(),
                    item.getPreparacaoAreaTotal(),
                    item.getPreparacaoValor(),
                    item.getPreparacaoValorEtapa(),
                    item.getPreparacaoDesenvolvimentoArea(),
                    item.getPreparacaoDesenvolvimentoPorcentagem(),
                    item.getProtecaoTipo(),
                    item.getProtecaoAreaTotal(),
                    item.getProtecaoValor(),
                    item.getProtecaoValorEtapa(),
                    item.getProtecaoDesenvolvimentoArea(),
                    item.getProtecaoDesenvolvimentoPorcentagem(),
                    item.getDesenvolvimentoArea(),
                    item.getDesenvolvimentoPorcentagem(),
                    item.getDataInicio(),
                    item.getDataUltima(),
                    item.getDataFinal(),
                    item.getStatus());
        }

        return null;
    }
}
