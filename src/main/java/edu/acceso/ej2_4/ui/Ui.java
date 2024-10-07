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
     * @param archivo Nombre que se quiere dar al archivo.
     * @param formato Formato del archivo que se usará para obtener la extensión.
     * @return La ruta del archivo
     */
    public static Path generarRuta(String archivo, String formato) {
        Path ruta;

        if(archivo != null) {
            ruta = Path.of(archivo);
        }
        else {
            String tmp = System.getProperty("java.io.tmpdir");
            String nombre = "estudiantes";

            ruta = Path.of(tmp, String.format("%s.%s", nombre, formato));
        }

        return ruta;
    }
}
