package edu.acceso.ej2_4.backend.csv;

import java.text.ParseException;

import edu.acceso.ej2_4.Estudiante;
import edu.acceso.ej2_4.Estudios;

public class EstudianteCsv extends Estudiante implements RegistroCsv {

    public static String[] columnas = new String[] { "Nombre", "Apellidos", "Nacimiento", "Estudios" };

    @Override
    public void cargarCampos(String ... campos) throws ParseException {
        this.cargarDatos(campos[0], campos[1], df.parse(campos[2]), Estudios.valueOf(campos[3]));
    }

    @Override
    public String[] toCsv() {
        return new String[] {
            getNombre(),
            getApellidos(),
            df.format(getNacimiento()),
            getEstudios().toString()
        };
    }
}