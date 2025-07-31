package service;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

import config.DatabaseConfig;
import models.Employee;

@Named("employeeService")
@ApplicationScoped
public class EmployeeService {

	Logger LOGGER = Logger.getLogger(EmployeeService.class.getName());

	public List<Employee> findAll() {
		List<Employee> employees = new ArrayList<Employee>();
		String sql = "SELECT * FROM project1.\"Mt_employee\"";
		try (Connection con = DatabaseConfig.getDataSource().getConnection();
				PreparedStatement statement = con.prepareStatement(sql);
				ResultSet resultSet = statement.executeQuery()) {
			while (resultSet.next()) {
				Employee employee = new Employee();
				employee.setCode(resultSet.getInt(1));
				employee.setName(resultSet.getString(2));
				employee.setAge(resultSet.getInt(3));
				employee.setDateOfBirth(resultSet.getDate(4).toLocalDate());
				employees.add(employee);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			LOGGER.info(e.getMessage());
			return null;
		}
		return employees;
	}

	public boolean deleteById(int code) {
		String sql = "DELETE FROM project1.\"Mt_employee\" WHERE employee_code = ?";
		LOGGER.info(sql);
		try (Connection con = DatabaseConfig.getDataSource().getConnection();
				PreparedStatement statement = con.prepareStatement(sql)) {
			statement.setInt(1, code);
			statement.execute();
			return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			LOGGER.info(e.getMessage());
			return false;
		}
	}

	public boolean update(Employee employee) {
		employee.setAge(LocalDate.now().getYear() - employee.getDateOfBirth().getYear());
		String sql = "UPDATE project1.\"Mt_employee\" SET employee_name=?, employee_age=?, date_of_birth=? WHERE employee_code=?";
		LOGGER.info(sql);
		try (Connection con = DatabaseConfig.getDataSource().getConnection();
				PreparedStatement statement = con.prepareStatement(sql)) {
			statement.setInt(4, employee.getCode());
			statement.setString(1, employee.getName());
			statement.setInt(2, employee.getAge());
			statement.setDate(3, Date.valueOf((employee.getDateOfBirth())));
			statement.execute();
			return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			LOGGER.info(e.getMessage());
			return false;
		}
	}

	public boolean insert(Employee employee) {

		employee.setAge(LocalDate.now().getYear() - employee.getDateOfBirth().getYear());
		String sql = "INSERT INTO project1.\"Mt_employee\" (employee_name, employee_age, date_of_birth) VALUES(?,?,?)";
		LOGGER.info(sql);
		try (Connection con = DatabaseConfig.getDataSource().getConnection();
				PreparedStatement statement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			statement.setString(1, employee.getName());
			statement.setInt(2, employee.getAge());
			statement.setDate(3, Date.valueOf((employee.getDateOfBirth())));
			int code = statement.executeUpdate();
			employee.setCode(code);
			return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			LOGGER.info(e.getMessage());
			return false;
		}
	}

}
