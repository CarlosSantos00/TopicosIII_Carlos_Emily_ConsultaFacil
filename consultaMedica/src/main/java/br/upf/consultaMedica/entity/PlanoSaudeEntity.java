package br.upf.consultaMedica.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.DecimalMin;
import java.io.Serializable;
import java.util.Objects;
import java.sql.Timestamp;

@Entity
@Table(name = "planos_medicos")
public class PlanoSaudeEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "nome", nullable = false, length = 100)
    private String nome;

    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "codigo_ans", nullable = false, unique = true, length = 20)
    private String codigoANS;

    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "tipo_plano", nullable = false, length = 50)
    private String tipoPlano;

    @NotNull
    @DecimalMin("0.01")
    @Column(name = "valor_mensal", nullable = false, precision = 10, scale = 2)
    private Double valorMensal;

    @DecimalMin("0.00")
    @Column(name = "coparticipacao", precision = 5, scale = 2)
    private Double coparticipacao = 0.0;

    @Column(name = "cobre_internacao", length = 20)
    private String cobreInternacao;

    @Column(name = "cobre_emergencia", length = 20)
    private String cobreEmergencia;

    @Column(name = "cobre_odontologico", length = 20)
    private String cobreOdontologico;

    @Column(name = "data_cadastro", updatable = false)
    private Timestamp dataCadastro = new Timestamp(System.currentTimeMillis());

    @Column(name = "data_atualizacao")
    private Timestamp dataAtualizacao = new Timestamp(System.currentTimeMillis());

    @Column(name = "observacoes", columnDefinition = "TEXT")
    private String observacoes;

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCodigoANS() {
        return codigoANS;
    }

    public void setCodigoANS(String codigoANS) {
        this.codigoANS = codigoANS;
    }

    public String getTipoPlano() {
        return tipoPlano;
    }

    public void setTipoPlano(String tipoPlano) {
        this.tipoPlano = tipoPlano;
    }

    public Double getValorMensal() {
        return valorMensal;
    }

    public void setValorMensal(Double valorMensal) {
        this.valorMensal = valorMensal;
    }

    public Double getCoparticipacao() {
        return coparticipacao;
    }

    public void setCoparticipacao(Double coparticipacao) {
        this.coparticipacao = coparticipacao;
    }

    public String getCobreInternacao() {
        return cobreInternacao;
    }

    public void setCobreInternacao(String cobreInternacao) {
        this.cobreInternacao = cobreInternacao;
    }

    public String getCobreEmergencia() {
        return cobreEmergencia;
    }

    public void setCobreEmergencia(String cobreEmergencia) {
        this.cobreEmergencia = cobreEmergencia;
    }

    public String getCobreOdontologico() {
        return cobreOdontologico;
    }

    public void setCobreOdontologico(String cobreOdontologico) {
        this.cobreOdontologico = cobreOdontologico;
    }

    public Timestamp getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(Timestamp dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    public Timestamp getDataAtualizacao() {
        return dataAtualizacao;
    }

    public void setDataAtualizacao(Timestamp dataAtualizacao) {
        this.dataAtualizacao = dataAtualizacao;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    // hashCode e equals
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        PlanoSaudeEntity other = (PlanoSaudeEntity) obj;
        return Objects.equals(id, other.id);
    }

    // toString (opcional)
    @Override
    public String toString() {
        return "PlanoMedicoEntity{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", codigoANS='" + codigoANS + '\'' +
                '}';
    }
}