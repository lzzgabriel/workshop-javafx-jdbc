package dev.lzzgabriel.workshop.model.services;

import java.util.ArrayList;
import java.util.List;

import dev.lzzgabriel.workshop.model.entities.Department;

public class DepartmentService {
  
  public List<Department> findAll() {
    var list = new ArrayList<Department>();
    
    //mock'em up
    list.add(new Department(1, "Books"));
    list.add(new Department(2, "Computers"));
    list.add(new Department(3, "Electronics"));
    
    return list;
  }

}
