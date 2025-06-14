package br.upf.consultaMedica.controller;

import br.upf.consultaMedica.entity.UsuarioEntity;
import br.upf.consultaMedica.facade.UsuarioFacade;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.ComponentSystemEvent;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import java.io.IOException;
import java.io.Serializable;
import java.util.Map;

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
        // Armazena o usuário na sessão
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        Map<String, Object> sessionMap = externalContext.getSessionMap();
        sessionMap.put("usuarioLogado", usuario);

        // Redireciona para a home
        return "/admin/home.xhtml?faces-redirect=true";
    } else {
        // Login inválido, exibe mensagem de erro
        FacesContext.getCurrentInstance().addMessage(null,
            new FacesMessage(FacesMessage.SEVERITY_ERROR, "Login ou senha inválidos", null));
        return null;
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
    
    public String getNomePessoa() {
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        UsuarioEntity usuario = (UsuarioEntity) externalContext.getSessionMap().get("usuarioLogado");
        return (usuario != null) ? usuario.getNome() : "";
    }
    
    public String logout() {
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        return "/login.xhtml?faces-redirect=true";
    }
    
public void redirecionarSeNaoLogado(ComponentSystemEvent event) throws IOException {
    ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
    if (externalContext.getSessionMap().get("usuarioLogado") == null) {
        // Redireciona para a raiz do sistema
        externalContext.redirect("http://localhost:8080/consultaMedica/");
    }
}
}
