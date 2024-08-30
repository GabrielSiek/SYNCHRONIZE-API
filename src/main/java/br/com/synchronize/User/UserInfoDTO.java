package br.com.synchronize.User;

import br.com.synchronize.Categorias.Role;

public record UserInfoDTO(Integer id,
                          String user_id,
                          String nome,
                          String email,
                          Role role
                          ) {
}
