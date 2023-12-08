package cz.muni.fi.pv168.project.ui.wiring;

import cz.muni.fi.pv168.employees.business.model.Department;
import cz.muni.fi.pv168.employees.business.model.Employee;
import cz.muni.fi.pv168.employees.business.repository.DepartmentRepository;
import cz.muni.fi.pv168.employees.business.repository.EmployeeRepository;
import cz.muni.fi.pv168.employees.business.service.crud.CrudService;
import cz.muni.fi.pv168.employees.business.service.export.ExportService;
import cz.muni.fi.pv168.employees.business.service.export.ImportService;
import cz.muni.fi.pv168.employees.business.service.validation.Validator;
import cz.muni.fi.pv168.employees.storage.sql.db.DatabaseManager;
import cz.muni.fi.pv168.employees.storage.sql.db.TransactionExecutor;

/**
 * Interface for instance wiring
 */
public interface DependencyProvider {

    DatabaseManager getDatabaseManager();

    DepartmentRepository getDepartmentRepository();

    EmployeeRepository getEmployeeRepository();

    TransactionExecutor getTransactionExecutor();

    CrudService<Employee> getEmployeeCrudService();

    CrudService<Department> getDepartmentCrudService();

    ImportService getImportService();

    ExportService getExportService();

    Validator<Employee> getEmployeeValidator();
}

