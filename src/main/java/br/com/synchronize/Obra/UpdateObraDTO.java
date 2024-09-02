package br.com.synchronize.Obra;

import br.com.synchronize.Categorias.Status;
import br.com.synchronize.Item.ItemDTO;
import br.com.synchronize.Item.UpdateItemDTO;

import java.util.List;

public record UpdateObraDTO(String nome,
                            Status status,
                            List<UpdateItemDTO> itens
                            ) {
}
