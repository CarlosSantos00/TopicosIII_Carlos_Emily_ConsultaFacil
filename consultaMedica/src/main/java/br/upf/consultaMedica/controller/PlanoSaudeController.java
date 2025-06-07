package br.upf.consultaMedica.controller;

import br.upf.consultaMedica.entity.PlanoSaudeEntity;
import br.upf.consultaMedica.facade.PlanoSaudeFacade;
import jakarta.ejb.EJB;
import jakarta.ejb.EJBException;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.List;

@Named(value = "planoSaudeController")
@SessionScoped
public class PlanoSaudeController implements Serializable {

    @EJB
    private PlanoSaudeFacade ejbFacade;

    private PlanoSaudeEntity planoSaude = new PlanoSaudeEntity();
    private List<PlanoSaudeEntity> planoSaudeList;
    private PlanoSaudeEntity selected;

    // Getters e Setters
    public PlanoSaudeEntity getSelected() {
        return selected;
    }

    public void setSelected(PlanoSaudeEntity selected) {
        this.selected = selected;
    }

    public PlanoSaudeEntity getPlanoSaude() {
        return planoSaude;
    }

    public void setPlanoSaude(PlanoSaudeEntity planoSaude) {
        this.planoSaude = planoSaude;
    }

    public List<PlanoSaudeEntity> getPlanoSaudeList() {
        if (planoSaudeList == null) {
            planoSaudeList = ejbFacade.buscarTodos();
        }
        return planoSaudeList;
    }

    public void setPlanoSaudeList(List<PlanoSaudeEntity> planoSaudeList) {
        this.planoSaudeList = planoSaudeList;
    }

    // Métodos de ação
    public PlanoSaudeEntity prepareAdicionar() {
        planoSaude = new PlanoSaudeEntity();
        return planoSaude;
    }

    public void adicionarPlanoSaude() {
        persist(PersistAction.CREATE, "Plano de saúde adicionado com sucesso!");
    }

    public void editarPlanoSaude() {
        persist(PersistAction.UPDATE, "Plano de saúde alterado com sucesso!");
    }

    public void deletarPlanoSaude() {
        persist(PersistAction.DELETE, "Plano de saúde excluído com sucesso!");
    }

    // Métodos auxiliares
    public static void addErrorMessage(String msg) {
        FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, msg);
        FacesContext.getCurrentInstance().addMessage(null, facesMsg);
    }

    public static void addSuccessMessage(String msg) {
        FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_INFO, msg, msg);
        FacesContext.getCurrentInstance().addMessage("successInfo", facesMsg);
    }

    public enum PersistAction {
        CREATE,
        DELETE,
        UPDATE
    }

    private void persist(PersistAction persistAction, String successMessage) {
        try {
            if (persistAction != null) {
                switch (persistAction) {
                    case CREATE:
                        ejbFacade.createReturn(planoSaude);
                        planoSaudeList = null; // Força recarregar a lista
                        break;
                    case UPDATE:
                        ejbFacade.edit(selected);
                        selected = null;
                        planoSaudeList = null;
                        break;
                    case DELETE:
                        ejbFacade.remove(selected);
                        selected = null;
                        planoSaudeList = null;
                        break;
                }
            }
            addSuccessMessage(successMessage);
        } catch (EJBException ex) {
            String msg = "";
            Throwable cause = ex.getCause();
            if (cause != null) {
                msg = cause.getLocalizedMessage();
            }
            if (msg.length() > 0) {
                addErrorMessage(msg);
            } else {
                addErrorMessage(ex.getLocalizedMessage());
            }
        } catch (Exception ex) {
            addErrorMessage(ex.getLocalizedMessage());
        }
    }
}