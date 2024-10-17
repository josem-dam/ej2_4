package edu.acceso.ej2_4.backend;

import java.io.IOException;
import java.nio.file.Path;

/**
 * Interfaz que deben seguir todos los métodos de almacenamiento.
 */
public interface Backend {

    /**
     * Lee del almacenamiento.
     * @param tipo La clase de la secuencia de objetos que se pretenden leer.
     * @return La secuencia de objetos leídos.
     * @throws IOException Si no puede leerse el almacenamiento.
     */
    public <T> T[] read(Class<T> tipo) throws IOException;

    /**
     * Guarda los datos en el almacenamiento.
     * @param datos La secuencia de datos que se necesita guardar.
     * @throws IOException Si no pueden almacenarse los datos.
     */
    public <T> void save(T[] datos) throws IOException;

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
