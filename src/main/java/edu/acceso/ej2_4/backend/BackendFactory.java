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
public class BackendFactory {

    public static enum Formato {
        CSV(BackendCsv.class, EstudianteCsv.class),
        JSON(BackendJson.class, EstudianteJson.class),
        object(BackendObject.class, EstudianteObject.class),
        XML(null, null);  // Aún no está implementado

        private Class<Backend> tipoBackend;
        private Class<Estudiante> tipoEstudiante;

        @SuppressWarnings("unchecked")
        Formato(Class backend, Class estudiante) {
            tipoBackend = backend;
            tipoEstudiante = estudiante;
        }

        public Class<Backend> getTipoBackend() {
            return tipoBackend;
        }

        public Class<Estudiante> getTipoEstudiante() {
            return tipoEstudiante;
        }

        /**
         * Devuelve el valor correspondiente a un formato.
         * 
         * <pre>
         *      Formato.getFormato("csv"); // Formato.CSV
         * </pre>
         * @param formato El nombre del formato.
         * @return El formato correspondiente o null, si no existe.
         */
        public static Formato getFormato(String formato) {
            return Arrays.stream(Formato.values())
                            .filter(f -> f.name().toLowerCase().equals(formato.toLowerCase()))
                            .findFirst().orElse(null);
        }

        public boolean noImplementado() {
            return tipoBackend == null || tipoEstudiante == null;
        }
    };

    /**
     * Lista de backends disponibles.
     */
    public final static String[] formatos = new String[] {"CSV", "JSON", "object", "XML"};

    private String formato;

    /**
     * Constructor de la clase.
     * @param formato Formato de almacenamiento (csv, json, etc)
     */
    public BackendFactory(String formato) {
        setFormato(formato);
    } 

    /**
     * Define el formato de almacenamiento.
     * @param formato El formato.
     */
    private void setFormato(String formato) {
        this.formato = formato.toLowerCase();
        switch(this.formato) {
            case "object":
            case "csv":
            case "json":
                break;
            default:
                if(Arrays.stream(formatos).anyMatch(f -> f.toLowerCase().equals(this.formato))) {
                    throw new UnsupportedOperationException(formato + ": Formato no soportado.");
                }
                else {
                    throw new IllegalArgumentException(formato + ": Formato desconocido.");
                }
        }
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
