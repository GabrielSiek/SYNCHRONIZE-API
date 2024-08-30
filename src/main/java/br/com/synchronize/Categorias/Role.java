package br.com.synchronize.Categorias;

public enum Role {
    ADMIN("admin"),
    FINANCEIRO("financeiro"),
    ALMOXARIFADO("almoxarifado"),
    ENCARREGADO("encarregado");

    private String acesso;

    Role(String acesso){
        this.acesso = acesso;
    }

    public String getAcesso() {
        return acesso;
    }
}
