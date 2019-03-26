/**
 * IHorarioAlumnoDAO es la interfaz que mantiene el control de CRUD
 * de los horarios de los alumnos en la base de datos
 * 
 * @author Mauricio Cruz Portilla
 * @version 1.0
 * @since 2019/03/21
 */
package alumnos;

import javafx.collections.ObservableList;

public interface IHorarioAlumnoDAO {
    /**
     * Carga los horarios de un alumno desde la base de datos
     * 
     * @param alumno alumno
     */
    public void loadHorariosAlumno(Alumno alumno);

    /**
     * Retorna todos los horarios del alumno recuperados de la base de datos.
     * 
     * @return lista de horarios del alumno
     */
    public ObservableList<HorarioAlumno> getHorariosAlumno();

    /**
     * Retorna las materias asociadas con cada horario del alumno.
     * 
     * @return lista de materias del horario
     */
    public ObservableList<HorarioMateria> getHorariosAlumnoMateria();

    /**
     * Inserta un horario de un alumno en la base de datos.
     * 
     * @param horarioAlumno horario de un alumno a ingresar
     */
    public HorarioAlumno insertHorarioAlumno(HorarioAlumno horarioAlumno);

    /**
     * Elimina un horario de un alumno existente en la base de datos.
     * 
     * @param horarioAlumno horario de un alumno a eliminar
     */
    public HorarioAlumno deleteHorarioAlumno(HorarioAlumno horarioAlumno);
}
