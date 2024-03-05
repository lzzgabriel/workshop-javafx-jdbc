package dev.lzzgabriel.workshop.model.dao;

import java.util.List;

import dev.lzzgabriel.workshop.model.entities.Department;

public interface DepartmentDAO {
  
  void insert(Department obj);
  void update(Department obj);
  void deleteByID(Integer id);
  Department findById(Integer id);
  List<Department> findAll();

}
