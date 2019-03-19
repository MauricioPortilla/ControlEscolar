/**
 * IMateriaDAO es la interfaz que mantiene el control de CRUD
 * de las materias en la base de datos
 * 
 * @author Mauricio Cruz Portilla
 * @version 1.0
 * @since 2019/03/14
 */
package alumnos;

import javafx.collections.ObservableList;

/**
 *
 * @author Mauricio CP
 */
public interface IMateriaDAO {
    /**
     * Carga las materias desde la base de datos
     */
    public void loadMaterias();

    /**
     * Retorna todos las materias recuperadas de la
     * base de datos.
     * @return lista de materias
     */
    public ObservableList<Materia> getMaterias();

    /**
     * Inserta una materia en la base de datos.
     * @param materia materia a ingresar
     */
    public Materia insertMateria(Materia materia);

    /**
     * Actualiza una materia existente en la base de datos.
     * @param materia materia a actualizar
     */
    public Materia updateMateria(Materia materia);

    /**
     * Elimina una materia existente en la base de datos.
     * @param materia materia a eliminar
     */
    public Materia deleteMateria(Materia materia);
}
