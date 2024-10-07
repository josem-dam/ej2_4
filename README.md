# Ejercicio 2.4
Cree una pequeña aplicación que:

a. Pida datos de estudiante:

   * Número de matrícula (es un identificador).
   * Nombre.
   * Apellidos.
   * Fecha de nacimiento.
   * Estudios previos (Primaria, Secundaria, Bachillerato, FP, Universidad).

b. Guarde la lista de estudiantes en distintos formatos (debe elegir el usuario cuál):

   + Como una serialización de objetos.
   + Como CSV.
   + Como JSON.

c. Recupere el archivo y compruebe que la lista recuperada es igual a la lista original.
   Para esto último, implemente un método equals que permita comprobar si dos objetos
   estudiante refieren el mismo estudiante.

d. Debe permitir la selección de la interfaz de usuario mediante argumento en la
   línea de órdenes. Implemente al menos una interfaz de texto.