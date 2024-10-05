package edu.acceso.ej2_4.backend;

import java.nio.file.Path;

import edu.acceso.ej2_4.Estudiante;
import edu.acceso.ej2_4.backend.csv.BackendCsv;
import edu.acceso.ej2_4.backend.csv.EstudianteCsv;
import edu.acceso.ej2_4.backend.json.BackendJson;
import edu.acceso.ej2_4.backend.json.EstudianteJson;
import edu.acceso.ej2_4.backend.object.BackendObject;
import edu.acceso.ej2_4.backend.object.EstudianteObject;

public class Factory {

    private String formato;

    public Factory(String formato) {
        setFormato(formato);
        switch(this.formato) {
            case "object":
            case "csv":
            case "json":
                break;
            default:
                throw new IllegalArgumentException("Formato no soportando: " + this.formato);
        }
    } 

    private void setFormato(String formato) {
        this.formato = formato.toLowerCase();
    }

    public String getFormato() {
        return formato;
    }

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
