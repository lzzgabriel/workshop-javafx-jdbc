package dev.lzzgabriel.workshop.model.services;

import java.util.List;

import dev.lzzgabriel.workshop.model.dao.DaoFactory;
import dev.lzzgabriel.workshop.model.dao.SellerDAO;
import dev.lzzgabriel.workshop.model.entities.Seller;

public class SellerService {
  
  private SellerDAO dao = DaoFactory.createSellerDAO();
  
  public List<Seller> findAll() {
    return dao.findAll();
  }
  
  public void saveOrUpdate(Seller obj) {
    if (obj.getId() == null) {
      dao.insert(obj);
    } else {
      dao.update(obj);
    }
  }
  
  public void remove(Seller obj) {
    dao.deleteById(obj.getId());
  }

}
