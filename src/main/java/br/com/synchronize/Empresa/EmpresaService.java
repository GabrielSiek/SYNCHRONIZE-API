package br.com.synchronize.Empresa;

import br.com.synchronize.User.User;
import br.com.synchronize.User.UserInfoDTO;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

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

    @Transactional
    public List<UserInfoDTO> getFuncionarios(String empresaId) {
        Empresa empresa = empresaRepository.findById(empresaId)
                .orElseThrow(() -> new RuntimeException("Empresa não encontrada"));

        // A lista de funcionários será carregada aqui
        List<User> funcionarios = empresa.getFuncionarios();

        AtomicInteger numero = new AtomicInteger(1);
        return funcionarios.stream()
                .map(funcionario -> new UserInfoDTO(
                        numero.getAndIncrement(),
                        funcionario.getId(),
                        funcionario.getNome(),
                        funcionario.getEmail(),
                        funcionario.getRole()))
                .collect(Collectors.toList());
    }
}
