package edu.acceso.ej2_4.backend.csv;

import java.text.ParseException;

/**
 * Interfaz quqe debe cumplir toda clase que pretenda almacenarse
 * en formato CSV.
 */
public interface RegistroCsv {

    /**
     * Carga todos los datos de un objeto leídos desde un registro CSV.
     * @param campos Campos de un registro CSV que se convierten en valores
     *          para los atributos corresponidentes del objeto.
     * @throws ParseException Cuando uno de los valores no se puede convertir
     *      en atributo.
     */
    public void cargarCampos(String ... campos) throws ParseException;

    /** 
     * Convierte el objeto en un array de cadenas para poder almacenarlo en un CSV.
     * @return El registro CSV correspondiente.
     * */
    public String[] toCsv();
}