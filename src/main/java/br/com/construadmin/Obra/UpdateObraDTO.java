package br.com.construadmin.Obra;

import br.com.construadmin.Categorias.Status;

public record UpdateObraDTO(String nome,
                            Status status,
                            String encarregado_id) {
}
