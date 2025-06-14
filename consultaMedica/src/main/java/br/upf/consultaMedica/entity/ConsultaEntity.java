package br.upf.consultaMedica.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "consulta")
public class ConsultaEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "paciente_id", referencedColumnName = "id", nullable = false)
    private PacienteEntity paciente;
    
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "medico_id", referencedColumnName = "cod", nullable = false)
    private UsuarioEntity medico;
    
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plano_id", referencedColumnName = "id", nullable = false)
    private PlanoSaudeEntity plano;
    
    @NotNull
    @Column(name = "data_hora", nullable = false)
    private Timestamp dataHora;
    
    @NotNull
    @Column(name = "status", nullable = false, length = 20)
    private String status = "Agendada";
    
    @Column(name = "observacoes", columnDefinition = "TEXT")
    private String observacoes;
  
    // Getters e Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public PacienteEntity getPaciente() {
        return paciente;
    }
    
    public void setPaciente(PacienteEntity paciente) {
        this.paciente = paciente;
    }
    
    public UsuarioEntity getMedico() {
        return medico;
    }
    
    public void setMedico(UsuarioEntity medico) {
        this.medico = medico;
    }
    
    public Timestamp getDataHora() {
        return dataHora;
    }
    
    public void setDataHora(Timestamp dataHora) {
        this.dataHora = dataHora;
    }
    
    public void setDataHora(Date dataHora) {
        if (dataHora != null) {
            this.dataHora = new Timestamp(dataHora.getTime());
        } else {
            this.dataHora = null;
        }
    }
    
        public PlanoSaudeEntity getPlano() {
        return plano;
    }
    
    public void setPlano(PlanoSaudeEntity plano) {
        this.plano = plano;
    }
    
    public Date getDataHoraAsDate() {
        return dataHora != null ? new Date(dataHora.getTime()) : null;
    }
    
    public void setDataHoraAsDate(Date date) {
    if (date != null) {
        this.dataHora = new Timestamp(date.getTime());
    } else {
        this.dataHora = null;
    }
}
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public String getObservacoes() {
        return observacoes;
    }
    
    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }
    
    public String toStringDetalhado() {
    return "ConsultaEntity{" +
            "id=" + id +
            ", pacienteId=" + (paciente != null ? paciente.getId() : "null") +
            ", medicoId=" + (medico != null ? medico.getCod() : "null") +
            ", dataHora=" + dataHora +
            ", status='" + status + '\'' +
            ", observacoes='" + (observacoes != null ? observacoes.substring(0, Math.min(observacoes.length(), 30)) + "..." : "null") +
            '}';
}
    
    // equals e hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConsultaEntity consulta = (ConsultaEntity) o;
        return Objects.equals(id, consulta.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    
    // toString
    @Override
    public String toString() {
        return "Consulta{" +
                "id=" + id +
                ", dataHora=" + dataHora +
                ", status='" + status + '\'' +
                '}';
    }
}