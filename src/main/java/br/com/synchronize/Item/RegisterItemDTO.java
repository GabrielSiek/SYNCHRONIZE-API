package br.com.synchronize.Item;

import br.com.synchronize.Categorias.Status;

public record RegisterItemDTO(
                              String local_de_aplicacao,
                              String nome,
                              String sistemas,
                              Integer tipo,
                              Integer quantidade,
                              Double area_total,
                              Double valor,
                              Double valor_etapa,
                              Integer preparacao_tipo,
                              Double preparacao_area_total,
                              Double preparacao_valor,
                              Double preparacao_valor_etapa,
                              Integer protecao_tipo,
                              Double protecao_area_total,
                              Double protecao_valor,
                              Double protecao_valor_etapa
) {
}
