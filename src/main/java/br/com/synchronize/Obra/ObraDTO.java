package br.com.synchronize.Obra;

import br.com.synchronize.Categorias.Status;
import br.com.synchronize.Item.ItemDTO;

import java.util.List;

public record ObraDTO(String nome,
                      Double valorTotal,
                      String encarregado_nome,
                      String encarregado_id,
                      Integer itensTotal,
                      List<ItemDTO> itens,
                      Status status) {
}
