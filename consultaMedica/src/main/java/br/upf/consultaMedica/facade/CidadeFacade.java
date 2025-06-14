package br.upf.consultaMedica.facade;

import br.upf.consultaMedica.entity.CidadeEntity;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class CidadeFacade extends AbstractFacade<CidadeEntity> {

    @PersistenceContext(unitName = "ConsultaMedicaPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CidadeFacade() {
        super(CidadeEntity.class);
    }

    public List<CidadeEntity> buscarTodos() {
        List<CidadeEntity> entityList = new ArrayList<>();
        try {
            Query query = getEntityManager().createQuery("SELECT c FROM CidadeEntity c ORDER BY c.nome");
            entityList = query.getResultList();
        } catch (Exception e) {
            System.out.println("Erro CidadeFacade: " + e);
        }
        return entityList;
    }
}
