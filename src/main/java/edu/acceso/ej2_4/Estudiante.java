package edu.acceso.ej2_4;

import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.stream.Collectors;

/**
 * Modela un estudiante según el enunciado del ejercicio.
 */
public class Estudiante implements Serializable {

    /**
     * Formato de las fechas de nacimiento.
     */
    protected static SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");

    /**
     * Identificador único para cada estudiantes. Se obtiene calculando el MD5
     * de la concatenación de sus atributos.
     */
    private String id;
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
     * Obtiene el MD5 de una cadena de texto.
     * @param data La cadena de texto de la que se quiere obtener el resumen criptográfico.
     * @return El resumen en formato hexadecimal.
     */
    public static String md5(String data) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("md5");
            md5.update(data.getBytes());
            byte[] digest = md5.digest();
            Byte[] digestB = new Byte[digest.length];
            Arrays.setAll(digestB, i -> digest[i]);
            return Arrays.stream(digestB).map(b -> String.format("%02x", b)).collect(Collectors.joining(""));
        }
        catch(NoSuchAlgorithmException err) {
            assert false: "El algoritmo MD5 no existe";
            return null;
        }
    }

    /**
     * Constructor de la clase.
     */
    public Estudiante() {
        super();
    }

    /**
     * Carga todos los datos de un estudiante.
     * @param nombre Nombre del estudiante.
     * @param apellidos Apellidos del estudiante.
     * @param nacimiento Fecha de nacimiento.
     * @param estudios Estudios previos.
     */
    public void cargarDatos(String nombre, String apellidos, Date nacimiento, Estudios estudios) {
        setNombre(nombre);
        setApellidos(apellidos);
        setNacimiento(nacimiento);
        setEstudios(estudios);
    }

    /**
     * Devuelve el identificador único del estudiante.
     * @return El identificador en forma de cadena.
     */
    public String getId() {
        if (id == null) {
            String datos = String.format("%s%s%s%s", getNombre(), getApellidos(), df.format(getNacimiento()), getEstudios());
            id = md5(datos);
        }
        return id.toString();
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
        Estudiante otro = (Estudiante) obj;
        return getId().equals(otro.getId());
    }
}
