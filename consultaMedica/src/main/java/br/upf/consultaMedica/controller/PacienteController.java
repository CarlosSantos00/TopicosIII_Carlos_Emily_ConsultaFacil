package br.upf.consultaMedica.controller;

import br.upf.consultaMedica.entity.PacienteEntity;
import br.upf.consultaMedica.entity.CidadeEntity;
import br.upf.consultaMedica.facade.PacienteFacade;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.ejb.EJBException;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;

@Named(value = "pacienteController")
@SessionScoped
public class PacienteController implements Serializable {

    @EJB
    private PacienteFacade ejbFacade;

    private PacienteEntity paciente;
    private List<PacienteEntity> pacienteList = new ArrayList<>();
    private PacienteEntity selected;

    private boolean isEditando;

    @PostConstruct
    public void init() {
        this.paciente = new PacienteEntity();
        initializePaciente(this.paciente);
        this.isEditando = false;
    }

    private void initializePaciente(PacienteEntity p) {
        if (p != null && p.getCidade() == null) {
            p.setCidade(new CidadeEntity());
        }
    }

    public PacienteEntity getSelected() {
        return selected;
    }

    public void setSelected(PacienteEntity selected) {
        this.selected = selected;
        if (this.selected != null) {
            initializePaciente(this.selected);
        }
    }

    public PacienteEntity getPaciente() {
        if (paciente == null) {
            paciente = new PacienteEntity();
            initializePaciente(paciente);
        }
        return paciente;
    }

    public void setPaciente(PacienteEntity paciente) {
        this.paciente = paciente;
        if (this.paciente != null) {
            initializePaciente(this.paciente);
        }
    }

    public List<PacienteEntity> getPacienteList() {
        try {
            return ejbFacade.buscarTodos();
        } catch (Exception e) {
            addErrorMessage("Erro ao carregar lista de pacientes: " + e.getLocalizedMessage());
            return new ArrayList<>();
        }
    }

    public void setPacienteList(List<PacienteEntity> pacienteList) {
        this.pacienteList = pacienteList;
    }

    public boolean isEditando() {
        return isEditando;
    }

    public void setEditando(boolean isEditando) {
        this.isEditando = isEditando;
    }

    public void prepareAdicionar() {
        paciente = new PacienteEntity();
        initializePaciente(paciente);
        isEditando = false;
        System.out.println("Prepare adicionar - Paciente inicializado: " + (paciente != null));
        System.out.println("Prepare adicionar - Cidade inicializada: " + (paciente.getCidade() != null));
    }

public void prepareEditar(PacienteEntity pacienteSelecionado) {
    if (pacienteSelecionado != null && pacienteSelecionado.getId() != null) {
        this.selected = ejbFacade.find(pacienteSelecionado.getId());
        initializePaciente(this.selected);
        isEditando = true;
    } else {
        addErrorMessage("Paciente inválido para edição.");
    }
}


    public void salvarPaciente() {
        if (isEditando) {
            editarPaciente();
        } else {
            adicionarPaciente();
        }
    }

    public void adicionarPaciente() {
        try {
            if (paciente == null) {
                addErrorMessage("Erro: Paciente não foi inicializado corretamente.");
                return;
            }
            initializePaciente(paciente);
            persist(PersistAction.CREATE, "Paciente adicionado com sucesso!");
            prepareAdicionar();
        } catch (Exception e) {
            addErrorMessage("Erro ao adicionar paciente: " + e.getLocalizedMessage());
            e.printStackTrace();
        }
    }

    public void editarPaciente() {
        try {
            if (selected == null) {
                addErrorMessage("Nenhum paciente selecionado para editar.");
                return;
            }

            // Logs úteis
            System.out.println("### DEBUG editarPaciente ###");
            System.out.println("Nome: " + selected.getNome());
            System.out.println("CPF: " + selected.getCpf());
            System.out.println("Celular: " + selected.getCelular());
            System.out.println("Data Nascimento: " + selected.getDataNascimento());
            System.out.println("Email: " + selected.getEmail());

            persist(PersistAction.UPDATE, "Paciente alterado com sucesso!");
            selected = null;
            prepareAdicionar();
        } catch (ConstraintViolationException ex) {
            for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
                addErrorMessage("Erro de validação: " + violation.getPropertyPath() + " - " + violation.getMessage());
            }
        } catch (Exception e) {
            addErrorMessage("Erro ao editar paciente: " + e.getLocalizedMessage());
            e.printStackTrace();
        }
    }

    public void deletarPaciente() {
        try {
            if (selected == null) {
                addErrorMessage("Nenhum paciente selecionado para exclusão.");
                return;
            }
            persist(PersistAction.DELETE, "Paciente excluído com sucesso!");
            selected = null;
        } catch (Exception e) {
            addErrorMessage("Erro ao deletar paciente: " + e.getLocalizedMessage());
            e.printStackTrace();
        }
    }

    public PacienteEntity buscarPorCpf(String cpf) {
        try {
            return ejbFacade.buscarPorCpf(cpf);
        } catch (Exception ex) {
            addErrorMessage("Erro ao buscar paciente por CPF: " + ex.getLocalizedMessage());
            return null;
        }
    }

    public List<PacienteEntity> buscarPorNome(String nome) {
        try {
            return ejbFacade.buscarPorNome(nome);
        } catch (Exception ex) {
            addErrorMessage("Erro ao buscar pacientes por nome: " + ex.getLocalizedMessage());
            return new ArrayList<>();
        }
    }

    public static void addErrorMessage(String msg) {
        FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, msg);
        FacesContext.getCurrentInstance().addMessage(null, facesMsg);
    }

    public static void addSuccessMessage(String msg) {
        FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_INFO, msg, msg);
        FacesContext.getCurrentInstance().addMessage("successInfo", facesMsg);
    }

    public static enum PersistAction {
        CREATE,
        DELETE,
        UPDATE
    }

    private void persist(PersistAction persistAction, String successMessage) {
        try {
            if (persistAction != null) {
                switch (persistAction) {
                    case CREATE:
                        ejbFacade.createReturn(paciente);
                        break;
                    case UPDATE:
                        ejbFacade.edit(selected); // ← CORREÇÃO FEITA AQUI
                        break;
                    case DELETE:
                        ejbFacade.remove(selected);
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
            if (msg != null && !msg.isEmpty()) {
                addErrorMessage(msg);
            } else {
                addErrorMessage(ex.getLocalizedMessage());
            }
        } catch (Exception ex) {
            addErrorMessage("Erro inesperado: " + ex.getLocalizedMessage());
            ex.printStackTrace();
        }
    }
}
