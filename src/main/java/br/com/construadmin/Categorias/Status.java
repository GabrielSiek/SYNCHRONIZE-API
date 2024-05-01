package br.com.construadmin.Categorias;

public enum Status {
    NAO_CONCLUIDO("não concluído"),
    CONCLUIDO("concluído");

    private final String status;

    Status(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
