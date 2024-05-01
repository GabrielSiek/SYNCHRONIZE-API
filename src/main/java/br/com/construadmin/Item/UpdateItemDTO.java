package br.com.construadmin.Item;

import br.com.construadmin.Categorias.Status;

public record UpdateItemDTO(String nome,
                            String obra_id,
                            Status status,
                            String valor) {
}
