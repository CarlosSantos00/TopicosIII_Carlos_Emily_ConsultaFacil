package br.upf.consultaMedica.controller;

import br.upf.consultaMedica.entity.EspecialidadeEntity;
import br.upf.consultaMedica.facade.EspecialidadeFacade;
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

@Named(value = "especialidadeController")
@SessionScoped
public class EspecialidadeController implements Serializable {

    private static final Logger logger = Logger.getLogger(EspecialidadeController.class.getName());
    private static final long serialVersionUID = 1L;

    @EJB
    private EspecialidadeFacade ejbFacade;

    private List<EspecialidadeEntity> especialidadeList = new ArrayList<>();

    public List<EspecialidadeEntity> especialidadeAll() {
        try {
            return ejbFacade.buscarTodos();
        } catch (Exception ex) {
            logger.log(Level.SEVERE, "Erro ao buscar todas as especialidades", ex);
            return new ArrayList<>();
        }
    }

    public List<EspecialidadeEntity> buscarPorNome(String nome) {
        try {
            if (nome == null || nome.trim().isEmpty()) {
                return new ArrayList<>();
            }
            return ejbFacade.buscarTodos().stream()
                    .filter(e -> e.getDescricao().toLowerCase().contains(nome.toLowerCase()))
                    .toList();
        } catch (Exception ex) {
            logger.log(Level.SEVERE, "Erro ao buscar especialidades por nome: " + nome, ex);
            return new ArrayList<>();
        }
    }

    public List<EspecialidadeEntity> getEspecialidadeList() {
        if (especialidadeList == null || especialidadeList.isEmpty()) {
            especialidadeList = especialidadeAll();
        }
        return especialidadeList;
    }

    public void setEspecialidadeList(List<EspecialidadeEntity> especialidadeList) {
        this.especialidadeList = especialidadeList;
    }

    public EspecialidadeEntity getEspecialidade(Integer id) {
        try {
            if (id == null) {
                return null;
            }
            return ejbFacade.find(id);
        } catch (Exception ex) {
            logger.log(Level.SEVERE, "Erro ao buscar especialidade por ID: " + id, ex);
            return null;
        }
    }

    @FacesConverter(forClass = EspecialidadeEntity.class)
    public static class EspecialidadeControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.trim().isEmpty()) {
                return null;
            }

            try {
                EspecialidadeController controller = (EspecialidadeController) facesContext.getApplication()
                        .getELResolver().getValue(facesContext.getELContext(), null, "especialidadeController");
                return controller.getEspecialidade(getKey(value));
            } catch (Exception ex) {
                Logger.getLogger(EspecialidadeControllerConverter.class.getName())
                        .log(Level.SEVERE, "Erro na conversão String->EspecialidadeEntity: " + value, ex);
                return null;
            }
        }

        Integer getKey(String value) {
            try {
                return Integer.valueOf(value);
            } catch (NumberFormatException ex) {
                Logger.getLogger(EspecialidadeControllerConverter.class.getName())
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
            if (object instanceof EspecialidadeEntity) {
                EspecialidadeEntity especialidade = (EspecialidadeEntity) object;
                return getStringKey(especialidade.getId());
            }
            return null;
        }
    }
}
