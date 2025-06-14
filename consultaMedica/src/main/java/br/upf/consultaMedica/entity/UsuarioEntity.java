package br.upf.consultaMedica.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Email;
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

    @NotNull(message = "Nome é obrigatório")
    @Size(min = 1, max = 500, message = "Nome deve ter entre 1 e 500 caracteres")
    @Column(name = "NOME")
    private String nome;

    @NotNull(message = "Data de nascimento é obrigatória")
    @Temporal(TemporalType.DATE)
    @Column(name = "DTA_NASCIMENTO")
    private Date dtaNascimento;

    @NotNull(message = "Email é obrigatório")
    @Email(message = "Email deve ter um formato válido")
    @Size(min = 1, max = 250, message = "Email deve ter entre 1 e 250 caracteres")
    @Column(name = "EMAIL")
    private String email;

    @Column(name = "OBSERVACAO")
    private String observacao;
    
    // CRM pode ser opcional dependendo do tipo de usuário
    @Size(max = 20, message = "CRM deve ter no máximo 20 caracteres")
    @Column(name = "CRM")
    private String crm;

    @NotNull(message = "Senha é obrigatória")
    @Size(min = 1, message = "Senha não pode estar vazia")
    @Column(name = "SENHA")
    private String senha;
    
    @NotNull(message = "Contato é obrigatório")
    @Size(min = 1, max = 500, message = "Contato deve ter entre 1 e 500 caracteres")
    @Column(name = "CONTATO")
    private String contato;

    @Column(name = "DTA_REGISTRO", insertable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtaRegistro;

    @NotNull(message = "Cidade é obrigatória")
    @ManyToOne(optional = false)
    @JoinColumn(name = "ID_CIDADE", referencedColumnName = "id")
    private CidadeEntity idCidade;

    @NotNull(message = "Função é obrigatória")
    @ManyToOne(optional = false)
    @JoinColumn(name = "ID_FUNCAO", referencedColumnName = "id")
    private FuncaoEntity idFuncao;

    @ManyToOne(optional = true)
    @JoinColumn(name = "ID_ESPECIALIDADE", referencedColumnName = "id")
    private EspecialidadeEntity idEspecialidade;

    // Construtor padrão
    public UsuarioEntity() {}

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
    
    public String getContato() {
        return contato;
    }

    public void setContato(String contato) {
        this.contato = contato;
    }

    // Método para verificar se é um médico (tem CRM)
    public boolean isMedico() {
        return crm != null && !crm.trim().isEmpty();
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

    @Override
    public String toString() {
        return "UsuarioEntity{" +
                "cod=" + cod +
                ", nome='" + nome + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}