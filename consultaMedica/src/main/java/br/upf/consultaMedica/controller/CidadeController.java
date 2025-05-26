package br.upf.consultaMedica.controller;

import br.upf.consultaMedica.entity.CidadeEntity;
import br.upf.consultaMedica.facade.CidadeFacade;
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

@Named(value = "cidadeController")
@SessionScoped
public class CidadeController implements Serializable {

    private static final Logger logger = Logger.getLogger(CidadeController.class.getName());
    private static final long serialVersionUID = 1L;

    @EJB
    private CidadeFacade ejbFacade;

    private List<CidadeEntity> cidadeList = new ArrayList<>();

    /**
     * Retorna todas as cidades cadastradas
     * @return Lista de todas as cidades
     */
    public List<CidadeEntity> cidadeAll() {
        try {
            return ejbFacade.buscarTodos();
        } catch (Exception ex) {
            logger.log(Level.SEVERE, "Erro ao buscar todas as cidades", ex);
            return new ArrayList<>();
        }
    }

    /**
     * Busca cidades por nome (útil para autocomplete)
     * @param nome Nome ou parte do nome da cidade
     * @return Lista de cidades que contém o nome informado
     */
    public List<CidadeEntity> buscarPorNome(String nome) {
        try {
            if (nome == null || nome.trim().isEmpty()) {
                return new ArrayList<>();
            }
            return ejbFacade.buscarTodos().stream()
                    .filter(c -> c.getNome().toLowerCase().contains(nome.toLowerCase()))
                    .toList();
        } catch (Exception ex) {
            logger.log(Level.SEVERE, "Erro ao buscar cidades por nome: " + nome, ex);
            return new ArrayList<>();
        }
    }

    /**
     * Getter para lista de cidades com lazy loading
     * @return Lista de cidades
     */
    public List<CidadeEntity> getCidadeList() {
        if (cidadeList == null || cidadeList.isEmpty()) {
            cidadeList = cidadeAll();
        }
        return cidadeList;
    }

    public void setCidadeList(List<CidadeEntity> cidadeList) {
        this.cidadeList = cidadeList;
    }

    /**
     * Busca uma cidade por ID
     * @param id ID da cidade
     * @return CidadeEntity encontrada ou null
     */
    public CidadeEntity getCidade(Integer id) {
        try {
            if (id == null) {
                return null;
            }
            return ejbFacade.find(id);
        } catch (Exception ex) {
            logger.log(Level.SEVERE, "Erro ao buscar cidade por ID: " + id, ex);
            return null;
        }
    }

    /**
     * Converter para JSF - Conversão entre String e CidadeEntity
     */
    @FacesConverter(forClass = CidadeEntity.class)
    public static class CidadeControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.trim().isEmpty()) {
                return null;
            }
            
            try {
                CidadeController controller = (CidadeController) facesContext.getApplication()
                        .getELResolver().getValue(facesContext.getELContext(), null, "cidadeController");
                return controller.getCidade(getKey(value));
            } catch (Exception ex) {
                Logger.getLogger(CidadeControllerConverter.class.getName())
                        .log(Level.SEVERE, "Erro na conversão String->CidadeEntity: " + value, ex);
                return null;
            }
        }

        Integer getKey(String value) {
            try {
                return Integer.valueOf(value);
            } catch (NumberFormatException ex) {
                Logger.getLogger(CidadeControllerConverter.class.getName())
                        .log(Level.WARNING, "Valor inválido para conversão: " + value, ex);
                return null;
            }
        }

        String getStringKey(Integer value) {
            if (value == null) {
                return null;
            }
            return value.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof CidadeEntity) {
                CidadeEntity cidade = (CidadeEntity) object;
                return getStringKey(cidade.getId());
            }
            return null;
        }
    }
}