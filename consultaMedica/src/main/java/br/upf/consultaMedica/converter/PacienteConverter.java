package br.upf.consultaMedica.converter;

import br.upf.consultaMedica.entity.PacienteEntity;
import br.upf.consultaMedica.facade.PacienteFacade;
import jakarta.ejb.EJB;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;

@FacesConverter(value = "pacienteConverter", managed = true)
public class PacienteConverter implements Converter<PacienteEntity> {

    @EJB
    private PacienteFacade pacienteFacade;

    @Override
    public PacienteEntity getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.trim().isEmpty() || "null".equals(value)) {
            return null;
        }
        
        try {
            Long id = Long.valueOf(value);
            return pacienteFacade.find(id);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, PacienteEntity value) {
        if (value == null || value.getId() == null) {
            return "";
        }
        return value.getId().toString();
    }
}