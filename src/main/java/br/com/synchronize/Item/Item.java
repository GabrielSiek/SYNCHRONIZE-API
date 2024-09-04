package br.com.synchronize.Item;

import br.com.synchronize.Categorias.Status;
import br.com.synchronize.Empresa.Empresa;
import br.com.synchronize.ItemRelatorio.ItemRelatorio;
import br.com.synchronize.Obra.Obra;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Entity(name = "itens")
@Table(name = "itens")
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@AllArgsConstructor
@NoArgsConstructor
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private Integer numero;

    @JsonProperty("local_de_aplicacao")
    private String localDeAplicacao;

    private String nome;

    private String sistemas;

    private Integer tipo;

    private Integer quantidade;

    @JsonProperty("area_total")
    private Double areaTotal;

    private Double valor;

    @JsonProperty("valor_etapa")
    private Double valorEtapa;

    @JsonProperty("preparacao_tipo")
    private Integer preparacaoTipo;

    @JsonProperty("preparacao_area_total")
    private Double preparacaoAreaTotal;

    @JsonProperty("preparacao_valor")
    private Double preparacaoValor;

    @JsonProperty("preparacao_valor_etapa")
    private Double preparacaoValorEtapa;

    @JsonProperty("preparacao_desenvolvimento_area")
    private Double preparacaoDesenvolvimentoArea;

    @JsonProperty("preparacao_desenvolvimento_porcentagem")
    private Double preparacaoDesenvolvimentoPorcentagem;

    @JsonProperty("protecao_tipo")
    private Integer protecaoTipo;

    @JsonProperty("protecao_area_total")
    private Double protecaoAreaTotal;

    @JsonProperty("protecao_valor")
    private Double protecaoValor;

    @JsonProperty("protecao_valor_etapa")
    private Double protecaoValorEtapa;

    @JsonProperty("protecao_desenvolvimento_area")
    private Double protecaoDesenvolvimentoArea;

    @JsonProperty("protecao_desenvolvimento_porcentagem")
    private Double protecaoDesenvolvimentoPorcentagem;

    @JsonProperty("desenvolvimento_area")
    private Double desenvolvimentoArea;

    @JsonProperty("desenvolvimento_porcentagem")
    private Double desenvolvimentoPorcentagem;

    @JsonProperty("data_inicio")
    private String dataInicio;

    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<ItemRelatorio> datas;

    @JsonProperty("data_final")
    private String dataFinal;

    @ManyToOne
    private Empresa empresa;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "obra_id")
    private Obra obra;

    @Enumerated(EnumType.STRING)
    private Status status;

    public String getDataUltima() {
        if(datas.size() <= 0)
            return null;

        return datas.get(datas.size() -1).getData();
    }

    @Override
    public String toString() {
        return "Item{" +
                "id='" + id + '\'' +
                ", numero=" + numero +
                ", localDeAplicacao='" + localDeAplicacao + '\'' +
                ", nome='" + nome + '\'' +
                ", sistemas='" + sistemas + '\'' +
                ", tipo=" + tipo +
                ", quantidade=" + quantidade +
                ", areaTotal=" + areaTotal +
                ", valor=" + valor +
                ", valorEtapa=" + valorEtapa +
                ", preparacaoTipo=" + preparacaoTipo +
                ", preparacaoAreaTotal=" + preparacaoAreaTotal +
                ", preparacaoValor=" + preparacaoValor +
                ", preparacaoValorEtapa=" + preparacaoValorEtapa +
                ", preparacaoDesenvolvimentoArea=" + preparacaoDesenvolvimentoArea +
                ", preparacaoDesenvolvimentoPorcentagem=" + preparacaoDesenvolvimentoPorcentagem +
                ", protecaoTipo=" + protecaoTipo +
                ", protecaoAreaTotal=" + protecaoAreaTotal +
                ", protecaoValor=" + protecaoValor +
                ", protecaoValorEtapa=" + protecaoValorEtapa +
                ", protecaoDesenvolvimentoArea=" + protecaoDesenvolvimentoArea +
                ", protecaoDesenvolvimentoPorcentagem=" + protecaoDesenvolvimentoPorcentagem +
                ", desenvolvimentoArea=" + desenvolvimentoArea +
                ", desenvolvimentoPorcentagem=" + desenvolvimentoPorcentagem +
                ", empresa=" + (empresa != null ? empresa.getNome() : "null") +
                ", obra=" + (obra != null ? obra.getNome() : "null") +
                ", status=" + status +
                '}';
    }

    public void startDatas() {
        DateTimeFormatter formatacao = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String data = LocalDate.now().format(formatacao);
        this.dataInicio = data;

        ItemRelatorio itemRelatorio = new ItemRelatorio();
        itemRelatorio.setDesenvolvimentoArea(0.0);
        itemRelatorio.setDesenvolvimentoPorcentagem(0.0);
        itemRelatorio.setPreparacaoDesenvolvimentoArea(0.0);
        itemRelatorio.setPreparacaoDesenvolvimentoPorcentagem(0.0);
        itemRelatorio.setProtecaoDesenvolvimentoArea(0.0);
        itemRelatorio.setProtecaoDesenvolvimentoPorcentagem(0.0);
        itemRelatorio.setData();

        datas.add(itemRelatorio);

        this.dataFinal = null;
    }

    public ItemRelatorio addItemRelatorio(Double desenvolvimentoArea, Double desenvolvimentoPorcentagem, Double preparacaoDesenvolvimentoArea, Double preparacaoDesenvolvimentoPorcentagem, Double protecaoDesenvolvimentoArea, Double protecaoDesenvolvimentoPorcentagem) {
        ItemRelatorio itemRelatorio = new ItemRelatorio();
        itemRelatorio.setDesenvolvimentoArea(desenvolvimentoArea);
        itemRelatorio.setDesenvolvimentoPorcentagem(desenvolvimentoPorcentagem);
        itemRelatorio.setPreparacaoDesenvolvimentoArea(preparacaoDesenvolvimentoArea);
        itemRelatorio.setPreparacaoDesenvolvimentoPorcentagem(preparacaoDesenvolvimentoPorcentagem);
        itemRelatorio.setProtecaoDesenvolvimentoArea(protecaoDesenvolvimentoArea);
        itemRelatorio.setProtecaoDesenvolvimentoPorcentagem(protecaoDesenvolvimentoPorcentagem);
        itemRelatorio.setData();

        datas.add(itemRelatorio);

        return itemRelatorio;
    }

    public void setDataFinal() {

        if(status.equals(Status.CONCLUIDO)) {
            DateTimeFormatter formatacao = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            String data = LocalDate.now().format(formatacao);
            this.dataFinal = data;
        } else {
            this.dataFinal = null;
        }
    }


}
