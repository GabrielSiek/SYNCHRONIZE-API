package br.com.synchronize.ItemRelatorio;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ItemRelatorioDTO(String id,
                               String data,
                               Double preparacaoDesenvolvimentoArea,
                               Double preparacaoDesenvolvimentoPorcentagem,
                               Double protecaoDesenvolvimentoArea,
                               Double protecaoDesenvolvimentoPorcentagem,
                               Double desenvolvimentoArea,
                               Double desenvolvimentoPorcentagem
) {}
