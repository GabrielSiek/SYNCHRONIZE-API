package br.com.construadmin.Obra;

import br.com.construadmin.Categorias.Status;
import br.com.construadmin.Empresa.Empresa;
import br.com.construadmin.User.InfoUserDTO;
import br.com.construadmin.User.User;

public record InfosObraDTO(String nome,
                           Double valor,
                           InfoUserDTO encarregado,
                           Integer itens,
                           Status status
                           ) {
}
