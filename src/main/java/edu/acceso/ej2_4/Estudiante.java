package edu.acceso.ej2_4;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

/**
 * Modela un estudiante según el enunciado del ejercicio.
 */
public class Estudiante implements Serializable {

    /**
     * Formato de las fechas de nacimiento.
     */
    protected static SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");

    /**
     * Número de matrícula del próximo estudiante matriculado.
     */
    private static long siguienteMatricula = 1L;

    /**
     * Número de matrícula. Es único para cada estudiante.
     */
    private long matricula  = -1;
    /**
     * Nombre del estudiante.
     */
    private String nombre;
    /**
     * Apellidos del estudiante.
     */
    private String apellidos;
    /**
     * Fecha de nacimiento.
     */
    private Date nacimiento;
    /**
     * Estudios previos del estudiante.
     */
    private Estudios estudios;

    /**
     * Constructor de la clase.
     */
    public Estudiante() {
        super();
    }

    /**
     * Carga todos los datos de un nuevo estudiante (sin número de matrícula aún).
     * @param nombre Nombre del estudiante.
     * @param apellidos Apellidos del estudiante.
     * @param nacimiento Fecha de nacimiento.
     * @param estudios Estudios previos.
     * @return El propio objeto.
     */
    public Estudiante cargarDatos(String nombre, String apellidos, Date nacimiento, Estudios estudios) {
        setMatricula(siguienteMatricula++);
        setNombre(nombre);
        setApellidos(apellidos);
        setNacimiento(nacimiento);
        setEstudios(estudios);
        return this;
    }

    /**
     * Carga todos los datos de un estudiante ya existente (tiene asignado un número de matricula).
     * Si el nombre del estudiante es nulo, se entiende que lo que se quiere es fijar el número de la
     * siguiente matrícula, no rellenar los datos de un estudiante.
     * @param matricula Número de matrícula.
     * @param nombre Nombre del estudiante.
     * @param apellidos Apellidos del estudiante.
     * @param nacimiento Fecha de nacimiento.
     * @param estudios Estudios previos.
     * @return El propio objeto.
     */
    public Estudiante cargarDatos(long matricula, String nombre, String apellidos, Date nacimiento, Estudios estudios) {
        if(nombre != null) {
            setMatricula(matricula);
            setNombre(nombre);
            setApellidos(apellidos);
            setNacimiento(nacimiento);
            setEstudios(estudios);
            return this;
        }
        else {
            siguienteMatricula = matricula;
            setMatricula(matricula);
            return null;
        }
    }

    /**
     * Devuelve el número del siguiente alumno que se matricule.
     * @return El número de matrícula del próximo estudiante.
     */
    public static long getSiguienteMatricula() {
        return siguienteMatricula;
    }

    /**
     * Getter de matricula
     * @return El número de matricula del estudiante.
     */
    public long getMatricula() {
        return matricula;
    }
    
    /**
     * Setter de matricula. Sólo fija el valor, si matrícula no está inicializado aún.
     * @param matricula Número de matricula del alumno.
     */
    public void setMatricula(long matricula) {
        if(this.matricula == -1) this.matricula = matricula;
    }

    /**
     * Getter de nombre.
     * @return El nombre.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Setter de nombre.
     * @param nombre El nombre del estudiante.
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Getter de apellidos.
     * @return pellidos del estudiante.
     */
    public String getApellidos() {
        return apellidos;
    }

    /**
     * Setter de apellidos
     * @param apellidos Los apellidos del estudiante.
     */
    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    /**
     * Setter de los estudios previos cursados por el estudiante.
     * @param estudios Los estudios cursados.
     */
    public void setEstudios(Estudios estudios) {
        this.estudios = estudios;
    }

    /**
     * Getter de estudios.
     * @return Los estudios previos
     */
    public Estudios getEstudios() {
        return estudios;
    }

    /**
     * Setter de nacimiento
     * @param nacimiento La fecha de nacimiento del estudiante.
     */
    public void setNacimiento(Date nacimiento) {
        this.nacimiento = nacimiento;
    }

    /**
     * Getter de nacimiento
     * @return La fecha de nacimiento del estudiante.
     */
    public Date getNacimiento() {
        return nacimiento;
    }

    /**
     * Calcula la edad a partir de su fecha de nacimiento.
     * @return La edad del estudiante.
     */
    public int edad() {
        Calendar hoy = Calendar.getInstance();
        Calendar nacimiento = Calendar.getInstance();

        hoy.setTime(new Date());
        nacimiento.setTime(this.nacimiento);

        int edad = hoy.get(Calendar.YEAR) - nacimiento.get(Calendar.YEAR);
        if(hoy.get(Calendar.MONTH) < nacimiento.get(Calendar.MONTH) || 
           (hoy.get(Calendar.MONTH) == nacimiento.get(Calendar.MONTH) &&
            hoy.get(Calendar.DAY_OF_MONTH) < nacimiento.get(Calendar.DAY_OF_MONTH))) edad--;

        return edad;
    }

    @Override
    public String toString() {
        return String.format("%s, %s (%d años)", apellidos, nombre, edad());
    }

    @Override
    public boolean equals(Object obj) {
        return hashCode() == Objects.hashCode(obj);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nombre, apellidos, nacimiento, estudios);
    }
}
