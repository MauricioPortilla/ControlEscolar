/**
 * IAlumnoDAO es la interfaz que mantiene el control de CRUD
 * de los alumnos en la base de datos
 * 
 * @author Mauricio Cruz Portilla
 * @version 1.0
 * @since 2019/03/11
 */
package alumnos;

import javafx.collections.ObservableList;

public interface IAlumnoDAO {
    /**
     * Carga los alumnos desde la base de datos
     */
    public void loadAlumnos();

    /**
     * Retorna todos los alumnos recuperados de la base de datos.
     * 
     * @return lista de alumnos
     */
    public ObservableList<Alumno> getAlumnos();

    /**
     * Retorna un alumno de acuerdo a su matricula.
     * 
     * @param matricula matricula del alumno
     * @return alumno
     */
    public Alumno getAlumno(String matricula);

    /**
     * Inserta un alumno en la base de datos.
     * 
     * @param alumno alumno a ingresar
     */
    public Alumno insertAlumno(Alumno alumno);

    /**
     * Actualiza un alumno existente en la base de datos.
     * 
     * @param alumno alumno a actualizar
     */
    public Alumno updateAlumno(Alumno alumno);

    /**
     * Elimina un alumno existente en la base de datos.
     * 
     * @param alumno alumno a eliminar
     */
    public Alumno deleteAlumno(Alumno alumno);
}
