package br.upf.consultaMedica.facade;

import br.upf.consultaMedica.entity.PacienteEntity;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
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

    public List<PacienteEntity> buscarTodos() {
        Query query = em.createQuery("SELECT p FROM PacienteEntity p ORDER BY p.nome");
        return query.getResultList();
    }
}
