package br.com.construadmin.Obra;

import br.com.construadmin.Categorias.Status;
import br.com.construadmin.Empresa.Empresa;
import br.com.construadmin.Item.Item;
import br.com.construadmin.User.User;

import java.util.List;

public record ObraDTO(String nome,
                      Status status,
                      Empresa empresa,
                      List<Item> itens,
                      User encarregado) {
}
