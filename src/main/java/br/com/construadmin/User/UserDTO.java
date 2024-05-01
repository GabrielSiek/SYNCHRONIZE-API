package br.com.construadmin.User;

import br.com.construadmin.Categorias.Role;
import br.com.construadmin.Obra.Obra;

import java.util.List;

public record UserDTO(String email,
                      String nome,
                      String password,
                      Role role,
                      List<Obra> obras) {
}
