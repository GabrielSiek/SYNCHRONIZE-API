package br.com.synchronize.Obra;

import br.com.synchronize.Categorias.Status;
import br.com.synchronize.Empresa.Empresa;
import br.com.synchronize.Item.Item;
import br.com.synchronize.User.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
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

    private Double valorTotal;

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
        this.valorTotal = 0.0;
        itens = new ArrayList<>();
    }

    public void setItens(List<Item> novosItens) {
        List<Item> itensParaRemover = new ArrayList<>(this.itens);

        for (Item novoItem : novosItens) {
            if (this.itens.contains(novoItem)) {
                // Atualiza o item existente
                int index = this.itens.indexOf(novoItem);
                this.itens.set(index, novoItem);
                itensParaRemover.remove(novoItem);
            } else {
                // Adiciona o novo item
                this.itens.add(novoItem);
                novoItem.setObra(this); // define a obra no item
            }
        }

        // Remove os itens que não estão mais na lista
        this.itens.removeAll(itensParaRemover);

        // Atualiza o valor total da obra
        updateValorTotal();
    }

    public void updateValorTotal(){
        itens.forEach(i -> valorTotal+= i.getValor());
    }
}

