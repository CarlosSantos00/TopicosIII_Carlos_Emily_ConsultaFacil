package br.upf.consultaMedica.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "usuarios")
public class UsuarioEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "COD")
    private Integer cod;

    @NotNull
    @Size(min = 1, max = 500)
    @Column(name = "NOME")
    private String nome;

    @NotNull
    @Temporal(TemporalType.DATE)
    @Column(name = "DTA_NASCIMENTO")
    private Date dtaNascimento;

    @NotNull
    @Size(min = 1, max = 250)
    @Column(name = "EMAIL")
    private String email;

    @Column(name = "OBSERVACAO")
    private String observacao;
    
    @Column(name = "CRM")
    private String crm;

    @NotNull
    @Column(name = "SENHA")
    private String senha;

    @NotNull
    @Column(name = "DTA_REGISTRO", insertable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtaRegistro;

    @ManyToOne(optional = false)
    @JoinColumn(name = "ID_CIDADE", referencedColumnName = "id")
    private CidadeEntity idCidade;

    @ManyToOne(optional = false)
    @JoinColumn(name = "ID_FUNCAO", referencedColumnName = "id")
    private FuncaoEntity idFuncao;

    @ManyToOne(optional = true)
    @JoinColumn(name = "ID_ESPECIALIDADE", referencedColumnName = "id")
    private EspecialidadeEntity idEspecialidade;

    // Getters e Setters

    public Integer getCod() {
        return cod;
    }

    public void setCod(Integer cod) {
        this.cod = cod;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Date getDtaNascimento() {
        return dtaNascimento;
    }
    
    public String getCrm() {
        return crm;
    }

    public void setCrm(String crm) {
        this.crm = crm;
    }

    public void setDtaNascimento(Date dtaNascimento) {
        this.dtaNascimento = dtaNascimento;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public Date getDtaRegistro() {
        return dtaRegistro;
    }

    public void setDtaRegistro(Date dtaRegistro) {
        this.dtaRegistro = dtaRegistro;
    }

    public CidadeEntity getIdCidade() {
        return idCidade;
    }

    public void setIdCidade(CidadeEntity idCidade) {
        this.idCidade = idCidade;
    }

    public FuncaoEntity getIdFuncao() {
        return idFuncao;
    }

    public void setIdFuncao(FuncaoEntity idFuncao) {
        this.idFuncao = idFuncao;
    }

    public EspecialidadeEntity getIdEspecialidade() {
        return idEspecialidade;
    }

    public void setIdEspecialidade(EspecialidadeEntity idEspecialidade) {
        this.idEspecialidade = idEspecialidade;
    }

    // hashCode e equals com base no campo cod

    @Override
    public int hashCode() {
        return Objects.hash(cod);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof UsuarioEntity)) return false;
        UsuarioEntity other = (UsuarioEntity) obj;
        return Objects.equals(cod, other.cod);
    }
}
