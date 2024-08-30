package br.com.synchronize.User;

import br.com.synchronize.Categorias.Role;
import br.com.synchronize.Obra.Obra;

import java.util.List;

public record UserDTO(String email,
                      String nome,
                      String password,
                      Role role,
                      List<Obra> obras) {
}
