/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import entity.assignment.Plan;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PlanDBContext {

    private Connection connection; // Khởi tạo biến kết nối

    public List<Plan> getAllPlans() {
        List<Plan> plans = new ArrayList<>();
        String query = "SELECT TOP (1000) [PlanID]\n"
                + "      ,[PlanName]\n"
                + "      ,[StartDate]\n"
                + "      ,[EndDate]\n"
                + "      ,[DepartmentID]\n"
                + "  FROM [CreatedByPA].[dbo].[Plan]";

        try (
                PreparedStatement stm = connection.prepareStatement(query); ResultSet rs = stm.executeQuery()) {
            while (rs.next()) {
                Plan plan = new Plan();
                plan.setId(rs.getInt("PlanID"));
                plan.setName(rs.getString("PlanName"));
                plan.setStart(rs.getDate("StartDate"));
                plan.setEnd(rs.getDate("EndDate"));
                plan.setDept(rs.getInt("DepartmentID"));
                plans.add(plan);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return plans;
    }
}
