package br.com.synchronize.ItemRelatorio;


import java.time.LocalDate;

public record ItemRelatorioDTO(String id,
                               LocalDate data,
                               Double preparacaoDesenvolvimentoArea,
                               Double preparacaoDesenvolvimentoPorcentagem,
                               Double protecaoDesenvolvimentoArea,
                               Double protecaoDesenvolvimentoPorcentagem,
                               Double desenvolvimentoArea,
                               Double desenvolvimentoPorcentagem
) {}
