/**
 * SQLColumn es la clase que simula ser una columna de una
 * tabla de la base de datos.
 * 
 * @author Mauricio Cruz Portilla
 * @version 1.0
 * @since 2019/02/22
 */

package alumnos.engine;

public class SQLColumn<K, V> {
    private final K key;
    private final V value;

    /**
     * Simula una columna de una tabla SQL
     * @param name nombre de la columna
     * @param value valor de la columna
     */
    public SQLColumn(K name, V value) {
        key = name;
        this.value = value;
    }
    /**
     * Obtiene el nombre de la columna
     * @return el nombre de la columna
     */
    public K getName() {
        return key;
    }

    /**
     * Obtiene el valor de la columna
     * @return el valor de la columna
     */
    public V getValue() {
        return value;
    }
}
