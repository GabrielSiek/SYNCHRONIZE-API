package br.com.synchronize.Obra;

import br.com.synchronize.Item.ItemDTO;
import br.com.synchronize.User.User;
import br.com.synchronize.User.UserRepository;
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


    public List<ObraDTO> obterObrasPorUserId(String workerId) {
        Optional<User> optionalUser = userRepository.findById(workerId);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            return user.getObras().stream()
                    .map(obra -> {
                        // Cria uma lista de ItemInfosBasicasDTO para cada obra
                        List<ItemDTO> itemInfos = obra.getItens().stream()
                                .map(item -> new ItemDTO(
                                        item.getId(),
                                        item.getNumero(),
                                        item.getLocalDeAplicacao(),
                                        item.getNome(),
                                        item.getSistemas(),
                                        item.getTipo(),
                                        item.getQuantidade(),
                                        item.getAreaTotal(),
                                        item.getValor(),
                                        item.getValorEtapa(),
                                        item.getPreparacaoTipo(),
                                        item.getPreparacaoAreaTotal(),
                                        item.getPreparacaoValor(),
                                        item.getPreparacaoValorEtapa(),
                                        item.getPreparacaoDesenvolvimentoArea(),
                                        item.getPreparacaoDesenvolvimentoPorcentagem(),
                                        item.getProtecaoTipo(),
                                        item.getProtecaoAreaTotal(),
                                        item.getProtecaoValor(),
                                        item.getProtecaoValorEtapa(),
                                        item.getProtecaoDesenvolvimentoArea(),
                                        item.getProtecaoDesenvolvimentoPorcentagem(),
                                        item.getDesenvolvimentoArea(),
                                        item.getDesenvolvimentoPorcentagem(),
                                        item.getDataInicio(),
                                        item.getDataUltima(),
                                        item.getDataFinal(),
                                        item.getStatus()))
                                .collect(Collectors.toList());

                        // Cria e retorna o ObraDTO com a lista de ItemInfosBasicasDTO
                        return new ObraDTO(
                                obra.getNome(),
                                obra.getValorTotal(),
                                obra.getEncarregado().getNome(),
                                obra.getEncarregado().getId(),
                                itemInfos.size(), // Tamanho da lista de itens
                                itemInfos, // Lista de ItemInfosBasicasDTO
                                obra.getStatus()
                        );
                    })
                    .collect(Collectors.toList());
        }

        // Retorna uma lista vazia se o usuário não for encontrado
        return null;
    }


    public ObraDTO obterPorId(String obraId) {
        Optional<Obra> optionalObra = obraRepository.findById(obraId);

        if (optionalObra.isPresent()) {
            Obra obra = optionalObra.get();
            List<ItemDTO> itensDTO = obra.getItens().stream()
                    .map(item -> new ItemDTO(
                            item.getId(),
                            item.getNumero(),
                            item.getLocalDeAplicacao(),
                            item.getNome(),
                            item.getSistemas(),
                            item.getTipo(),
                            item.getQuantidade(),
                            item.getAreaTotal(),
                            item.getValor(),
                            item.getValorEtapa(),
                            item.getPreparacaoTipo(),
                            item.getPreparacaoAreaTotal(),
                            item.getPreparacaoValor(),
                            item.getPreparacaoValorEtapa(),
                            item.getPreparacaoDesenvolvimentoArea(),
                            item.getPreparacaoDesenvolvimentoPorcentagem(),
                            item.getProtecaoTipo(),
                            item.getProtecaoAreaTotal(),
                            item.getProtecaoValor(),
                            item.getProtecaoValorEtapa(),
                            item.getProtecaoDesenvolvimentoArea(),
                            item.getProtecaoDesenvolvimentoPorcentagem(),
                            item.getDesenvolvimentoArea(),
                            item.getDesenvolvimentoPorcentagem(),
                            item.getDataInicio(),
                            item.getDataUltima(),
                            item.getDataFinal(),
                            item.getStatus()))
                    .collect(Collectors.toList());

            return new ObraDTO(obra.getNome(), obra.getValorTotal(), obra.getEncarregado().getNome(), obra.getEncarregado().getId(), obra.getItens().size(), itensDTO, obra.getStatus());
        }

        return null;
    }
}
