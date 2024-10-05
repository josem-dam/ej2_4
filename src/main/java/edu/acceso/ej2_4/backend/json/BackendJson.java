package edu.acceso.ej2_4.backend.json;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.nio.file.Path;
//import java.text.SimpleDateFormat;
import java.util.TimeZone;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import edu.acceso.ej2_4.backend.Backend;

/**
 * Serializa un objeto para almacenarlo en un archivo.
 */
public class BackendJson implements Backend {

    /**
     * Ruta del archivo de almacenamiento.
     */
    private Path archivo;
    private ObjectMapper mapper;
    //private static SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");

    /**
     * Constructor de la clase.
     * @param archivo Ruta del archivo de almacenamiento.
     */
    public BackendJson(Path archivo) {
        this.archivo = archivo;
        mapper = new ObjectMapper();
        //mapper.setDateFormat(df);
        mapper.setTimeZone(TimeZone.getDefault());
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    /**
     * Almacena el objeto serializado en un archivo.
     * @param datos El objeto a serializar.
     * @throws IOException Si hay algún problema con el archivo de almacenamiento.
     */
    @Override
    public void save(Object[] datos) throws IOException {
        try (
            OutputStream st = Files.newOutputStream(archivo);
            OutputStreamWriter sw = new OutputStreamWriter(st)
        ) {
            mapper.writeValue(sw, datos);
        }
    }

    /**
     * Recupera de archivo el objeto serializado.
     * @return El objeto deserializado.
     * @throws IOException Si hay algún problema con el archivo de almacenamiento.
     */
    @Override
    public EstudianteJson[] load() throws IOException {
        try (
            InputStream st = Files.newInputStream(archivo);
            InputStreamReader sr = new InputStreamReader(st);
        ) {
            return mapper.readValue(sr, EstudianteJson[].class);
        }
    }
}
