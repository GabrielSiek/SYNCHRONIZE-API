package br.com.construadmin.User;

import br.com.construadmin.Categorias.Role;

public record RegisterUserDTO(String email,
                              String nome,
                              String password,
                              Role role) {
}
