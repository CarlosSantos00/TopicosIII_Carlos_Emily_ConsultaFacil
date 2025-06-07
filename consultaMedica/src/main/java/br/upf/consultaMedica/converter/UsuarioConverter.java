package br.upf.consultaMedica.converter;

import br.upf.consultaMedica.entity.UsuarioEntity;
import br.upf.consultaMedica.facade.UsuarioFacade;
import jakarta.ejb.EJB;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;

@FacesConverter(value = "usuarioConverter", managed = true)
public class UsuarioConverter implements Converter<UsuarioEntity> {

    @EJB
    private UsuarioFacade usuarioFacade;

    @Override
    public UsuarioEntity getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.trim().isEmpty() || "null".equals(value)) {
            return null;
        }
        
        try {
            // Assumindo que o campo 'cod' é do tipo Long
            // Se for String, remova o Long.valueOf()
            Long cod = Long.valueOf(value);
            return usuarioFacade.find(cod);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, UsuarioEntity value) {
        if (value == null || value.getCod() == null) {
            return "";
        }
        return value.getCod().toString();
    }
}