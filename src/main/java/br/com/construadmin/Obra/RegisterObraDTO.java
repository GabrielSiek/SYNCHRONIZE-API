package br.com.construadmin.Obra;

import br.com.construadmin.Item.Item;

public record RegisterObraDTO(String nome,
                              String encarregado_id,
                              String empresa_id) {
}
