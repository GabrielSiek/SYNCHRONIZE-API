package br.com.synchronize.User;

import br.com.synchronize.Categorias.Role;

public record UpdateUserDTO(String nome,
                            String email,
                            String password,
                            Role role) {
}
