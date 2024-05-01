package br.com.construadmin.Obra;

import br.com.construadmin.User.User;
import br.com.construadmin.User.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ObraService {

    @Autowired
    private ObraRepository obraRepository;

    @Autowired
    private UserRepository userRepository;


    public List<ObraDTO> obterObrasPorWorkerId(String workerId) {
        Optional<User> optionalUser = userRepository.findById(workerId);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            return user.getObras().stream()
                    .map(o -> new ObraDTO(o.getNome(), o.getStatus(), o.getEmpresa(), o.getItens(), o.getEncarregado()))
                    .collect(Collectors.toList());
        }

        return null;
    }

    public ObraDTO obterPorId(String obraId) {
        Optional<Obra> optionalObra = obraRepository.findById(obraId);

        if (optionalObra.isPresent()) {
            Obra obra = optionalObra.get();
            return new ObraDTO(obra.getNome(), obra.getStatus(), obra.getEmpresa(), obra.getItens(), obra.getEncarregado());
        }

        return null;
    }
}
