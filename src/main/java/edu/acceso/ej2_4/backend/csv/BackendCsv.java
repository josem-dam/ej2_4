package edu.acceso.ej2_4.backend.csv;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.ParseException;
import java.util.Arrays;
import java.util.stream.StreamSupport;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;

import edu.acceso.ej2_4.backend.Backend;

/**
 * Backend para almacenar estudiantes en un archivo CSV.
 */
public class BackendCsv implements Backend {

    private Path ruta;
    private CSVFormat formatoBase;

    /**
     * Constructor de la clase.
     * @param ruta Archivo de almacenamiento.
     */
    public BackendCsv(Path ruta) {
        this.ruta = ruta;  
        formatoBase = CSVFormat.Builder.create(CSVFormat.DEFAULT)
                                        .setHeader(EstudianteCsv.columnas)
                                        .build();
    }

    @Override
    public EstudianteCsv[] read() throws IOException {
        CSVFormat formato = CSVFormat.Builder.create(formatoBase)
                            .setSkipHeaderRecord(true)
                            .build();

        try (
            InputStream st = Files.newInputStream(ruta);
            InputStreamReader sr = new InputStreamReader(st);
            CSVParser parser = new CSVParser(sr, formato);
        ) {
            return StreamSupport.stream(parser.spliterator(), false).map(registro -> {
                String[] campos = StreamSupport.stream(registro.spliterator(), false).toArray(String[]::new);
                try {
                    EstudianteCsv estudiante = new EstudianteCsv();
                    estudiante.cargarCampos(campos);
                    return estudiante;
                }
                catch(ParseException err) {
                    err.printStackTrace();
                    return null;
                }
            }).toArray(EstudianteCsv[]::new);
        }
    }

    @Override
    public void save(Object[] datos) throws IOException {
        try (
            OutputStream st = Files.newOutputStream(ruta);
            OutputStreamWriter sw = new OutputStreamWriter(st); 
            CSVPrinter printer = new CSVPrinter(sw, formatoBase);
        ) {
            printer.printRecords(Arrays.stream(datos).map(r -> ((RegistroCsv) r).toCsv()));
        }
    }
}
