package edu.acceso.ej2_4.backend;

import java.io.IOException;

/**
 * Interfaz que deben seguir todos los métodos de almacenamiento.
 */
public interface Backend {
    /**
     * Lee del almacenamiento.
     * @return El objeto almacenado.
     * @throws IOException Si no puede leerse el almacenamiento.
     */
    public <O> O[] load() throws IOException;

    /**
     * Guarda los datos en el almacenamiento.
     * @param datos Los datos que se necesita guardar.
     * @throws IOException Si no pueden almacenarse los datos.
     */
    public <O> void save(O[] datos) throws IOException;
}
