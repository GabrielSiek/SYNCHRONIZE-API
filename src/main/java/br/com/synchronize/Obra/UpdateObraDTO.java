package br.com.synchronize.Obra;

import br.com.synchronize.Categorias.Status;
import br.com.synchronize.Item.ItemDTO;

import java.util.List;

public record UpdateObraDTO(String nome,
                            Status status,
                            List<ItemDTO> itens
                            ) {
}
