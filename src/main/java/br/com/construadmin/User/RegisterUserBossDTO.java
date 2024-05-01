package br.com.construadmin.User;

public record RegisterUserBossDTO(String email,
                                  String nome,
                                  String password,
                                  String nome_empresa) {
}
