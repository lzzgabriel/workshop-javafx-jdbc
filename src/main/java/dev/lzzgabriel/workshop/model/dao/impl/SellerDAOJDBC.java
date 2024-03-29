package dev.lzzgabriel.workshop.model.dao.impl;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dev.lzzgabriel.workshop.db.DataBase;
import dev.lzzgabriel.workshop.db.DbException;
import dev.lzzgabriel.workshop.model.dao.SellerDAO;
import dev.lzzgabriel.workshop.model.entities.Department;
import dev.lzzgabriel.workshop.model.entities.Seller;

public class SellerDAOJDBC implements SellerDAO {

  private Connection conn;

  public SellerDAOJDBC(Connection conn) {
    this.conn = conn;
  }

  @Override
  public void insert(Seller obj) {
    PreparedStatement st = null;
    try {
      st = conn.prepareStatement(
          "INSERT INTO seller " + "(Name, Email, BirthDate, BaseSalary, DepartmentId) " + "VALUES " + "(?, ?, ?, ?, ?)",
          Statement.RETURN_GENERATED_KEYS);

      st.setString(1, obj.getName());
      st.setString(2, obj.getEmail());
      st.setDate(3, Date.valueOf(obj.getBirthDate()));
      st.setDouble(4, obj.getBaseSalary());
      st.setInt(5, obj.getDepartment().getId());

      int rowsAffected = st.executeUpdate();

      if (rowsAffected > 0) {
        ResultSet rs = st.getGeneratedKeys();
        if (rs.next()) {
          int id = rs.getInt(1);
          obj.setId(id);
        }
        DataBase.closeResultSet(rs);
      } else {
        throw new DbException("Unexpected error! No rows affected!");
      }
    } catch (SQLException e) {
      throw new DbException(e.getMessage());
    } finally {
      DataBase.closeStatement(st);
    }
  }

  @Override
  public void update(Seller obj) {
    PreparedStatement st = null;
    try {
      st = conn.prepareStatement("UPDATE seller "
          + "SET Name = ?, Email = ?, BirthDate = ?, BaseSalary = ?, DepartmentId = ? " + "WHERE Id = ?");

      st.setString(1, obj.getName());
      st.setString(2, obj.getEmail());
      st.setDate(3, Date.valueOf(obj.getBirthDate()));
      st.setDouble(4, obj.getBaseSalary());
      st.setInt(5, obj.getDepartment().getId());
      st.setInt(6, obj.getId());

      st.executeUpdate();
    } catch (SQLException e) {
      throw new DbException(e.getMessage());
    } finally {
      DataBase.closeStatement(st);
    }
  }

  @Override
  public void deleteById(Integer id) {
    PreparedStatement st = null;
    try {
      st = conn.prepareStatement("DELETE FROM seller WHERE Id = ?");

      st.setInt(1, id);

      st.executeUpdate();
    } catch (SQLException e) {
      throw new DbException(e.getMessage());
    } finally {
      DataBase.closeStatement(st);
    }
  }

  @Override
  public Seller findById(Integer id) {
    PreparedStatement st = null;
    ResultSet rs = null;
    try {
      st = conn.prepareStatement("SELECT seller.*,department.Name as DepName " + "FROM seller INNER JOIN department "
          + "ON seller.DepartmentId = department.Id " + "WHERE seller.Id = ?");

      st.setInt(1, id);
      rs = st.executeQuery();
      if (rs.next()) {
        Department dep = instantiateDepartment(rs);
        Seller obj = instantiateSeller(rs, dep);
        return obj;
      }
      return null;
    } catch (SQLException e) {
      throw new DbException(e.getMessage());
    } finally {
      DataBase.closeStatement(st);
      DataBase.closeResultSet(rs);
    }
  }

  private Seller instantiateSeller(ResultSet rs, Department dep) throws SQLException {
    Seller obj = new Seller();
    obj.setId(rs.getInt("Id"));
    obj.setName(rs.getString("Name"));
    obj.setEmail(rs.getString("Email"));
    obj.setBaseSalary(rs.getDouble("BaseSalary"));
    obj.setBirthDate(rs.getDate("BirthDate").toLocalDate());
    obj.setDepartment(dep);
    return obj;
  }

  private Department instantiateDepartment(ResultSet rs) throws SQLException {
    Department dep = new Department();
    dep.setId(rs.getInt("DepartmentId"));
    dep.setName(rs.getString("DepName"));
    return dep;
  }

  @Override
  public List<Seller> findAll() {
    PreparedStatement st = null;
    ResultSet rs = null;
    try {
      st = conn.prepareStatement("SELECT seller.*,department.Name as DepName " + "FROM seller INNER JOIN department "
          + "ON seller.DepartmentId = department.Id " + "ORDER BY Name");

      rs = st.executeQuery();

      List<Seller> list = new ArrayList<>();
      Map<Integer, Department> map = new HashMap<>();

      while (rs.next()) {

        Department dep = map.get(rs.getInt("DepartmentId"));

        if (dep == null) {
          dep = instantiateDepartment(rs);
          map.put(rs.getInt("DepartmentId"), dep);
        }

        Seller obj = instantiateSeller(rs, dep);
        list.add(obj);
      }
      return list;
    } catch (SQLException e) {
      throw new DbException(e.getMessage());
    } finally {
      DataBase.closeStatement(st);
      DataBase.closeResultSet(rs);
    }
  }

  @Override
  public List<Seller> findByDepartment(Department department) {
    PreparedStatement st = null;
    ResultSet rs = null;
    try {
      st = conn.prepareStatement("SELECT seller.*,department.Name as DepName " + "FROM seller INNER JOIN department "
          + "ON seller.DepartmentId = department.Id " + "WHERE DepartmentId = ? " + "ORDER BY Name");

      st.setInt(1, department.getId());

      rs = st.executeQuery();

      List<Seller> list = new ArrayList<>();
      Map<Integer, Department> map = new HashMap<>();

      while (rs.next()) {

        Department dep = map.get(rs.getInt("DepartmentId"));

        if (dep == null) {
          dep = instantiateDepartment(rs);
          map.put(rs.getInt("DepartmentId"), dep);
        }

        Seller obj = instantiateSeller(rs, dep);
        list.add(obj);
      }
      return list;
    } catch (SQLException e) {
      throw new DbException(e.getMessage());
    } finally {
      DataBase.closeStatement(st);
      DataBase.closeResultSet(rs);
    }
  }

}
