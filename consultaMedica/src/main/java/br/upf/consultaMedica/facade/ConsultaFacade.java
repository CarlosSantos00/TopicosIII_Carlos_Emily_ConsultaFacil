package br.upf.consultaMedica.facade;

import br.upf.consultaMedica.entity.ConsultaEntity;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Stateless
public class ConsultaFacade extends AbstractFacade<ConsultaEntity> {
    
    @PersistenceContext(unitName = "ConsultaMedicaPU")
    private EntityManager em;
    
    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
    public ConsultaFacade() {
        super(ConsultaEntity.class);
    }
    
    public List<ConsultaEntity> buscarTodos() {
    try {
        return em.createQuery(
            "SELECT c FROM ConsultaEntity c " +
            "LEFT JOIN FETCH c.paciente " + 
            "LEFT JOIN FETCH c.medico " +   
            "ORDER BY c.dataHora", 
            ConsultaEntity.class
        ).getResultList();
    } catch (Exception e) {
        Logger.getLogger(ConsultaFacade.class.getName()).log(Level.SEVERE, null, e);
        return new ArrayList<>();
    }
}
    
    public List<ConsultaEntity> buscarPorFiltros(Timestamp dataInicio, Timestamp dataFim, String status) {
        List<ConsultaEntity> entityList = new ArrayList<>();
        try {
            StringBuilder jpql = new StringBuilder("SELECT c FROM ConsultaEntity c WHERE 1=1 ");
            
            if (dataInicio != null) {
                jpql.append("AND c.dataConsulta >= :dataInicio ");
            }
            
            if (dataFim != null) {
                jpql.append("AND c.dataConsulta <= :dataFim ");
            }
            
            if (status != null && !status.trim().isEmpty()) {
                jpql.append("AND c.status = :status ");
            }
            
            jpql.append("ORDER BY c.dataConsulta");
            
            TypedQuery<ConsultaEntity> query = getEntityManager().createQuery(jpql.toString(), ConsultaEntity.class);
            
            if (dataInicio != null) {
                query.setParameter("dataInicio", dataInicio);
            }
            
            if (dataFim != null) {
                query.setParameter("dataFim", dataFim);
            }
            
            if (status != null && !status.trim().isEmpty()) {
                query.setParameter("status", status);
            }
            
            entityList = query.getResultList();
            
        } catch (Exception e) {
            System.out.println("Erro ConsultaFacade buscarPorFiltros: " + e);
        }
        return entityList;
    }
    
    public List<ConsultaEntity> buscarPorStatus(String status) {
        List<ConsultaEntity> entityList = new ArrayList<>();
        try {
            TypedQuery<ConsultaEntity> query = getEntityManager().createQuery(
                "SELECT c FROM ConsultaEntity c WHERE c.status = :status ORDER BY c.dataConsulta", 
                ConsultaEntity.class
            );
            query.setParameter("status", status);
            entityList = query.getResultList();
        } catch (Exception e) {
            System.out.println("Erro ConsultaFacade buscarPorStatus: " + e);
        }
        return entityList;
    }
    
    public List<ConsultaEntity> buscarPorPeriodo(Timestamp dataInicio, Timestamp dataFim) {
        List<ConsultaEntity> entityList = new ArrayList<>();
        try {
            TypedQuery<ConsultaEntity> query = getEntityManager().createQuery(
                "SELECT c FROM ConsultaEntity c WHERE c.dataConsulta BETWEEN :dataInicio AND :dataFim ORDER BY c.dataConsulta", 
                ConsultaEntity.class
            );
            query.setParameter("dataInicio", dataInicio);
            query.setParameter("dataFim", dataFim);
            entityList = query.getResultList();
        } catch (Exception e) {
            System.out.println("Erro ConsultaFacade buscarPorPeriodo: " + e);
        }
        return entityList;
    }
}