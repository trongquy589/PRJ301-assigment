/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import entity.Department;
import entity.Employee;
import entity.accesscontrol.User;
import java.util.ArrayList;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author sonnt-local
 */
public class EmployeeDBContext extends DBContext<Employee> {

    public ArrayList<Employee> search(Integer id, String name, Boolean gender, String address, Date from, Date to, Integer did) {
        String sql = "SELECT e.eid,e.ename,e.gender,e.address,e.dob,d.did,dname FROM Employee e \n"
                + "	INNER JOIN Department d ON e.did = d.did\n"
                + "WHERE (1=1)";

        ArrayList<Employee> emps = new ArrayList<>();
        ArrayList<Object> paramValues = new ArrayList<>();
        if (id != null) {
            sql += " AND e.eid = ?";
            paramValues.add(id);
        }

        if (name != null) {
            sql += " AND e.ename LIKE '%'+?+'%'";
            paramValues.add(name);
        }

        if (gender != null) {
            sql += " AND e.gender = ?";
            paramValues.add(gender);
        }

        if (address != null) {
            sql += " AND e.[address] LIKE '%'+?+'%'";
            paramValues.add(address);
        }
        if (from != null) {
            sql += " AND e.dob >= ?";
            paramValues.add(from);
        }

        if (to != null) {
            sql += " AND e.dob <= ?";
            paramValues.add(to);
        }

        if (did != null) {
            sql += " AND d.did = ?";
            paramValues.add(did);
        }

        PreparedStatement stm = null;
        try {
            stm = connection.prepareStatement(sql);
            for (int i = 0; i < paramValues.size(); i++) {
                stm.setObject((i + 1), paramValues.get(i));
            }
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                Employee e = new Employee();
                e.setId(rs.getInt("eid"));
                e.setName(rs.getNString("ename"));
                e.setGender(rs.getBoolean("gender"));
                e.setDob(rs.getDate("dob"));
                e.setAddress(rs.getString("address"));

                Department d = new Department();
                d.setId(rs.getInt("did"));
                d.setName(rs.getString("dname"));
                e.setDept(d);

                emps.add(e);
            }
        } catch (SQLException ex) {
            Logger.getLogger(EmployeeDBContext.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                stm.close();
                connection.close();
            } catch (SQLException ex) {
                Logger.getLogger(EmployeeDBContext.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return emps;
    }

    @Override
    public void insert(Employee entity) {
        String sql_insert = "INSERT INTO [Employee]\n"
                + "				   ([ename]\n"
                + "				   ,[gender]\n"
                + "				   ,[address]\n"
                + "				   ,[dob]\n"
                + "				   ,[did]\n"
                + "				   ,[createdby])\n"
                + "			 VALUES\n"
                + "				   (?\n"
                + "				   ,?\n"
                + "				   ,?\n"
                + "				   ,?\n"
                + "				   ,?\n"
                + "				   ,?)";

        String sql_select = "SELECT @@IDENTITY as eid";

        PreparedStatement stm_insert = null;
        PreparedStatement stm_select = null;

        try {
            connection.setAutoCommit(false);
            stm_insert = connection.prepareStatement(sql_insert);
            stm_insert.setString(1, entity.getName());
            stm_insert.setBoolean(2, entity.isGender());
            stm_insert.setString(3, entity.getAddress());
            stm_insert.setDate(4, entity.getDob());
            stm_insert.setInt(5, entity.getDept().getId());
            stm_insert.setString(6, entity.getCreatedby().getUsername());
            stm_insert.executeUpdate();

            stm_select = connection.prepareStatement(sql_select);
            ResultSet rs = stm_select.executeQuery();
            if (rs.next()) {
                entity.setId(rs.getInt("eid"));
            }

            connection.commit();
        } catch (SQLException ex) {
            Logger.getLogger(EmployeeDBContext.class.getName()).log(Level.SEVERE, null, ex);
            try {
                connection.rollback();
            } catch (SQLException ex1) {
                Logger.getLogger(EmployeeDBContext.class.getName()).log(Level.SEVERE, null, ex1);
            }
        } finally {
            try {
                connection.setAutoCommit(true);
                connection.close();
            } catch (SQLException ex) {
                Logger.getLogger(EmployeeDBContext.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    @Override
    public void update(Employee entity) {
        String sql_update = "UPDATE [Employee]\n"
                + "   SET [ename] = ?\n"
                + "      ,[gender] = ?\n"
                + "      ,[address] = ?\n"
                + "      ,[dob] = ?\n"
                + "      ,[did] = ?\n"
                + "      ,[updatedby] = ?\n"
                + "      ,[updatedtime] = GETDATE()\n"
                + " WHERE eid = ?";

        PreparedStatement stm_update = null;

        try {

            stm_update = connection.prepareStatement(sql_update);
            stm_update.setString(1, entity.getName());
            stm_update.setBoolean(2, entity.isGender());
            stm_update.setString(3, entity.getAddress());
            stm_update.setDate(4, entity.getDob());
            stm_update.setInt(5, entity.getDept().getId());
            stm_update.setString(6, entity.getUpdatedby().getUsername());
            stm_update.setInt(7, entity.getId());
            stm_update.executeUpdate();

        } catch (SQLException ex) {
            Logger.getLogger(EmployeeDBContext.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                connection.close();
            } catch (SQLException ex) {
                Logger.getLogger(EmployeeDBContext.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public void delete(Employee entity) {
        String sql_update = "DELETE FROM Employee\n"
                + " WHERE eid = ?";

        PreparedStatement stm_update = null;

        try {

            stm_update = connection.prepareStatement(sql_update);
            stm_update.setInt(1, entity.getId());
            stm_update.executeUpdate();

        } catch (SQLException ex) {
            Logger.getLogger(EmployeeDBContext.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                connection.close();
            } catch (SQLException ex) {
                Logger.getLogger(EmployeeDBContext.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public ArrayList<Employee> list() {
        ArrayList<Employee> emps = new ArrayList<>();
        PreparedStatement command = null;
        try {
            String sql = "SELECT [eid]\n"
                    + "      ,[ename]\n"
                    + "      ,[gender]\n"
                    + "      ,[address]\n"
                    + "      ,[dob]\n"
                    + "  FROM [Employee]";

            command = connection.prepareStatement(sql);
            ResultSet rs = command.executeQuery();
            while (rs.next()) {
                Employee e = new Employee();
                e.setId(rs.getInt("eid"));
                e.setName(rs.getNString("ename"));
                e.setGender(rs.getBoolean("gender"));
                e.setDob(rs.getDate("dob"));
                e.setAddress(rs.getString("address"));
                emps.add(e);
            }

        } catch (SQLException ex) {
            Logger.getLogger(EmployeeDBContext.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                command.close();
                connection.close();
            } catch (SQLException ex) {
                Logger.getLogger(EmployeeDBContext.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return emps;
    }

    @Override
    public Employee get(int id) {

        PreparedStatement command = null;
        try {
            String sql = "SELECT e.eid,e.ename,e.gender,e.dob,e.address\n"
                    + "		,d.did,d.dname,\n"
                    + "		e.updatedtime,\n"
                    + "		c.username as cusername,c.displayname as cdisplayname,\n"
                    + "		u.username as uusername, u.displayname as udisplayname\n"
                    + "FROM Employee e \n"
                    + "	INNER JOIN Department d ON d.did = e.did\n"
                    + "	INNER JOIN [User] c ON c.username = e.createdby\n"
                    + "	LEFT JOIN [User] u ON u.username = e.updatedby\n"
                    + "	WHERE e.eid = ?";

            command = connection.prepareStatement(sql);
            command.setInt(1, id);
            ResultSet rs = command.executeQuery();
            if (rs.next()) {
                Employee e = new Employee();
                e.setId(rs.getInt("eid"));
                e.setName(rs.getNString("ename"));
                e.setGender(rs.getBoolean("gender"));
                e.setDob(rs.getDate("dob"));
                e.setAddress(rs.getString("address"));
                e.setUpdatedtime(rs.getTimestamp("updatedtime"));

                Department d = new Department();
                d.setId(rs.getInt("did"));
                d.setName(rs.getString("dname"));
                e.setDept(d);

                User c = new User();
                c.setUsername(rs.getString("cusername"));
                c.setDisplayname(rs.getString("cdisplayname"));
                e.setCreatedby(c);

                String uusername = rs.getString("uusername");
                if (uusername != null) {
                    User u = new User();
                    u.setUsername(rs.getString("uusername"));
                    u.setDisplayname(rs.getString("udisplayname"));
                    e.setUpdatedby(u);
                }

                return e;
            }

        } catch (SQLException ex) {
            Logger.getLogger(EmployeeDBContext.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                command.close();
                connection.close();
            } catch (SQLException ex) {
                Logger.getLogger(EmployeeDBContext.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;

    }

}
