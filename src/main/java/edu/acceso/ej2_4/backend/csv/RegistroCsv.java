package edu.acceso.ej2_4.backend.csv;

import java.text.ParseException;

public interface RegistroCsv {

    /**
     * Carga todos los datos de un objeto leídos desde un registro CSV.
     * @param datos
     */
    public void cargarCampos(String ... campos) throws ParseException;
    /** 
     * Convierte el objeto en un array de cadenas para poder almacenarlo en un CSV.
     * @return El registro CSV correspondiente.
     * */
    public String[] toCsv();
}