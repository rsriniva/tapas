package com.radoslavhusar.tapas.ejb.session;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.radoslavhusar.tapas.ejb.entity.Employee;

@Stateless(name = "employeeService")
@Local(EmployeeService.class)
public class EmployeeServiceBean implements EmployeeService {

   @PersistenceContext
   private EntityManager entityManager;

   @Override
   public void deleteEmployee(long employeeId) throws Exception {
      entityManager.remove(entityManager.find(Employee.class, employeeId));
   }

   @Override
   public Employee findEmployee(long employeeId) {
      return entityManager.find(Employee.class, employeeId);
   }

   @Override
   public void saveEmployee(long employeeId, String name, String surname,
           String jobDescription) throws Exception {
      Employee emp = new Employee();
      emp.setEmployeeId(employeeId);
      emp.setEmployeeName(name);
      emp.setEmployeeSurname(surname);
      emp.setJob(jobDescription + "jobsuffix");

      entityManager.persist(emp);

   }

   @Override
   public void saveOrUpdateEmployee(long employeeId, String name,
           String surname, String jobDescription) throws Exception {
      Employee emp = new Employee();
      emp.setEmployeeId(employeeId);
      emp.setEmployeeName(name);
      emp.setEmployeeSurname(surname);
      emp.setJob(jobDescription + "jobsuffix");

      entityManager.merge(emp);
   }

   @Override
   public void updateEmployee(long employeeId, String name, String surname, String jobDescription) throws Exception {
      Employee emp = entityManager.find(Employee.class, employeeId);
      emp.setEmployeeName(name);
      emp.setEmployeeSurname(surname);
      emp.setJob(jobDescription);

      entityManager.merge(emp);
   }
}