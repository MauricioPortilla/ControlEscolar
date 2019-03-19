/**
 * IHorarioMateriaDAO es la interfaz que mantiene el control de CRUD
 * de los horarios de las materias en la base de datos
 * 
 * @author Mauricio Cruz Portilla
 * @version 1.0
 * @since 2019/03/16
 */
package alumnos;

import javafx.collections.ObservableList;

public interface IHorarioMateriaDAO {
    /**
     * Carga los horarios de las materias desde la base de datos
     */
    public void loadHorariosMaterias();

    /**
     * Retorna todos los horarios de las materias recuperados de 
     * la base de datos.
     * 
     * @return lista de horarios de las materias
     */
    public ObservableList<HorarioMateria> getHorariosMaterias();

    /**
     * Inserta un horario de una materia en la base de datos.
     * 
     * @param horarioMateria horario de una materia a ingresar
     */
    public HorarioMateria insertHorarioMateria(HorarioMateria horarioMateria);

    /**
     * Actualiza un horario de una materia existente en la base de datos.
     * 
     * @param horarioMateria horario de una materia a actualizar
     */
    public HorarioMateria updateHorarioMateria(HorarioMateria horarioMateria);

    /**
     * Elimina un horario de una materia existente en la base de datos.
     * 
     * @param horarioMateria horario de una materia a eliminar
     */
    public HorarioMateria deleteHorarioMateria(HorarioMateria horarioMateria);
}
