package edu.acceso.ej2_4.backend.json;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import edu.acceso.ej2_4.Estudiante;

/**
 * Implementa las particularidades de la clase estudiante para su almacenamiento en JSON
 */
public class EstudianteJson extends Estudiante {

    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd/MM/yyyy")
    private Date nacimiento;

    @JsonIgnore
    private String id;
}