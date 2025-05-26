package br.upf.consultaMedica.controller;

import br.upf.consultaMedica.entity.UsuarioEntity;
import br.upf.consultaMedica.facade.UsuarioFacade;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import java.io.Serializable;

@Named
@ViewScoped
public class LoginController implements Serializable {

    private static final long serialVersionUID = 1L;

    private Pessoa pessoa;

    @EJB
    private UsuarioFacade usuarioFacade;

    @PostConstruct
    public void init() {
        pessoa = new Pessoa();
        // valores iniciais podem ser vazios
        pessoa.setEmail("");
        pessoa.setSenha("");
    }

    public String validarLogin() {
        UsuarioEntity usuario = usuarioFacade.buscarPorEmailESenha(pessoa.getEmail(), pessoa.getSenha());

        if (usuario != null && usuario.getCod() != null) {
            // Login válido, redireciona para home.xhtml
            return "/admin/home.xhtml?faces-redirect=true";
        } else {
            // Login inválido, mensagem de erro
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Login ou senha inválidos", null));
            return null; // fica na mesma página
        }
    }

    // Getters e Setters

    public Pessoa getPessoa() {
        return pessoa;
    }

    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }

    // Classe interna para simplificar (ou criar em arquivo separado)
    public static class Pessoa implements Serializable {
        private String email;
        private String senha;

        public String getEmail() {
            return email;
        }
        public void setEmail(String email) {
            this.email = email;
        }

        public String getSenha() {
            return senha;
        }
        public void setSenha(String senha) {
            this.senha = senha;
        }
    }
}
