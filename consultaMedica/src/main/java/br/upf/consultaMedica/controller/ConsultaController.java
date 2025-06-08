package br.upf.consultaMedica.controller;

import br.upf.consultaMedica.entity.ConsultaEntity;
import br.upf.consultaMedica.entity.PacienteEntity;
import br.upf.consultaMedica.entity.UsuarioEntity;
import br.upf.consultaMedica.facade.ConsultaFacade;
import br.upf.consultaMedica.facade.PacienteFacade;
import br.upf.consultaMedica.facade.UsuarioFacade;
import jakarta.ejb.EJB;
import jakarta.ejb.EJBException;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.stream.Collectors;

@Named(value = "consultaController")
@SessionScoped
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
    
    private List<ConsultaEntity> consultasHoje;
private int totalConsultasHoje;
private int totalPacientes;
private ConsultaEntity proximaConsulta;

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
     public String adicionarConsulta() {
        try {
            // Validações
            if (pacienteIdSelecionado == null) {
                addErrorMessage("Selecione um paciente!");
                return null;
            }

            if (medicoIdSelecionado == null) {
                addErrorMessage("Selecione um médico!");
                return null;
            }

            // Busca as entidades
            PacienteEntity paciente = pacienteFacade.find(pacienteIdSelecionado);
            if (paciente == null) {
                addErrorMessage("Paciente não encontrado!");
                return null;
            }

            UsuarioEntity medico = usuarioFacade.find(medicoIdSelecionado);
            if (medico == null) {
                addErrorMessage("Médico não encontrado!");
                return null;
            }

            // Configura a consulta
            consulta.setPaciente(paciente);
            consulta.setMedico(medico);

            // Valida data
            if (consulta.getDataHoraAsDate() == null) {
                addErrorMessage("Informe a data e hora da consulta!");
                return null;
            }

            // Persiste a consulta
            ejbFacade.createReturn(consulta);

            // Configura mensagem para mostrar após redirecionamento
            FacesContext.getCurrentInstance().getExternalContext().getFlash()
                .setKeepMessages(true);
            addSuccessMessage("Consulta agendada com sucesso!");

            // Limpa o formulário
            consulta = new ConsultaEntity();
            pacienteIdSelecionado = null;
            medicoIdSelecionado = null;
            consultaList = null;

            // Redireciona para evitar reenvio ao atualizar
            return "consulta?faces-redirect=true";

        } catch (Exception e) {
            System.err.println("Erro ao agendar consulta: " + e.toString());
            e.printStackTrace();
            addErrorMessage("Erro ao agendar consulta: " + e.getMessage());
            return null;
        }
    }

    
    public String editarConsulta() {
        FacesContext context = FacesContext.getCurrentInstance();
        
        try {
            // 1. Verificar consulta selecionada
            if (selected == null) {
                addErrorMessage("Nenhuma consulta selecionada para edição!");
                return null;
            }
            
            // 2. Validar paciente
            if (pacienteIdSelecionado == null) {
                addErrorMessage("Selecione um paciente válido!");
                return null;
            }
            
            PacienteEntity paciente = pacienteFacade.find(pacienteIdSelecionado);
            if (paciente == null) {
                addErrorMessage("Paciente não encontrado com ID: " + pacienteIdSelecionado);
                return null;
            }
            selected.setPaciente(paciente);
            
            // 3. Validar médico
            if (medicoIdSelecionado == null) {
                addErrorMessage("Selecione um médico válido!");
                return null;
            }
            
            UsuarioEntity medico = usuarioFacade.find(medicoIdSelecionado);
            if (medico == null) {
                addErrorMessage("Médico não encontrado com ID: " + medicoIdSelecionado);
                return null;
            }
            selected.setMedico(medico);
            
            // 4. Validar data/hora
            if (selected.getDataHoraAsDate() == null) {
                addErrorMessage("Data/hora da consulta é obrigatória!");
                return null;
            }
            
            // 5. Atualizar timestamp
            selected.setDataHora(new Timestamp(selected.getDataHoraAsDate().getTime()));
            
            // 6. Validar status
            if (selected.getStatus() == null || selected.getStatus().isEmpty()) {
                selected.setStatus("Agendada");
            }
            
            // 7. Persistir
            ejbFacade.edit(selected);
            
            // Configura mensagem para mostrar após redirecionamento
            context.getExternalContext().getFlash().setKeepMessages(true);
            addSuccessMessage("Consulta atualizada com sucesso!");
            
            // 8. Limpar estado
            selected = null;
            pacienteIdSelecionado = null;
            medicoIdSelecionado = null;
            consultaList = null;
            
            // 9. Redirecionar para evitar reenvio ao atualizar
            return "consulta?faces-redirect=true";
            
        } catch (EJBException ejbEx) {
            String msg = ejbEx.getCause() != null ? ejbEx.getCause().getMessage() : ejbEx.getMessage();
            System.err.println("Erro EJB ao editar consulta: " + msg);
            addErrorMessage("Erro ao editar consulta: " + msg);
            return null;
            
        } catch (Exception ex) {
            System.err.println("Erro detalhado ao editar consulta:");
            ex.printStackTrace();
            addErrorMessage("Erro ao editar: " + ex.getMessage());
            return null;
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
    


// Métodos para o Dashboard
public List<ConsultaEntity> getConsultasHoje() {
    if (consultasHoje == null) {
        carregarConsultasHoje();
    }
    return consultasHoje;
}

public void carregarConsultasHoje() {
    try {
        // Busca todas as consultas
        List<ConsultaEntity> todasConsultas = ejbFacade.buscarTodos();
        
        // Data de hoje
        LocalDate hoje = LocalDate.now();
        
        // Filtra apenas as consultas de hoje
        consultasHoje = todasConsultas.stream()
            .filter(consulta -> {
                if (consulta.getDataHoraAsDate() != null) {
                    LocalDate dataConsulta = consulta.getDataHoraAsDate()
                        .toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate();
                    return dataConsulta.equals(hoje);
                }
                return false;
            })
            .sorted((c1, c2) -> {
                // Ordena por horário
                if (c1.getDataHoraAsDate() != null && c2.getDataHoraAsDate() != null) {
                    return c1.getDataHoraAsDate().compareTo(c2.getDataHoraAsDate());
                }
                return 0;
            })
            .collect(Collectors.toList());
            
        // Atualiza estatísticas
        atualizarEstatisticas();
        
    } catch (Exception e) {
        System.err.println("Erro ao carregar consultas de hoje: " + e.getMessage());
        e.printStackTrace();
        consultasHoje = new ArrayList<>();
    }
}

private void atualizarEstatisticas() {
    // Total de consultas hoje
    totalConsultasHoje = consultasHoje != null ? consultasHoje.size() : 0;
    
    // Próxima consulta (primeira consulta não realizada do dia)
    proximaConsulta = null;
    if (consultasHoje != null && !consultasHoje.isEmpty()) {
        LocalDateTime agora = LocalDateTime.now();
        
        for (ConsultaEntity consulta : consultasHoje) {
            if (consulta.getDataHoraAsDate() != null) {
                LocalDateTime dataHoraConsulta = consulta.getDataHoraAsDate()
                    .toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime();
                
                // Verifica se a consulta ainda não aconteceu e não foi cancelada
                if (dataHoraConsulta.isAfter(agora) && 
                    !"Cancelada".equals(consulta.getStatus()) &&
                    !"Realizada".equals(consulta.getStatus())) {
                    proximaConsulta = consulta;
                    break;
                }
            }
        }
    }
    
    // Total de pacientes (você pode implementar uma busca específica se necessário)
    try {
        List<PacienteEntity> pacientes = pacienteFacade.buscarTodos();
        totalPacientes = pacientes != null ? pacientes.size() : 0;
    } catch (Exception e) {
        System.err.println("Erro ao buscar total de pacientes: " + e.getMessage());
        totalPacientes = 0;
    }
}

// Getters para as estatísticas
public int getTotalConsultasHoje() {
    if (consultasHoje == null) {
        carregarConsultasHoje();
    }
    return totalConsultasHoje;
}

public int getTotalPacientes() {
    if (consultasHoje == null) {
        carregarConsultasHoje();
    }
    return totalPacientes;
}

public ConsultaEntity getProximaConsulta() {
    if (consultasHoje == null) {
        carregarConsultasHoje();
    }
    return proximaConsulta;
}

// Métodos auxiliares para formatação na tela
public String getProximaConsultaHorario() {
    if (proximaConsulta != null && proximaConsulta.getDataHoraAsDate() != null) {
        LocalDateTime dataHora = proximaConsulta.getDataHoraAsDate()
            .toInstant()
            .atZone(ZoneId.systemDefault())
            .toLocalDateTime();
        
        return String.format("%02d:%02d", dataHora.getHour(), dataHora.getMinute());
    }
    return "--:--";
}

public String getProximaConsultaDescricao() {
    if (proximaConsulta != null && proximaConsulta.getPaciente() != null) {
        String nomePaciente = proximaConsulta.getPaciente().getNome();
        String especialidade = "Consulta";
        
        if (proximaConsulta.getMedico() != null && 
            proximaConsulta.getMedico().getIdEspecialidade() != null) {
            especialidade = proximaConsulta.getMedico().getIdEspecialidade().getDescricao();
        }
        
        return nomePaciente + " - " + especialidade;
    }
    return "Nenhuma consulta agendada";
}

public String formatarHorarioConsulta(ConsultaEntity consulta) {
    if (consulta != null && consulta.getDataHoraAsDate() != null) {
        LocalDateTime dataHora = consulta.getDataHoraAsDate()
            .toInstant()
            .atZone(ZoneId.systemDefault())
            .toLocalDateTime();
        
        return String.format("%02d:%02d", dataHora.getHour(), dataHora.getMinute());
    }
    return "--:--";
}

public String getStatusClass(String status) {
    if (status == null) return "status-pending";
    
    switch (status.toLowerCase()) {
        case "confirmada":
        case "confirmado":
            return "status-confirmed";
        case "agendada":
        case "agendado":
            return "status-scheduled";
        case "pendente":
            return "status-pending";
        default:
            return "status-pending";
    }
}

// Método para forçar atualização dos dados (útil para chamadas AJAX)
public void atualizarDashboard() {
    consultasHoje = null;
    carregarConsultasHoje();
}
}