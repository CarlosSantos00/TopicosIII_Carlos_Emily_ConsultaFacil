package br.upf.consultaMedica.facade;

import br.upf.consultaMedica.entity.FuncaoEntity;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class FuncaoFacade extends AbstractFacade<FuncaoEntity> {

    @PersistenceContext(unitName = "ConsultaMedicaPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public FuncaoFacade() {
        super(FuncaoEntity.class);
    }

    public List<FuncaoEntity> buscarTodos() {
        List<FuncaoEntity> entityList = new ArrayList<>();
        try {
            Query query = getEntityManager().createQuery("SELECT f FROM FuncaoEntity f ORDER BY f.descricao");
            entityList = query.getResultList();
        } catch (Exception e) {
            System.out.println("Erro FuncaoFacade: " + e);
        }
        return entityList;
    }
}
