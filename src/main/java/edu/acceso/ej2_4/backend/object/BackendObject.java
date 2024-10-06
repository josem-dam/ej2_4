package edu.acceso.ej2_4.backend.object;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import edu.acceso.ej2_4.backend.Backend;

/**
 * Serializa un objeto para almacenarlo en un archivo.
 */
public class BackendObject implements Backend {

    private Path archivo;

    /**
     * Constructor de la clase.
     * @param archivo Ruta del archivo de almacenamiento.
     */
    public BackendObject(Path archivo) {
        this.archivo = archivo;
    }

    /**
     * Almacena el objeto serializado en un archivo.
     * @param datos El objeto a serializar.
     * @throws IOException Si hay algún problema con el archivo de almacenamiento.
     */
    @Override
    public void save(Object[] datos) throws IOException {
        try (
            OutputStream os = Files.newOutputStream(archivo);
            ObjectOutputStream oss = new ObjectOutputStream(os)
        ) {
            oss.writeObject(datos);
        }
    }

    /**
     * Recupera de archivo el objeto serializado.
     * @return El objeto deserializado.
     * @throws IOException Si hay algún problema con el archivo de almacenamiento.
     */
    @Override
    public Object[] read() throws IOException {
        try (
            InputStream is = Files.newInputStream(archivo);
            ObjectInputStream ois = new ObjectInputStream(is);
        ) {
            return (Object[]) ois.readObject();
        }
        catch(ClassNotFoundException err) {
            err.printStackTrace();
            return null;
        }
    }
}
