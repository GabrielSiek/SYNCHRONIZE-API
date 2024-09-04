package br.com.synchronize.Item;

import br.com.synchronize.Categorias.Status;

import java.time.LocalDate;

public record ItemSimplesDTO(Integer id,
                             String nome,
                             String local_de_aplicacao,
                             Double area_total,
                             Double valor,
                             String obra_id,
                             String obra_nome,
                             String encarregado_nome,
                             String encarregado_id,
                             Double preparacao_desenvolvimento_area,
                             Double preparacao_desenvolvimento_porcentagem,
                             Double protecao_desenvolvimento_area,
                             Double protecao_desenvolvimento_porcentagem,
                             Double desenvolvimento_area,
                             Double desenvolvimento_porcentagem,
                             LocalDate data_inicio,
                             LocalDate data_ultima,
                             LocalDate data_final,
                             Status status
                             ) {
}
