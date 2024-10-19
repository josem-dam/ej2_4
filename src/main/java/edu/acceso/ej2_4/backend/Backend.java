package edu.acceso.ej2_4.backend;

import java.io.IOException;

/**
 * Interfaz que deben seguir todos los métodos de almacenamiento.
 */
public interface Backend {

    /**
     * Lee del almacenamiento.
     * @param tipo La clase de la secuencia de objetos que se pretenden leer.
     * @param <T> Clase de los objetos
     * @return La secuencia de objetos leídos.
     * @throws IOException Si no puede leerse el almacenamiento.
     */
    public <T> T[] read(Class<T> tipo) throws IOException;

    /**
     * Guarda los datos en el almacenamiento.
     * @param datos La secuencia de datos que se necesita guardar.
     * @param <T> Clase de los objetos
     * @return La cantidad de objetos almacenados.
     * @throws IOException Si no pueden almacenarse los datos.
     */
    public <T> int save(T[] datos) throws IOException;
}
