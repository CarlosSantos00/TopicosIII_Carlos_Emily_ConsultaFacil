package br.upf.consultaMedica.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "pacientes")
public class PacienteEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;

    @NotNull(message = "Nome é obrigatório")
    @Column(name = "nome")
    private String nome;

    @NotNull(message = "CPF é obrigatório")
    @Column(name = "cpf", unique = true)
    private String cpf;

    @Column(name = "rg")
    private String rg;

    @NotNull(message = "Data de nascimento é obrigatória")
    @Temporal(TemporalType.DATE)
    @Column(name = "data_nascimento")
    private Date dataNascimento;

    @Pattern(regexp = "[MFO]", message = "Sexo deve ser M, F ou O")
    @Column(name = "sexo")
    private String sexo;

    @Column(name = "estado_civil")
    private String estadoCivil;

    @Column(name = "profissao")
    private String profissao;

    @Column(name = "celular")
    private String celular;

    @Email(message = "Email deve ter um formato válido")
    @Column(name = "email")
    private String email;

    @Size(max = 150, message = "Endereço deve ter no máximo 150 caracteres")
    @Column(name = "endereco")
    private String endereco;

    @Size(max = 10, message = "Número deve ter no máximo 10 caracteres")
    @Column(name = "numero")
    private String numero;

    @Size(max = 50, message = "Bairro deve ter no máximo 50 caracteres")
    @Column(name = "bairro")
    private String bairro;

    @Size(max = 2, message = "UF deve ter no máximo 2 caracteres")
    @Column(name = "uf")
    private String uf;

    @Column(name = "cep")
    private String cep;

    @Size(max = 3, message = "Tipo sanguíneo deve ter no máximo 3 caracteres")
    @Column(name = "tipo_sanguineo")
    private String tipoSanguineo;

    @Column(name = "alergias")
    private String alergias;

    @Column(name = "medicamentos_uso")
    private String medicamentosUso;

    @Column(name = "observacoes")
    private String observacoes;

    @Size(max = 100, message = "Nome do contato de emergência deve ter no máximo 100 caracteres")
    @Column(name = "contato_emergencia_nome")
    private String contatoEmergenciaNome;

    @Size(max = 30, message = "Parentesco do contato de emergência deve ter no máximo 30 caracteres")
    @Column(name = "contato_emergencia_parentesco")
    private String contatoEmergenciaParentesco;

    @Size(max = 15, message = "Telefone do contato de emergência deve ter no máximo 15 caracteres")
    @Column(name = "contato_emergencia_telefone")
    private String contatoEmergenciaTelefone;

    @Column(name = "data_cadastro", insertable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataCadastro;

    @Column(name = "data_atualizacao", insertable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataAtualizacao;

    @ManyToOne(optional = true)
    @JoinColumn(name = "cidade", referencedColumnName = "id")
    private CidadeEntity cidade;

    // Construtor padrão
    public PacienteEntity() {}

    // Getters e Setters

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getRg() {
        return rg;
    }

    public void setRg(String rg) {
        this.rg = rg;
    }

    public Date getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(Date dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getEstadoCivil() {
        return estadoCivil;
    }

    public void setEstadoCivil(String estadoCivil) {
        this.estadoCivil = estadoCivil;
    }

    public String getProfissao() {
        return profissao;
    }

    public void setProfissao(String profissao) {
        this.profissao = profissao;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getUf() {
        return uf;
    }

    public void setUf(String uf) {
        this.uf = uf;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getTipoSanguineo() {
        return tipoSanguineo;
    }

    public void setTipoSanguineo(String tipoSanguineo) {
        this.tipoSanguineo = tipoSanguineo;
    }

    public String getAlergias() {
        return alergias;
    }

    public void setAlergias(String alergias) {
        this.alergias = alergias;
    }

    public String getMedicamentosUso() {
        return medicamentosUso;
    }

    public void setMedicamentosUso(String medicamentosUso) {
        this.medicamentosUso = medicamentosUso;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    public String getContatoEmergenciaNome() {
        return contatoEmergenciaNome;
    }

    public void setContatoEmergenciaNome(String contatoEmergenciaNome) {
        this.contatoEmergenciaNome = contatoEmergenciaNome;
    }

    public String getContatoEmergenciaParentesco() {
        return contatoEmergenciaParentesco;
    }

    public void setContatoEmergenciaParentesco(String contatoEmergenciaParentesco) {
        this.contatoEmergenciaParentesco = contatoEmergenciaParentesco;
    }

    public String getContatoEmergenciaTelefone() {
        return contatoEmergenciaTelefone;
    }

    public void setContatoEmergenciaTelefone(String contatoEmergenciaTelefone) {
        this.contatoEmergenciaTelefone = contatoEmergenciaTelefone;
    }

    public Date getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(Date dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    public Date getDataAtualizacao() {
        return dataAtualizacao;
    }

    public void setDataAtualizacao(Date dataAtualizacao) {
        this.dataAtualizacao = dataAtualizacao;
    }


    public CidadeEntity getCidade() {
        if (cidade == null) {
            cidade = new CidadeEntity();
        }
        return cidade;
    }
    
    public void setCidade(CidadeEntity cidade) {
        this.cidade = cidade;
    }  

    // hashCode e equals com base no campo id
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof PacienteEntity)) return false;
        PacienteEntity other = (PacienteEntity) obj;
        return Objects.equals(id, other.id);
    }

    @Override
    public String toString() {
        return "PacienteEntity{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", cpf='" + cpf + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}