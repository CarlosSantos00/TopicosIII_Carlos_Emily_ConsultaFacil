package br.upf.consultaMedica.controller;

import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import java.io.Serializable;

@Named
@ViewScoped
public class LoginController implements Serializable {

    private Pessoa pessoa;

    @PostConstruct
    public void init() {
        pessoa = new Pessoa();
        // Valores chumbados
        pessoa.setEmail("admin@empresa.com");
        pessoa.setSenha("123456");
    }

    public String validarLogin() {
        // Exemplo simples: só para ilustrar a ação do botão
        if ("admin@empresa.com".equals(pessoa.getEmail()) && "123456".equals(pessoa.getSenha())) {
            return "home.xhtml?faces-redirect=true"; // Redireciona para home.xhtml (exemplo)
        } else {
            // Implementar mensagem de erro no growl aqui
            return null; // Fica na mesma página
        }
    }

    // Getters e Setters

    public Pessoa getPessoa() {
        return pessoa;
    }

    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }

    // Classe interna para simplificar (ou crie em arquivo separado)
    public static class Pessoa implements Serializable {
        private String email;
        private String senha;

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }

        public String getSenha() { return senha; }
        public void setSenha(String senha) { this.senha = senha; }
    }
}
