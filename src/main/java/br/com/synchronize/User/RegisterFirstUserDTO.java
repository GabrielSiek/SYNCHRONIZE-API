package br.com.synchronize.User;

import br.com.synchronize.Categorias.Role;

public record RegisterFirstUserDTO(String nome,
                                   String email,
                                   String password,
                                   String empresa_nome) {

}
