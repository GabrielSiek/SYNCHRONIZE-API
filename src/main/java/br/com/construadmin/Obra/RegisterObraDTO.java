package br.com.construadmin.Obra;

import br.com.construadmin.Item.Item;

public record RegisterObraDTO(String nome,
                              String encarregado_id,
                              Item[] items) {
}
