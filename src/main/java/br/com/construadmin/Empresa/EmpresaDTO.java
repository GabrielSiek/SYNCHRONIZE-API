package br.com.construadmin.Empresa;

import br.com.construadmin.Obra.Obra;
import br.com.construadmin.User.User;

import java.util.List;

public record EmpresaDTO(String nome,
                         List<User> funcionarios,
                         List<Obra> obras) {
}
