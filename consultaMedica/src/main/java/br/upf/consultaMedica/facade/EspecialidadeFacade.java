package br.upf.consultaMedica.facade;

import br.upf.consultaMedica.entity.EspecialidadeEntity;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class EspecialidadeFacade extends AbstractFacade<EspecialidadeEntity> {

    @PersistenceContext(unitName = "ConsultaMedicaPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public EspecialidadeFacade() {
        super(EspecialidadeEntity.class);
    }

    public List<EspecialidadeEntity> buscarTodos() {
        List<EspecialidadeEntity> entityList = new ArrayList<>();
        try {
            Query query = getEntityManager().createQuery("SELECT e FROM EspecialidadeEntity e ORDER BY e.descricao");
            entityList = query.getResultList();
        } catch (Exception e) {
            System.out.println("Erro EspecialidadeFacade: " + e);
        }
        return entityList;
    }
}
