package br.upf.consultaMedica.controller;

import br.upf.consultaMedica.entity.ConsultaEntity;
import br.upf.consultaMedica.entity.PacienteEntity;
import br.upf.consultaMedica.entity.UsuarioEntity;
import br.upf.consultaMedica.facade.ConsultaFacade;
import br.upf.consultaMedica.facade.PacienteFacade;
import br.upf.consultaMedica.facade.UsuarioFacade;
import jakarta.ejb.EJB;
import jakarta.ejb.EJBException;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Named(value = "consultaController")
@ViewScoped
public class ConsultaController implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private ConsultaFacade ejbFacade;
    
    @EJB
    private PacienteFacade pacienteFacade;

    @EJB
    private UsuarioFacade usuarioFacade;

    private ConsultaEntity consulta = new ConsultaEntity();
    private List<ConsultaEntity> consultaList;
    private ConsultaEntity selected;
    
    private Integer pacienteIdSelecionado;
    private Integer medicoIdSelecionado;

    // Getters e Setters
   public Integer getPacienteIdSelecionado() {
        return pacienteIdSelecionado;
    }

    public void setPacienteIdSelecionado(Integer pacienteIdSelecionado) {
        this.pacienteIdSelecionado = pacienteIdSelecionado;
    }

    public Integer getMedicoIdSelecionado() {
        return medicoIdSelecionado;
    }

    public void setMedicoIdSelecionado(Integer medicoIdSelecionado) {
        this.medicoIdSelecionado = medicoIdSelecionado;
    }
    

    public ConsultaEntity getSelected() {
        return selected;
    }

    public void setSelected(ConsultaEntity selected) {
        this.selected = selected;
        // Quando selecionamos uma consulta, populamos os IDs para edição
        if (selected != null) {
            if (selected.getPaciente() != null) {
                this.pacienteIdSelecionado = selected.getPaciente().getId();
            }
            if (selected.getMedico() != null) {
                this.medicoIdSelecionado = selected.getMedico().getCod();
            }
        }
    }

    public ConsultaEntity getConsulta() {
        return consulta;
    }

    public void setConsulta(ConsultaEntity consulta) {
        this.consulta = consulta;
    }

public List<ConsultaEntity> getConsultaList() {
    consultaList = ejbFacade.buscarTodos();
    return consultaList;
}

    public ConsultaEntity prepareAdicionar() {
        consulta = new ConsultaEntity();
        pacienteIdSelecionado = null;
        medicoIdSelecionado = null;
        return consulta;
    }

    // Métodos de ação
    public void adicionarConsulta() {
        try {
            // Validações
            if (pacienteIdSelecionado == null) {
                addErrorMessage("Selecione um paciente!");
                return;
            }

            if (medicoIdSelecionado == null) {
                addErrorMessage("Selecione um médico!");
                return;
            }

            // Busca as entidades (já com os tipos corretos)
            PacienteEntity paciente = pacienteFacade.find(pacienteIdSelecionado);
            if (paciente == null) {
                addErrorMessage("Paciente não encontrado!");
                return;
            }

            UsuarioEntity medico = usuarioFacade.find(medicoIdSelecionado);
            if (medico == null) {
                addErrorMessage("Médico não encontrado!");
                return;
            }

            // Configura a consulta
            consulta.setPaciente(paciente);
            consulta.setMedico(medico);

            // Valida data
            if (consulta.getDataHoraAsDate() == null) {
                addErrorMessage("Informe a data e hora da consulta!");
                return;
            }

            // Persiste
            ejbFacade.createReturn(consulta);

            // Limpa o formulário
            consulta = new ConsultaEntity();
            pacienteIdSelecionado = null;
            medicoIdSelecionado = null;
            consultaList = null;

            addSuccessMessage("Consulta agendada com sucesso!");

        } catch (Exception e) {
            System.err.println("Erro ao agendar consulta: " + e.toString());
            e.printStackTrace();
            addErrorMessage("Erro ao agendar consulta: " + e.getMessage());
        }
    }

    
    public void editarConsulta() {
        System.out.println("=== INICIANDO editarConsulta ===");
        
        try {
            if (selected == null) {
                addErrorMessage("Nenhuma consulta selecionada para edição!");
                return;
            }
            
            // Validar e buscar paciente
            if (pacienteIdSelecionado == null || pacienteIdSelecionado == null) {
                addErrorMessage("Por favor, selecione um paciente!");
                return;
            }
            
            Long pacienteId = Long.valueOf(pacienteIdSelecionado);
            PacienteEntity paciente = pacienteFacade.find(pacienteId);
            if (paciente == null) {
                addErrorMessage("Paciente não encontrado!");
                return;
            }
            selected.setPaciente(paciente);
            
            // Validar e buscar médico
            if (medicoIdSelecionado == null || medicoIdSelecionado == null) {
                addErrorMessage("Por favor, selecione um médico!");
                return;
            }
            
            Long medicoId = Long.valueOf(medicoIdSelecionado);
            UsuarioEntity medico = usuarioFacade.find(medicoId);
            if (medico == null) {
                addErrorMessage("Médico não encontrado!");
                return;
            }
            selected.setMedico(medico);
            
            // Validar data
            if (selected.getDataHoraAsDate() == null) {
                addErrorMessage("Por favor, informe a data e hora da consulta!");
                return;
            }
            
            // Persistir alterações
            ejbFacade.edit(selected);
            
            // Limpar seleção e forçar reload
            selected = null;
            pacienteIdSelecionado = null;
            medicoIdSelecionado = null;
            consultaList = null;
            
            addSuccessMessage("Consulta editada com sucesso!");
            
        } catch (NumberFormatException e) {
            System.err.println("Erro de formato numérico: " + e.getMessage());
            addErrorMessage("ID inválido selecionado!");
        } catch (Exception e) {
            System.err.println("Erro ao editar consulta: " + e.getMessage());
            e.printStackTrace();
            addErrorMessage("Erro ao editar consulta: " + e.getMessage());
        }
    }

    public void confirmarConsulta() {
        if (selected != null && "Agendada".equals(selected.getStatus())) {
            selected.setStatus("Confirmada");
            persist(PersistAction.UPDATE, "Consulta confirmada com sucesso!");
        } else {
            addErrorMessage("Apenas consultas agendadas podem ser confirmadas!");
        }
    }

    public void cancelarConsulta() {
        if (selected != null && ("Agendada".equals(selected.getStatus()) || "Confirmada".equals(selected.getStatus()))) {
            selected.setStatus("Cancelada");
            persist(PersistAction.UPDATE, "Consulta cancelada com sucesso!");
        } else {
            addErrorMessage("Apenas consultas agendadas ou confirmadas podem ser canceladas!");
        }
    }

    public void deletarConsulta() {
        if (selected != null) {
            persist(PersistAction.DELETE, "Consulta removida com sucesso!");
        } else {
            addErrorMessage("Nenhuma consulta selecionada para exclusão!");
        }
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
                        if (consulta.getPaciente() == null) {
                            addErrorMessage("Paciente não foi selecionado!");
                            return;
                        }
                        if (consulta.getMedico() == null) {
                            addErrorMessage("Médico não foi selecionado!");
                            return;
                        }
                        
                        ejbFacade.createReturn(consulta);
                        consulta = new ConsultaEntity();
                        pacienteIdSelecionado = null;
                        medicoIdSelecionado = null;
                        consultaList = null;
                        break;
                        
                    case UPDATE:
                        if (selected == null) {
                            addErrorMessage("Nenhuma consulta selecionada!");
                            return;
                        }
                        
                        ejbFacade.edit(selected);
                        selected = null;
                        pacienteIdSelecionado = null;
                        medicoIdSelecionado = null;
                        consultaList = null;
                        break;
                        
                    case DELETE:
                        if (selected == null) {
                            addErrorMessage("Nenhuma consulta selecionada!");
                            return;
                        }
                        
                        ejbFacade.remove(selected);
                        selected = null;
                        pacienteIdSelecionado = null;
                        medicoIdSelecionado = null;
                        consultaList = null;
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
            System.err.println("Erro na persistência: " + ex.getMessage());
            ex.printStackTrace();
            addErrorMessage("Erro na operação: " + ex.getLocalizedMessage());
        }
    }
    
    // Método auxiliar para conversão manual se necessário
    private Timestamp convertDateToTimestamp(Date date) {
        return date != null ? new Timestamp(date.getTime()) : null;
    }
}