package dev.lzzgabriel.workshop.model.services;

import java.util.List;

import dev.lzzgabriel.workshop.model.dao.DaoFactory;
import dev.lzzgabriel.workshop.model.dao.DepartmentDAO;
import dev.lzzgabriel.workshop.model.entities.Department;

public class DepartmentService {
  
  private DepartmentDAO dao = DaoFactory.createDepartmentDAO();
  
  public List<Department> findAll() {
    return dao.findAll();
  }
  
  public void saveOrUpdate(Department obj) {
    if (obj.getId() == null) {
      dao.insert(obj);
    } else {
      dao.update(obj);
    }
  }
  
  public void remove(Department obj) {
    dao.deleteByID(obj.getId());
  }

}
