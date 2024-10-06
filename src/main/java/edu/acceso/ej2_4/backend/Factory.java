package edu.acceso.ej2_4.backend;

import java.nio.file.Path;
import java.util.Arrays;

import edu.acceso.ej2_4.Estudiante;
import edu.acceso.ej2_4.backend.csv.BackendCsv;
import edu.acceso.ej2_4.backend.csv.EstudianteCsv;
import edu.acceso.ej2_4.backend.json.BackendJson;
import edu.acceso.ej2_4.backend.json.EstudianteJson;
import edu.acceso.ej2_4.backend.object.BackendObject;
import edu.acceso.ej2_4.backend.object.EstudianteObject;

/**
 * Implementa el patrón Factory para seleccionar las clases apropiadas
 * según sea el almacenamiento.
 */
public class Factory {

    /**
     * Lista de backends disponibles.
     */
    public final static String[] formatos = new String[] {"CSV", "JSON", "object", "XML"};

    private String formato;

    /**
     * Constructor de la clase.
     * @param formato Formato de almacenamiento (csv, json, etc)
     */
    public Factory(String formato) {
        setFormato(formato);
        switch(this.formato) {
            case "object":
            case "csv":
            case "json":
                break;
            default:
                if(Arrays.stream(formatos).map(f -> f.toLowerCase()).anyMatch(f -> f.equals(this.formato))) {
                    throw new UnsupportedOperationException(formato + ": Formato no soportado.");
                }
                else {
                    throw new IllegalArgumentException(formato + ": Formato desconocido.");
                }
        }
    } 

    /**
     * Define el formato de almacenamiento.
     * @param formato El formato.
     */
    private void setFormato(String formato) {
        this.formato = formato.toLowerCase();
    }

    /**
     * Devuelve el formato de almacenamiento.
     * @return El formato.
     */
    public String getFormato() {
        return formato;
    }

    /**
     * Crea un backend que se usará para almacenar la información.
     * @param archivo La ruta del archivo.
     * @return  Un objeto para almacenar en el formato adecuado.
     */
    public Backend crearBackend(Path archivo) {
        switch(formato) {
            case "json":
                return new BackendJson(archivo);
            case "object":
                return new BackendObject(archivo);
            case "csv":
                return new BackendCsv(archivo);
            default:
                assert false: "Backend imposible";
                return null;
        }
    } 

    /**
     * Crea un estudiante apto para ser almacenado en el formato escogido.
     * @return Un objeto estudiante apropiado.
     */
    public Estudiante crearEstudiante() {
        switch(formato) {
            case "json":
                return new EstudianteJson();
            case "object":
                return new EstudianteObject();
            case "csv":
                return new EstudianteCsv();
            default:
                assert false: "Backend imposible";
                return null;
        }
    }
}
