package br.com.construadmin.Item;

import br.com.construadmin.Empresa.Empresa;
import br.com.construadmin.Obra.Obra;

public record ItemDTO(String nome,
                      Empresa empresa,
                      Obra obra) {
}
