package br.com.construadmin.Categorias;

public enum Role {
    ADMIN("admin"),
    COMPLETO("completo"),
    ENCARREGADO("encarregado");

    private String acesso;

    Role(String acesso){
        this.acesso = acesso;
    }

    public String getAcesso() {
        return acesso;
    }
}
