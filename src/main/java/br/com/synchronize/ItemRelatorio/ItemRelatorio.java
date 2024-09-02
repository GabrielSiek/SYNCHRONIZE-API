package br.com.synchronize.ItemRelatorio;

import br.com.synchronize.Item.Item;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity(name = "itens_relatorio")
@Table(name = "itens_relatorio")
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@AllArgsConstructor
@NoArgsConstructor
public class ItemRelatorio {

    private String id;

    private String data;

    @JsonProperty("preparacao_desenvolvimento_area")
    private Double preparacaoDesenvolvimentoArea;

    @JsonProperty("preparacao_desenvolvimento_porcentagem")
    private Double preparacaoDesenvolvimentoPorcentagem;

    @JsonProperty("protecao_desenvolvimento_area")
    private Double protecaoDesenvolvimentoArea;

    @JsonProperty("protecao_desenvolvimento_porcentagem")
    private Double protecaoDesenvolvimentoPorcentagem;

    @JsonProperty("desenvolvimento_area")
    private Double desenvolvimentoArea;

    @JsonProperty("desenvolvimento_porcentagem")
    private Double desenvolvimentoPorcentagem;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;
}
