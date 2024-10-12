package edu.acceso.ej2_4.backend;

import java.lang.reflect.InvocationTargetException;
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

        private Class<? extends Backend> tipoBackend;
        private Class<? extends Estudiante> tipoEstudiante;

        Formato(Class<? extends Backend> backend, Class<? extends Estudiante> estudiante) {
            tipoBackend = backend;
            tipoEstudiante = estudiante;
        }

        public Class<? extends Backend> getTipoBackend() {
            return tipoBackend;
        }

        public Class<? extends Estudiante> getTipoEstudiante() {
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

    private Formato formato;

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
        this.formato = Formato.getFormato(formato);
        if(this.formato == null) {
            throw new IllegalArgumentException(formato + ": Formato desconocido.");
        }
        else if(this.formato.noImplementado())  {
            throw new UnsupportedOperationException(formato + ": Formato no soportado.");
        }
    }

    /**
     * Devuelve el formato de almacenamiento.
     * @return El formato.
     */
    public Formato getFormato() {
        return formato;
    }

    /**
     * Crea un backend que se usará para almacenar la información.
     * @param archivo La ruta del archivo.
     * @return  Un objeto para almacenar en el formato adecuado.
     */
    public Backend crearBackend(Path archivo) {
        try {
            return formato.getTipoBackend().getDeclaredConstructor(Path.class).newInstance(archivo);
        }
        catch(NoSuchMethodException|SecurityException|InstantiationException|IllegalAccessException|InvocationTargetException err) {
            return null;
        }
    } 

    /**
     * Crea un estudiante apto para ser almacenado en el formato escogido.
     * @return Un objeto estudiante apropiado.
     */
    public Estudiante crearEstudiante() {
        try {
            return formato.getTipoEstudiante().getDeclaredConstructor().newInstance();
        }
        catch(NoSuchMethodException|SecurityException|InstantiationException|IllegalAccessException|InvocationTargetException err) {
            return null;
        }
    }
}
