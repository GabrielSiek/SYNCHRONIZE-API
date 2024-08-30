package br.com.synchronize.User;

import br.com.synchronize.Categorias.Role;
import br.com.synchronize.Empresa.Empresa;
import br.com.synchronize.Obra.Obra;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity(name = "users")
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @NotEmpty
    @Column(unique = true)

    private String email;
    private String nome;
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    @ManyToOne
    private Empresa empresa;

    @OneToMany(mappedBy = "encarregado", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Obra> obras;

    public User(String email, String nome, String password, Role role) {
        this.email = email;
        this.nome = nome;
        this.password = password;
    }

    public User(String email, String nome, String password, Role role, Empresa empresa) {
        this.email = email;
        this.nome = nome;
        this.password = password;
        this.role = role;
        this.empresa = empresa;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (this.role == Role.ADMIN)
            return List.of(new SimpleGrantedAuthority("ROLE_ADMIN"), new SimpleGrantedAuthority("ROLE_FINANCEIRO"), new SimpleGrantedAuthority("ROLE_ENCARREGADO"));
        else if (this.role == Role.FINANCEIRO)
            return List.of(new SimpleGrantedAuthority("ROLE_FINANCEIRO"), new SimpleGrantedAuthority("ROLE_FINANCEIRO"));
        else
            return List.of(new SimpleGrantedAuthority("ROLE_ENCARREGADO"));
    }


    @Override
    public String getUsername() {
        return email;
    }


    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
