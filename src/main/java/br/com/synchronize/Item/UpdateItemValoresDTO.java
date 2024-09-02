package br.com.synchronize.Item;

import br.com.synchronize.Categorias.Status;

public record UpdateItemValoresDTO(
                                   Double preparacao_desenvolvimento_area,
                                   Double preparacao_desenvolvimento_porcentagem,
                                   Double protecao_desenvolvimento_area,
                                   Double protecao_desenvolvimento_porcentagem,
                                   Double desenvolvimento_area,
                                   Double desenvolvimento_porcentagem,
                                   Status status
) {
}
