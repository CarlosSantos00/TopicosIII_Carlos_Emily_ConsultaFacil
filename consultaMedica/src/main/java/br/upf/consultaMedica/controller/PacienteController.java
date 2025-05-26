package br.upf.consultaMedica.controller;

import br.upf.consultaMedica.entity.PacienteEntity;
import br.upf.consultaMedica.facade.PacienteFacade;
import jakarta.ejb.EJB;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Named(value = "pacienteController")
@SessionScoped
public class PacienteController implements Serializable {

    @EJB
    private PacienteFacade ejbFacade;

    private PacienteEntity paciente = new PacienteEntity();
    private List<PacienteEntity> pacienteList = new ArrayList<>();
    private PacienteEntity selected;

    public PacienteEntity getPaciente() {
        return paciente;
    }

    public void setPaciente(PacienteEntity paciente) {
        this.paciente = paciente;
    }

    public PacienteEntity getSelected() {
        return selected;
    }

    public void setSelected(PacienteEntity selected) {
        this.selected = selected;
    }

    public List<PacienteEntity> getPacienteList() {
        return ejbFacade.buscarTodos();
    }

    public void prepararNovo() {
        paciente = new PacienteEntity();
    }

    public void adicionarPaciente() {
        persist(PersistAction.CREATE, "Paciente adicionado com sucesso!");
    }

    public void editarPaciente() {
        persist(PersistAction.UPDATE, "Paciente alterado com sucesso!");
    }

    public void deletarPaciente() {
        persist(PersistAction.DELETE, "Paciente excluído com sucesso!");
    }

    public static enum PersistAction {
        CREATE,
        DELETE,
        UPDATE
    }

    private void persist(PersistAction action, String msg) {
        try {
            switch (action) {
                case CREATE:
                    ejbFacade.create(paciente);
                    break;
                case UPDATE:
                    ejbFacade.edit(selected);
                    selected = null;
                    break;
                case DELETE:
                    ejbFacade.remove(selected);
                    selected = null;
                    break;
            }
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, msg, null));
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro: " + e.getMessage(), null));
        }
    }
}
