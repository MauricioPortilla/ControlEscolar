/**
 * MateriaDAO es la clase que implementa la interfaz
 * IMateriaDAO y lleva a cabo el control de CRUD de las
 * materias en la base de datos.
 * 
 * @author Mauricio Cruz Portilla
 * @version 1.0
 * @since 2019/03/14
 */
package alumnos;

import alumnos.engine.SQL;
import alumnos.engine.SQLRow;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import javafx.collections.FXCollections;

public class MateriaDAO implements IMateriaDAO {
    private ObservableList<Materia> materias;

    /**
     * Crea un nuevo objeto MateriaDAO
     */
    public MateriaDAO(){
        materias = FXCollections.observableArrayList();
    }

    @Override
    public void loadMaterias(){
        materias.clear();
        SQL.executeQuery("SELECT * FROM materia;", (result) -> {
            for(SQLRow row : result){
                materias.add(new Materia(
                    (int)row.getColumnData("idmateria"),
                    (int)row.getColumnData("nrc"),
                    row.getColumnData("nombre").toString(),
                    (int)row.getColumnData("creditos"),
                    (int)row.getColumnData("horasTeoricas"),
                    (int)row.getColumnData("horasPracticas"),
                    row.getColumnData("profesor").toString()
                ));
            }
            return true;
        });
    }

    @Override
    public ObservableList<Materia> getMaterias(){
        return materias;
    }

    @Override
    public Materia insertMateria(Materia materia){
        ArrayList<Object> parameters = new ArrayList<>();
        parameters.add(materia.getNrc());
        parameters.add(materia.getNombre());
        parameters.add(materia.getCreditos());
        parameters.add(materia.getHorasTeoricas());
        parameters.add(materia.getHorasPracticas());
        parameters.add(materia.getProfesor());
        // Verificar si existe la materia
        if(materia.getId() == 0){
            // No existe la materia
            SQL.executeUpdate(
                "INSERT INTO materia VALUES (NULL, ?, ?, ?, ?, ?, ?)", 
                parameters
            );
        }
        return materia;
    }

    @Override
    public Materia updateMateria(Materia materia){
        ArrayList<Object> parameters = new ArrayList<>();
        parameters.add(materia.getNrc());
        parameters.add(materia.getNombre());
        parameters.add(materia.getCreditos());
        parameters.add(materia.getHorasTeoricas());
        parameters.add(materia.getHorasPracticas());
        parameters.add(materia.getProfesor());
        parameters.add(materia.getId());
        // Verificar si existe la materia
        SQL.executeQuery(
            "SELECT idmateria FROM materia WHERE idmateria = ?",
            new ArrayList<Object>(){{
                add(materia.getId());
            }}, (result) -> {
                // Existe la materia
                SQL.executeUpdate(
                    "UPDATE materia SET nrc = ?, nombre = ?, creditos = ?, " +
                    "horasTeoricas = ?, horasPracticas = ?, profesor = ? WHERE idmateria = ?", 
                    parameters
                );
                return true;
            }
        );
        return materia;
    }

    @Override
    public Materia deleteMateria(Materia materia){
        SQL.executeUpdate(
            "DELETE FROM materia WHERE idmateria = ?", 
            new ArrayList<Object>(){{
                add(materia.getId());
            }}
        );
        materias.remove(materia);
        return materia;
    }
}
