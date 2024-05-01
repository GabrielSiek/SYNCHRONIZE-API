package br.com.construadmin.Empresa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EmpresaService {

    @Autowired
    private EmpresaRepository empresaRepository;

    public EmpresaDTO obterPorId(String id) {
        Optional<Empresa> empresa = empresaRepository.findById(id);

        if (empresa.isPresent()) {
            Empresa e = empresa.get();
            return new EmpresaDTO(e.getNome(), e.getFuncionarios(), e.getObras());
        }

        return null;
    }
}
