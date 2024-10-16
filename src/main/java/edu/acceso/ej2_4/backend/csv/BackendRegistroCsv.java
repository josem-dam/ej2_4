package edu.acceso.ej2_4.backend.csv;

import java.io.IOException;
import java.nio.file.Path;

import edu.acceso.ej2_4.backend.Backend;

/**
 * Interfaz que deben seguir todos los métodos de almacenamiento.
 */
public interface BackendRegistroCsv extends Backend {

    @Override
    public <T extends RegistroCsv> T[] read(Class<T> clase) throws IOException;

    @Override
    public <T extends RegistroCsv> void save(T[] datos) throws IOException;

}
