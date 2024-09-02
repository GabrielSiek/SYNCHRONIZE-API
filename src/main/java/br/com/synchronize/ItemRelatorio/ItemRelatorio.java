package br.com.synchronize.ItemRelatorio;

import br.com.synchronize.Item.Item;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

import javax.swing.text.DateFormatter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Entity(name = "itens_relatorio")
@Table(name = "itens_relatorio")
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@AllArgsConstructor
@NoArgsConstructor
public class ItemRelatorio {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
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

    public void setData() {
        DateTimeFormatter formatacao = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String data = LocalDate.now().format(formatacao);
        this.data = data;
    }
}
