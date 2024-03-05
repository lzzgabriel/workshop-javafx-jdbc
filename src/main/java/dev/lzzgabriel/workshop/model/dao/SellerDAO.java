package dev.lzzgabriel.workshop.model.dao;

import java.util.List;

import dev.lzzgabriel.workshop.model.entities.Department;
import dev.lzzgabriel.workshop.model.entities.Seller;

public interface SellerDAO {
  
  void insert(Seller obj);
  void update(Seller obj);
  void deleteById(Integer id);
  Seller findById(Integer id);
  List<Seller> findAll();
  List<Seller> findByDepartment(Department department);

}
