/**
 * Engine es la clase que lleva a cabo el control en la
 * base de datos.
 * 
 * @author Mauricio Cruz Portilla
 * @version 1.0
 * @since 2019/02/22
 */
package alumnos.engine;

public class Engine {

    /**
     * Crea la base de datos AlumnosDB si no existe.
     */
    public static void createDatabase(){
        SQL.executeUpdate(
            "CREATE DATABASE IF NOT EXISTS AlumnosDB;" +
            "CREATE TABLE IF NOT EXISTS AlumnosDB.alumno (" +
                "matricula varchar(9) PRIMARY KEY NOT NULL," +
                "nombre varchar(20) NOT NULL," +
                "paterno varchar(20) NOT NULL," +
                "materno varchar(20) NOT NULL" +
            ");" +
            "CREATE TABLE IF NOT EXISTS AlumnosDB.materia (" +
                "idmateria int PRIMARY KEY NOT NULL AUTO_INCREMENT," +
                "nrc int NOT NULL," +
                "nombre varchar(50) NOT NULL," +
                "creditos int NOT NULL," +
                "horasTeoricas int NOT NULL," +
                "horasPracticas int NOT NULL," +
                "profesor varchar(35) NOT NULL" +
            ");" +
            "CREATE TABLE IF NOT EXISTS AlumnosDB.horarioMateria (" +
                "idhorario int PRIMARY KEY NOT NULL AUTO_INCREMENT," +
                "idmateria int NOT NULL," +
                "FOREIGN KEY (idmateria) REFERENCES AlumnosDB.materia(idmateria)," +
                "salon varchar(10) NOT NULL," +
                "horaInicio time NOT NULL," +
                "horaFin time NOT NULL," +
                "dia varchar(10) NOT NULL" +
            ");" +
            "CREATE TABLE IF NOT EXISTS AlumnosDB.horarioAlumno (" +
                "idhorarioAlumno int PRIMARY KEY NOT NULL AUTO_INCREMENT," +
                "idhorarioMateria int NOT NULL," +
                "FOREIGN KEY (idhorarioMateria) REFERENCES horarioMateria(idhorario)," +
                "matriculaAlumno varchar(9) NOT NULL," +
                "FOREIGN KEY (matriculaAlumno) REFERENCES alumno(matricula)" +
            ");", false
        );
    }
}
