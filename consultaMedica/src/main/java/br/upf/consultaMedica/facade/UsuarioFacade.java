package br.upf.consultaMedica.facade;

import br.upf.consultaMedica.entity.UsuarioEntity;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class UsuarioFacade extends AbstractFacade<UsuarioEntity> {

    @PersistenceContext(unitName = "ConsultaMedicaPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public UsuarioFacade() {
        super(UsuarioEntity.class);
    }

    private List<UsuarioEntity> entityList;

    public List<UsuarioEntity> buscarTodos() {
        entityList = new ArrayList<>();
        try {
            Query query = getEntityManager().createQuery("SELECT u FROM UsuarioEntity u ORDER BY u.nome");
            if (!query.getResultList().isEmpty()) {
                entityList = (List<UsuarioEntity>) query.getResultList();
            }
        } catch (Exception e) {
            System.out.println("Erro: " + e);
        }
        return entityList;
    }
    
        public List<UsuarioEntity> buscarTodosMedicos() {
        entityList = new ArrayList<>();
        try {
            Query query = getEntityManager().createQuery("SELECT u FROM UsuarioEntity u WHERE u.idFuncao.id = 3 ORDER BY u.nome");
            if (!query.getResultList().isEmpty()) {
                entityList = (List<UsuarioEntity>) query.getResultList();
            }
        } catch (Exception e) {
            System.out.println("Erro: " + e);
        }
        return entityList;
    }

    public UsuarioEntity buscarPorEmailESenha(String email, String senha) {
        UsuarioEntity usuario = null;
    try {
        Query query = getEntityManager()
            .createQuery("SELECT u FROM UsuarioEntity u WHERE u.email = :email AND u.senha = :senha");
        query.setParameter("email", email);
        query.setParameter("senha", senha);

        usuario = (UsuarioEntity) query.getSingleResult();
    } catch (Exception e) {
        usuario = null;
    }
    return usuario;
}
}
