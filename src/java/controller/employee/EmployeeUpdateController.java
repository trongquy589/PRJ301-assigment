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
import java.sql.Date;
import java.util.ArrayList;

/**
 *
 * @author sonnt-local hand-some
 */
public class EmployeeUpdateController extends BaseRBACController {

    @Override
    protected void doAuthorizedGet(HttpServletRequest req, HttpServletResponse resp, User account) throws ServletException, IOException {
        int id = Integer.parseInt(req.getParameter("id"));
        EmployeeDBContext db = new EmployeeDBContext();
        Employee e = db.get(id);
        if(e!=null)
        {
            DepartmentDBContext dbDept = new DepartmentDBContext();
            ArrayList<Department> depts = dbDept.list();
            req.setAttribute("e", e);
            req.setAttribute("depts", depts);
            req.getRequestDispatcher("../view/employee/update.jsp").forward(req, resp);
        }
        else
        {
            resp.sendError(404,"this employee does not exist!");
        }
    
    }

    @Override
    protected void doAuthorizedPost(HttpServletRequest req, HttpServletResponse resp, User account) throws ServletException, IOException {
         //read parameters
        String raw_id =req.getParameter("id");
        String raw_name =req.getParameter("name");
        String raw_gender = req.getParameter("gender");
        String raw_dob = req.getParameter("dob");
        String raw_address = req.getParameter("address");
        String raw_did = req.getParameter("did");
        
        //validate params
        
        
        //object binding
        Employee e = new Employee();
        e.setId(Integer.parseInt(raw_id));
        e.setName(raw_name);
        e.setAddress(raw_address);
        e.setGender(raw_gender.equals("male"));
        e.setDob(Date.valueOf(raw_dob));
        
        Department d = new Department();
        d.setId(Integer.parseInt(raw_did));
        e.setDept(d);
        
        e.setUpdatedby(account);
        //save data to database
        
        EmployeeDBContext db = new EmployeeDBContext();
        db.update(e);
        
        //return results to user
        resp.getWriter().println("Done");
    
    
    }
}
