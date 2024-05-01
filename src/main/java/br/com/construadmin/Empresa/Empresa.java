package br.com.construadmin.Empresa;

import br.com.construadmin.Obra.Obra;
import br.com.construadmin.User.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "empresas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Empresa {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String nome;

    @OneToMany(mappedBy = "empresa", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<User> funcionarios = new ArrayList<>();

    @OneToMany(mappedBy = "empresa", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Obra> obras = new ArrayList<>();

    public Empresa(String nome){
        this.nome = nome;
    }
    public Empresa(RegisterEmpresaDTO registerEmpresaDTO){
        this.nome = registerEmpresaDTO.nome();
    }




}
