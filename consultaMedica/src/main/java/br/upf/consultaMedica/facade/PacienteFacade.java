package br.upf.consultaMedica.facade;

import br.upf.consultaMedica.entity.PacienteEntity;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class PacienteFacade extends AbstractFacade<PacienteEntity> {

    @PersistenceContext(unitName = "ConsultaMedicaPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public PacienteFacade() {
        super(PacienteEntity.class);
    }

    private List<PacienteEntity> entityList;

    public List<PacienteEntity> buscarTodos() {
        entityList = new ArrayList<>();
        try {
            Query query = getEntityManager().createQuery("SELECT p FROM PacienteEntity p ORDER BY p.nome");
            if (!query.getResultList().isEmpty()) {
                entityList = (List<PacienteEntity>) query.getResultList();
            }
        } catch (Exception e) {
            System.out.println("Erro: " + e);
        }
        return entityList;
    }

    public PacienteEntity buscarPorCpf(String cpf) {
        PacienteEntity paciente = null;
        try {
            Query query = getEntityManager()
                .createQuery("SELECT p FROM PacienteEntity p WHERE p.cpf = :cpf");
            query.setParameter("cpf", cpf);
            paciente = (PacienteEntity) query.getSingleResult();
        } catch (Exception e) {
            paciente = null;
        }
        return paciente;
    }

    public List<PacienteEntity> buscarPorNome(String nome) {
        List<PacienteEntity> pacientes = new ArrayList<>();
        try {
            Query query = getEntityManager()
                .createQuery("SELECT p FROM PacienteEntity p WHERE UPPER(p.nome) LIKE UPPER(:nome) ORDER BY p.nome");
            query.setParameter("nome", "%" + nome + "%");
            if (!query.getResultList().isEmpty()) {
                pacientes = (List<PacienteEntity>) query.getResultList();
            }
        } catch (Exception e) {
            System.out.println("Erro ao buscar por nome: " + e);
        }
        return pacientes;
    }

    public List<PacienteEntity> buscarAtivos() {
        List<PacienteEntity> pacientes = new ArrayList<>();
        try {
            Query query = getEntityManager()
                .createQuery("SELECT p FROM PacienteEntity p WHERE p.ativo = true ORDER BY p.nome");
            if (!query.getResultList().isEmpty()) {
                pacientes = (List<PacienteEntity>) query.getResultList();
            }
        } catch (Exception e) {
            System.out.println("Erro ao buscar pacientes ativos: " + e);
        }
        return pacientes;
    }

    public List<PacienteEntity> buscarPorCidade(Integer cidadeId) {
        List<PacienteEntity> pacientes = new ArrayList<>();
        try {
            Query query = getEntityManager()
                .createQuery("SELECT p FROM PacienteEntity p WHERE p.cidade.id = :cidadeId ORDER BY p.nome");
            query.setParameter("cidadeId", cidadeId);
            if (!query.getResultList().isEmpty()) {
                pacientes = (List<PacienteEntity>) query.getResultList();
            }
        } catch (Exception e) {
            System.out.println("Erro ao buscar por cidade: " + e);
        }
        return pacientes;
    }

    public List<PacienteEntity> buscarPorTipoSanguineo(String tipoSanguineo) {
        List<PacienteEntity> pacientes = new ArrayList<>();
        try {
            Query query = getEntityManager()
                .createQuery("SELECT p FROM PacienteEntity p WHERE p.tipoSanguineo = :tipoSanguineo ORDER BY p.nome");
            query.setParameter("tipoSanguineo", tipoSanguineo);
            if (!query.getResultList().isEmpty()) {
                pacientes = (List<PacienteEntity>) query.getResultList();
            }
        } catch (Exception e) {
            System.out.println("Erro ao buscar por tipo sanguíneo: " + e);
        }
        return pacientes;
    }

    public boolean existeCpf(String cpf) {
        try {
            Query query = getEntityManager()
                .createQuery("SELECT COUNT(p) FROM PacienteEntity p WHERE p.cpf = :cpf");
            query.setParameter("cpf", cpf);
            Long count = (Long) query.getSingleResult();
            return count > 0;
        } catch (Exception e) {
            System.out.println("Erro ao verificar CPF: " + e);
            return false;
        }
    }

    public boolean existeEmail(String email) {
        try {
            Query query = getEntityManager()
                .createQuery("SELECT COUNT(p) FROM PacienteEntity p WHERE p.email = :email");
            query.setParameter("email", email);
            Long count = (Long) query.getSingleResult();
            return count > 0;
        } catch (Exception e) {
            System.out.println("Erro ao verificar email: " + e);
            return false;
        }
    }
}