package br.upf.consultaMedica.facade;

import br.upf.consultaMedica.entity.PlanoSaudeEntity;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class PlanoSaudeFacade extends AbstractFacade<PlanoSaudeEntity> {

    @PersistenceContext(unitName = "ConsultaMedicaPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public PlanoSaudeFacade() {
        super(PlanoSaudeEntity.class);
    }

    private List<PlanoSaudeEntity> entityList;

    /**
     * Busca todos os planos de saúde ordenados por nome
     * @return 
     */
    public List<PlanoSaudeEntity> buscarTodos() {
        entityList = new ArrayList<>();
        try {
            Query query = getEntityManager().createQuery("SELECT p FROM PlanoSaudeEntity p ORDER BY p.nome");
            if (!query.getResultList().isEmpty()) {
                entityList = (List<PlanoSaudeEntity>) query.getResultList();
            }
        } catch (Exception e) {
            System.out.println("Erro ao buscar planos de saúde: " + e.getMessage());
        }
        return entityList;
    }

    /**
     * Busca um plano de saúde pelo código ANS
     * @return 
     */
    public PlanoSaudeEntity buscarPorCodigoANS(String codigoANS) {
        PlanoSaudeEntity plano = null;
        try {
            Query query = getEntityManager()
                .createQuery("SELECT p FROM PlanoSaudeEntity p WHERE p.codigoANS = :codigoANS");
            query.setParameter("codigoANS", codigoANS);
            plano = (PlanoSaudeEntity) query.getSingleResult();
        } catch (Exception e) {
            System.out.println("Plano não encontrado para o código ANS: " + codigoANS);
        }
        return plano;
    }

    /**
     * Busca planos de saúde por tipo (Individual, Familiar, etc)
     * @param tipoPlano
     * @return 
     */
    public List<PlanoSaudeEntity> buscarPorTipo(String tipoPlano) {
        List<PlanoSaudeEntity> planos = new ArrayList<>();
        try {
            Query query = getEntityManager()
                .createQuery("SELECT p FROM PlanoSaudeEntity p WHERE p.tipoPlano = :tipo ORDER BY p.nome");
            query.setParameter("tipo", tipoPlano);
            planos = (List<PlanoSaudeEntity>) query.getResultList();
        } catch (Exception e) {
            System.out.println("Erro ao buscar planos por tipo: " + e.getMessage());
        }
        return planos;
    }

    /**
     * Busca planos que cobrem odontológico
     * @return 
     */
    public List<PlanoSaudeEntity> buscarPlanosComOdontologico() {
        List<PlanoSaudeEntity> planos = new ArrayList<>();
        try {
            Query query = getEntityManager()
                .createQuery("SELECT p FROM PlanoSaudeEntity p WHERE p.cobreOdontologico IS NOT NULL AND p.cobreOdontologico != 'Não'");
            planos = (List<PlanoSaudeEntity>) query.getResultList();
        } catch (Exception e) {
            System.out.println("Erro ao buscar planos com odontológico: " + e.getMessage());
        }
        return planos;
    }

    /**
     * Cria e retorna o plano persistido
     * @param plano
     * @return 
     */
    @Override
    public PlanoSaudeEntity createReturn(PlanoSaudeEntity plano) {
        try {
            getEntityManager().persist(plano);
            getEntityManager().flush();
            return plano;
        } catch (Exception e) {
            System.out.println("Erro ao persistir plano de saúde: " + e.getMessage());
            return null;
        }
    }
}