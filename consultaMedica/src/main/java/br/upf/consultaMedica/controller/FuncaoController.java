package br.upf.consultaMedica.controller;

import br.upf.consultaMedica.entity.FuncaoEntity;
import br.upf.consultaMedica.facade.FuncaoFacade;
import jakarta.ejb.EJB;
import jakarta.inject.Named;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Named(value = "funcaoController")
@SessionScoped
public class FuncaoController implements Serializable {

    private static final Logger logger = Logger.getLogger(FuncaoController.class.getName());
    private static final long serialVersionUID = 1L;

    @EJB
    private FuncaoFacade ejbFacade;

    private List<FuncaoEntity> funcaoList = new ArrayList<>();

    public List<FuncaoEntity> funcaoAll() {
        try {
            return ejbFacade.buscarTodos();
        } catch (Exception ex) {
            logger.log(Level.SEVERE, "Erro ao buscar todas as funções", ex);
            return new ArrayList<>();
        }
    }

    public List<FuncaoEntity> buscarPorNome(String nome) {
        try {
            if (nome == null || nome.trim().isEmpty()) {
                return new ArrayList<>();
            }
            return ejbFacade.buscarTodos().stream()
                    .filter(f -> f.getDescricao().toLowerCase().contains(nome.toLowerCase()))
                    .toList();
        } catch (Exception ex) {
            logger.log(Level.SEVERE, "Erro ao buscar funções por nome: " + nome, ex);
            return new ArrayList<>();
        }
    }

    public List<FuncaoEntity> getFuncaoList() {
        if (funcaoList == null || funcaoList.isEmpty()) {
            funcaoList = funcaoAll();
        }
        return funcaoList;
    }

    public void setFuncaoList(List<FuncaoEntity> funcaoList) {
        this.funcaoList = funcaoList;
    }

    public FuncaoEntity getFuncao(Integer id) {
        try {
            if (id == null) {
                return null;
            }
            return ejbFacade.find(id);
        } catch (Exception ex) {
            logger.log(Level.SEVERE, "Erro ao buscar função por ID: " + id, ex);
            return null;
        }
    }

    @FacesConverter(forClass = FuncaoEntity.class)
    public static class FuncaoControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.trim().isEmpty()) {
                return null;
            }

            try {
                FuncaoController controller = (FuncaoController) facesContext.getApplication()
                        .getELResolver().getValue(facesContext.getELContext(), null, "funcaoController");
                return controller.getFuncao(getKey(value));
            } catch (Exception ex) {
                Logger.getLogger(FuncaoControllerConverter.class.getName())
                        .log(Level.SEVERE, "Erro na conversão String->FuncaoEntity: " + value, ex);
                return null;
            }
        }

        Integer getKey(String value) {
            try {
                return Integer.valueOf(value);
            } catch (NumberFormatException ex) {
                Logger.getLogger(FuncaoControllerConverter.class.getName())
                        .log(Level.WARNING, "Valor inválido para conversão: " + value, ex);
                return null;
            }
        }

        String getStringKey(Integer value) {
            return (value != null) ? value.toString() : null;
        }

        @Override
public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
    if (object == null) {
        return null;
    }
    if (object instanceof FuncaoEntity) {
        FuncaoEntity funcao = (FuncaoEntity) object;
        return getStringKey(funcao.getId());
    }
    return null;
}
    }
}
