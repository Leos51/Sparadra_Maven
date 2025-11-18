package fr.sparadrap.ecf.database.dao;

import fr.sparadrap.ecf.database.DatabaseConnection;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public abstract class DAO<T> {
   protected Connection connection = DatabaseConnection.getInstanceDB();

    public DAO() throws SQLException, IOException, ClassNotFoundException {
    }

    /**
     * Permet de creer un objet dans la bdd
     * @param obj
     * @return boolean
     */
    public abstract boolean create(T obj) throws SQLException;

    /**
     * Permet de mettre a jour les données d'une entrée dasn la base
     * @param obj
     */
    public abstract boolean update(T obj) throws SQLException;

    /**
     * Permet de supprimer un objet de la bdd
     * @param id
     */
    public abstract boolean deleteById(int id)  throws SQLException;

    /**
     * Permet de recuperer tout les objets
     * @return
     */
    public abstract List<T> findAll() throws SQLException;

    /**
     * Permet de recuperer un objet via son id
     * @param id
     * @return
     */
    public abstract T findById(int id);

 /**
  * Methode de cloture de la connexion
  */
    public abstract void closeConnection() throws SQLException;

}
