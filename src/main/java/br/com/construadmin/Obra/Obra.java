package br.com.construadmin.Obra;

import br.com.construadmin.Categorias.Status;
import br.com.construadmin.Empresa.Empresa;
import br.com.construadmin.Item.Item;
import br.com.construadmin.User.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity(name = "obras")
@Table(name = "obras")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Obra {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String nome;

    @Enumerated(EnumType.STRING)
    private Status status;

    @ManyToOne
    private Empresa empresa;

    @OneToMany(mappedBy = "obra", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Item> itens;

    @ManyToOne
    private User encarregado;

    public Obra(String nome, Empresa empresa, User encarregado) {
        this.nome = nome;
        this.empresa = empresa;
        this.encarregado = encarregado;
        status = Status.NAO_CONCLUIDO;
    }
}

