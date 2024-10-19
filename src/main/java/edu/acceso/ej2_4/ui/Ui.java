package edu.acceso.ej2_4.ui;

import java.nio.file.Path;

/**
 * Interfaz que deben cumplir las interfaces de usuario.
 */
public interface Ui {
    /**
     * Lanza la interfaz de interacción con el usuario.
     */
    public void start();

    /**
     * Genera la ruta del archivo donde se almacenan los datos.
     * @param tipo Tipo de objeto que se pretende almacenar.
     * @param formato Formato del archivo que se usará para obtener la extensión.
     * @return La ruta del archivo
     */
    public static Path generarRuta(Class<?> tipo, String formato) {
        String tmp = System.getProperty("java.io.tmpdir");
        String nombre = tipo.getSimpleName().toLowerCase();

        return Path.of(tmp, String.format("%s.%s", nombre, formato.toLowerCase()));
    } 
}
