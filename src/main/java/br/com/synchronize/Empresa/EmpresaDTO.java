package br.com.synchronize.Empresa;

import br.com.synchronize.Obra.Obra;
import br.com.synchronize.User.User;

import java.util.List;

public record EmpresaDTO(String nome,
                         List<User> funcionarios,
                         List<Obra> obras) {
}
