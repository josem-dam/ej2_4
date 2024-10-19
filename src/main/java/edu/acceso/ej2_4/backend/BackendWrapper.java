package edu.acceso.ej2_4.backend;

import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Clase envoltorio de Backend que permite manipular los datos antes de guardarlos
 * o justamente después de leerlos.
 */
public class BackendWrapper implements Backend {

    private Backend formato;

    public BackendWrapper(Backend formato) {
        this.formato = formato;
    }

    /**
     * Lee datos manipulándolos a continuación si la clase de objetos
     * leídos tiene definido el método estático "postread".
     * @param tipo Clase de la secuencia de objetos que se pretenden leer
     * @return Los datos leídos.
     */
    @SuppressWarnings("unchecked")
    @Override
    public <T> T[] read(Class<T> tipo) throws IOException {
        T[] datos = formato.read(tipo);

        /* 
         * Si el tipo "T" tiene definido el método "postread", manipulamos los datos con él.
         * Sin embargo, el método está definido en una superclase de "T" y, además, los datos
         * que transforma son T[], por lo que para encontrar el método hay que consultar en
         * toda la cadena de superclases. De ahí, el bucle.
         */
        Class<?> supertipo = tipo;

        do {
            try {
                Method metodo = supertipo.getDeclaredMethod("postread", Array.newInstance(supertipo, 0).getClass());
                datos = (T[]) metodo.invoke(null, (Object) datos);
                break;
            }
            catch(IllegalAccessException|InvocationTargetException err) {
                err.printStackTrace();
                break;
            }
            catch(NoSuchMethodException err) {
                supertipo = supertipo.getSuperclass();
            }
        } while(supertipo != null);

        return datos;
    }

    /**
     * Escribe los datos, manipulándolos antes si la clase de objetos que
     * se pretenden escribir, tiene definido el método estático "presave".
     * @param datos Los datos que se pretenden almacenar.
     */
    @SuppressWarnings("unchecked")
    @Override
    public <T> int save(T[] datos) throws IOException {
        if(datos.length > 0) {

            // Véase el comentario en el método "read" para la explicación a este bucle.
            // En este caso hay que buscar si está definido el método "presave".
            Class<?> tipo = datos[0].getClass();
            do {
                try {
                    Method metodo = tipo.getDeclaredMethod("presave", new Class<?>[] {Array.newInstance(tipo, 0).getClass()});
                    datos = (T[]) metodo.invoke(null, (Object) datos);
                    break;
                }
                catch(IllegalAccessException|InvocationTargetException err) {
                    err.printStackTrace();
                    break;
                }
                catch(NoSuchMethodException err) {
                    tipo = tipo.getSuperclass();
                }
            } while(tipo != null);
        }

        return formato.save(datos);
    }
}