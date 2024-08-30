package br.com.synchronize.Obra;

import br.com.synchronize.Item.Item;

import java.util.List;

public record RegisterObraDTO(String nome,
                              String encarregado_id,
                              List<Item> items,
                              String empresa_id) {
}
