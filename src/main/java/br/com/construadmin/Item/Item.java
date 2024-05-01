package br.com.construadmin.Item;

import br.com.construadmin.Categorias.Status;
import br.com.construadmin.Empresa.Empresa;
import br.com.construadmin.Obra.Obra;
import jakarta.persistence.*;
import lombok.*;

@Entity(name = "itens")
@Table(name = "itens")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Item extends ItemDefault {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private int numero;
    private String localDeAplicacao;
    private String nome;
    private String sistemas;
    private int quantidade;

    private ItemDefault preparacao;

    private ItemDefault protecao;

    @ManyToOne
    private Empresa empresa;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "obra_id")
    private Obra obra;

    @Enumerated(EnumType.STRING)
    private Status status;


    public Item(String nome, Empresa empresa, Obra obra) {
        this.nome = nome;
        this.empresa = empresa;
        this.obra = obra;
        status = Status.NAO_CONCLUIDO;
    }
}
