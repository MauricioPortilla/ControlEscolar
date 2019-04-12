/**
 * SQL es la clase que permite llevar a cabo las consultas en la
 * base de datos.
 * 
 * @author Mauricio Cruz Portilla
 * @version 1.0
 * @since 2019/02/22
 */

package alumnos.engine;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.function.Predicate;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

public class SQL {
    public static Statement statement;

    /**
     * Realiza una consulta a la base de datos
     * y retorna una lista con cada resultado
     * encontrado.
     * 
     * @param sql consulta SQL
     * @param result funcion con parametro que posee
     * los resultados de la consulta
     * @return lista de resultados encontrados
     */
    public static ArrayList<SQLRow> executeQuery(String sql, Predicate<ArrayList<SQLRow>> result) {
        try {
            Connection conn = new Database().getConnection();
            statement = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ResultSet rs = statement.executeQuery(sql);
            ArrayList<SQLRow> list = retrieveSQLData(rs);
            if (result != null) {
                if (!list.isEmpty()) {
                    result.test(list);
                }
            }
            statement.close();
            conn.close();
            return list;
        } catch (SQLException ex) {
            System.out.println("executeQuery Error -> " + ex.getMessage());
        }
        return null;
    }

    /**
     * Realiza una consulta a la base de datos
     * y retorna una lista con cada resultado
     * encontrado.
     * @param sql consulta SQL
     * @param parameters lista de valores que va a tomar la consulta
     * @param result funcion con parametro que posee
     * los resultados de la consulta
     * @return lista de resultados encontrados
     */
    public static ArrayList<SQLRow> executeQuery(String sql, ArrayList<Object> parameters, Predicate<ArrayList<SQLRow>> result) {
        try {
            Connection conn = new Database().getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            for (int i = 0; i < parameters.size(); i++) {
                ps.setObject(i + 1, parameters.get(i));
            }
            ResultSet rs = ps.executeQuery();
            ArrayList<SQLRow> list = retrieveSQLData(rs);
            if (result != null) {
                if (!list.isEmpty()) {
                    result.test(list);
                }
            }
            ps.close();
            conn.close();
            return list;
        } catch (SQLException sqlException) {
            System.out.println("executeQuery Error -> " + sqlException.getMessage());
        }
        return null;
    }

    /**
     * Realiza una actualizacion en la base
     * de datos y retorna el numero de filas
     * afectadas si no se indica que se retorne
     * el id de la fila afectada.
     * @param sql consulta SQL
     * @param returnId si deberia retornar el id de la fila afectada
     * @return el numero de filas afectadas
     */
    public static int executeUpdate(String sql, boolean returnId) {
        int status = -1;
        try {
            Connection conn = new Database().getConnection();
            statement = conn.createStatement();
            if (returnId) {
                statement.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
                ResultSet rs = statement.getGeneratedKeys();
                if (rs.next())
                    status = rs.getInt(1);
            } else
                status = statement.executeUpdate(sql);
            statement.close();
            conn.close();
        } catch (SQLException ex) {
            System.out.println("executeUpdate Error -> " + ex.getMessage());
            System.out.println("Query -> " + sql);
        }
        return status;
    }

    /**
     * Realiza una actualizacion en la base
     * de datos de acuerdo a los parametros
     * ingresados
     * @param sql consulta SQL
     * @param parameters valores a usar en la consulta
     * @return el numero de filas afectadas
     */
    public static int executeUpdate(String sql, ArrayList<Object> parameters) {
        int status = -1;
        try {
            Connection conn = new Database().getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            for (int i = 0; i < parameters.size(); i++) {
                ps.setObject(i+1, parameters.get(i));
            }
            status = ps.executeUpdate();
            ps.close();
            conn.close();
        } catch (SQLException sqlException) {
            System.out.println("executeUpdate Error -> " + sqlException.getMessage() + " | Query: " + sql);
        }
        return status;
    }

    /**
     * Convierte un ResultSet en una lista con los datos
     * de cada fila.
     * @param rs resultado de la consulta
     * @return la lista con cada fila
     */
    private static ArrayList<SQLRow> retrieveSQLData(ResultSet rs) {
        ArrayList<SQLRow> list = new ArrayList<>();
        try {
            ResultSetMetaData metaData = rs.getMetaData();
            while (rs != null && rs.next()) {
                SQLRow row = new SQLRow();
                for(int i = 1; i <= metaData.getColumnCount(); i++){
                    row.setColumnData(metaData.getColumnName(i), rs.getObject(i));
                }
                list.add(row);
            }
        } catch (SQLException ex) {
            System.out.println("retrieveSQLData Error -> " + ex.getMessage());
        }
        return list;
    }
}
