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

@Named(value = "pacienteController")
@SessionScoped
public class PacienteController implements Serializable {

    @EJB
    private PacienteFacade ejbFacade;

    private PacienteEntity paciente;
    private List<PacienteEntity> pacienteList = new ArrayList<>();
    private PacienteEntity selected;

    @PostConstruct
    public void init() {
        // Inicializar paciente no PostConstruct
        this.paciente = new PacienteEntity();
        initializePaciente(this.paciente);
    }

    // Método para inicializar objetos aninhados do paciente
    private void initializePaciente(PacienteEntity p) {
        if (p != null) {
            // Inicializar cidade se for nula (usando sua CidadeEntity)
            if (p.getCidade() == null) {
                CidadeEntity cidade = new CidadeEntity();
                // Não definir ID pois será selecionado pelo usuário
                p.setCidade(cidade);
            }
            
        }
    }

    public PacienteEntity getSelected() {
        return selected;
    }

    public void setSelected(PacienteEntity selected) {
        this.selected = selected;
        // Garantir que objetos aninhados não sejam nulos
        if (this.selected != null) {
            initializePaciente(this.selected);
        }
    }

    public PacienteEntity getPaciente() {
    if (paciente == null) {
        paciente = new PacienteEntity();
        initializePaciente(paciente);
    }
    // Garantir que cidade e ID não sejam nulos
    if (paciente.getCidade() == null) {
        paciente.setCidade(new CidadeEntity());
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

    public void prepareAdicionar() {
        paciente = new PacienteEntity();
        initializePaciente(paciente);
        System.out.println("Prepare adicionar - Paciente inicializado: " + (paciente != null));
        System.out.println("Prepare adicionar - Cidade inicializada: " + (paciente.getCidade() != null));
    }

    public void adicionarPaciente() {
        try {
            // Garantir que o paciente está inicializado
            if (paciente == null) {
                addErrorMessage("Erro: Paciente não foi inicializado corretamente.");
                return;
            }
            
            // Verificações antes de salvar
            initializePaciente(paciente);
            
            // Debug - usando sua CidadeEntity
            System.out.println("Adicionando paciente: " + paciente.getNome());
            System.out.println("Cidade: " + (paciente.getCidade() != null ? paciente.getCidade().getNome() : "null"));
            System.out.println("Cidade ID: " + (paciente.getCidade() != null ? paciente.getCidade().getId() : "null"));
            
            persist(PersistAction.CREATE, "Paciente adicionado com sucesso!");
            
        } catch (Exception e) {
            addErrorMessage("Erro ao adicionar paciente: " + e.getLocalizedMessage());
            e.printStackTrace();
        }
    }

    public void editarPaciente() {
        try {
            if (selected == null) {
                addErrorMessage("Nenhum paciente selecionado para edição.");
                return;
            }
            persist(PersistAction.UPDATE, "Paciente alterado com sucesso!");
        } catch (Exception e) {
            addErrorMessage("Erro ao editar paciente: " + e.getLocalizedMessage());
        }
    }

    public void deletarPaciente() {
        try {
            if (selected == null) {
                addErrorMessage("Nenhum paciente selecionado para exclusão.");
                return;
            }
            persist(PersistAction.DELETE, "Paciente excluído com sucesso!");
        } catch (Exception e) {
            addErrorMessage("Erro ao deletar paciente: " + e.getLocalizedMessage());
        }
    }

    // Método para buscar pacientes ativos
    // Método para buscar por CPF
    public PacienteEntity buscarPorCpf(String cpf) {
        try {
            return ejbFacade.buscarPorCpf(cpf);
        } catch (Exception ex) {
            addErrorMessage("Erro ao buscar paciente por CPF: " + ex.getLocalizedMessage());
            return null;
        }
    }

    // Método para buscar por nome (parcial)
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
                        // Após criar, reinicializar para próximo cadastro
                        prepareAdicionar();
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
            }
            addSuccessMessage(successMessage);
        } catch (EJBException ex) {
            String msg = "";
            Throwable cause = ex.getCause();
            if (cause != null) {
                msg = cause.getLocalizedMessage();
            }
            if (msg != null && msg.length() > 0) {
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