package br.com.synchronize.Item;

import br.com.synchronize.Categorias.Status;

public record ItemDTO(String id,
                      Integer numero,
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
                      Double preparacao_desenvolvimento_area,
                      Double preparacao_desenvolvimento_porcentagem,
                      Integer protecao_tipo,
                      Double protecao_area_total,
                      Double protecao_valor,
                      Double protecao_valor_etapa,
                      Double protecao_desenvolvimento_area,
                      Double protecao_desenvolvimento_porcentagem,
                      Double desenvolvimento_area,
                      Double desenvolvimento_porcentagem,
                      String data_inicio,
                      String data_ultima,
                      String data_final,
                      Status status
                                  ) {
}
