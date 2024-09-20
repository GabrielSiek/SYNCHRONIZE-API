package br.com.synchronize.Obra;

import br.com.synchronize.Item.Item;
import br.com.synchronize.Item.ItemDTO;

import java.util.List;

public record RegisterObraDTO(String nome,
                              String encarregado_id,
                              List<ItemDTO> items,
                              String empresa_id) {
}
