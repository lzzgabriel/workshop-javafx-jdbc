package dev.lzzgabriel.workshop.model.dao;

import dev.lzzgabriel.workshop.db.DataBase;
import dev.lzzgabriel.workshop.model.dao.impl.DepartmentDAOJDBC;
import dev.lzzgabriel.workshop.model.dao.impl.SellerDAOJDBC;

public class DaoFactory {
  
  public static SellerDAO createSellerDAO() {
    return new SellerDAOJDBC(DataBase.getConnection());
  }
  
  public static DepartmentDAO createDepartmentDAO() {
    return new DepartmentDAOJDBC(DataBase.getConnection());
  }

}
