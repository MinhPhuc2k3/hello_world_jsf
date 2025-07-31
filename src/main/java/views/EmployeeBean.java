package views;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import models.Employee;
import service.EmployeeService;

@Named(value = "EmployeeBean")
@SessionScoped
public class EmployeeBean implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private List<Employee> employees;
	
	private Map<Integer, Employee> employeeMap;
	
	private int selectedEmployeeCode;
	
	private Employee employee = new Employee();
	
	private Logger LOGGER = Logger.getLogger(EmployeeBean.class.getName());
	@Inject
	private EmployeeService employeeService;

	public List<Employee> getEmployees() {
		employees = employeeService.findAll();
		employees.sort((e1, e2) -> Integer.compare(e1.getCode(), e2.getCode()));
		employeeMap = employees.stream().collect(Collectors.toMap(Employee::getCode, Function.identity()));
		return employees;
	}

	public void setEmployees(List<Employee> employees) {
		this.employees = employees;
	}

	public int getSelectedEmployeeCode() {
		return selectedEmployeeCode;
	}

	public void setSelectedEmployeeCode(int selectedEmployeeCode) {
		this.selectedEmployeeCode = selectedEmployeeCode;
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	public void delete() {
		LOGGER.info(Integer.toString(selectedEmployeeCode));
		if (employeeService.deleteById(selectedEmployeeCode)) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Employee deleted"));
		} else
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Fail", "Error"));
	}

	public void update() {
		Employee selectedEmployee = employeeMap.get(selectedEmployeeCode);
		selectedEmployee.setDateOfBirth(employee.getDateOfBirth());
		selectedEmployee.setName(employee.getName());
		if (employeeService.update(selectedEmployee)) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Employee updated"));
		} else
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Fail", "Error"));
	}

	public void add() {
		if (employeeService.insert(employee)) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Employee inserted"));
		} else
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Fail", "Error"));
	}

}
