package br.com.synchronize.Obra;

import br.com.synchronize.Categorias.Status;

public record ObraInfosDTO(Integer id,
                           String obra_id,
                           String nome,
                           Double valor,
                           String encarregado_nome,
                           String encarregado_id,
                           Integer itens,
                           Status status
                           ) {
}
