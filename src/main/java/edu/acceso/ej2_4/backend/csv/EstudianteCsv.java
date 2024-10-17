package edu.acceso.ej2_4.backend.csv;

import java.util.Date;
import java.text.ParseException;

import edu.acceso.ej2_4.Estudiante;
import edu.acceso.ej2_4.Estudios;

/**
 * Añade a la clase Estudiante las particularidades propias
 * de su almacenamiento fomo CSV.
 */
public class EstudianteCsv extends Estudiante implements RegistroCsv {

    /**
     * Nombre de las columnas en el CSV.
     */
    // Este valor estático deben incluirlo todas las clases que pretendan almacenarse como CSV,
    // pero no hay forma en Java de declarar nada estático en la interfaz.
    public static String[] columnas = new String[] { "Nombre", "Apellidos", "Nacimiento", "Estudios" };

    @Override
    public void cargarCampos(String ... campos) throws ParseException {
        Date nacimiento = campos[3] != ""?df.parse(campos[3]):null;
        Estudios previos = null;
        try { previos = Estudios.valueOf(campos[4]); }
        catch(IllegalArgumentException|NullPointerException err) {}

        this.cargarDatos(Long.parseLong(campos[0]), campos[1], campos[2], nacimiento, previos);
    }

    @Override
    public String[] toCsv() {
        String nacimiento = getNacimiento() != null?df.format(getNacimiento()):null;
        String previos = getEstudios() != null?getEstudios().name():null;

        return new String[] {
            Long.toString(getMatricula()),
            getNombre(),
            getApellidos(),
            nacimiento,
            previos
        };
    }
}