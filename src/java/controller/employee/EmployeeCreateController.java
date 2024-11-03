/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

package controller.employee;

import controller.accesscontrol.BaseRBACController;
import controller.accesscontrol.BaseRequiredAuthenticationController;
import dal.DepartmentDBContext;
import dal.EmployeeDBContext;
import entity.Department;
import entity.Employee;
import entity.accesscontrol.User;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.sql.Date;

/**
 *
 * @author sonnt-local hand-some
 */
public class EmployeeCreateController extends BaseRBACController {

    @Override
    protected void doAuthorizedGet(HttpServletRequest req, HttpServletResponse resp, User account) throws ServletException, IOException {
        DepartmentDBContext db = new DepartmentDBContext();
        ArrayList<Department> depts = db.list();
        req.setAttribute("depts", depts);
        req.getRequestDispatcher("../view/employee/create.jsp").forward(req, resp);
    }

    @Override
    protected void doAuthorizedPost(HttpServletRequest req, HttpServletResponse resp, User account) throws ServletException, IOException {
        //read parameters
        String raw_name =req.getParameter("name");
        String raw_gender = req.getParameter("gender");
        String raw_dob = req.getParameter("dob");
        String raw_address = req.getParameter("address");
        String raw_did = req.getParameter("did");
        
        //validate params
        
        
        //object binding
        Employee e = new Employee();
        e.setName(raw_name);
        e.setAddress(raw_address);
        e.setGender(raw_gender.equals("male"));
        e.setDob(Date.valueOf(raw_dob));
        
        Department d = new Department();
        d.setId(Integer.parseInt(raw_did));
        e.setDept(d);
        
        e.setCreatedby(account);
        //save data to database
        
        EmployeeDBContext db = new EmployeeDBContext();
        db.insert(e);
        
        //return results to user
        resp.getWriter().println("Inserted "+ e.getId());
    
    
    }
   
   

}
