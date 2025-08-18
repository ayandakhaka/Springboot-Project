package com.ayandakhaka.springboot.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.rmi.RMISecurityManager;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.server.ResponseStatusException;

import com.ayandakhaka.springboot.dto.EmployeeDto;
import com.ayandakhaka.springboot.exception.GlobalExceptionHandler;
import com.ayandakhaka.springboot.exception.ResourceNotFoundException;
import com.ayandakhaka.springboot.service.EmployeeService;
import com.fasterxml.jackson.databind.ObjectMapper;

//@SpringBootTest
@WebMvcTest(EmployeeController.class)
public class EmployeeServiceTest {

	private static final String END_POINT = "/api/employees";
	@Autowired
	private MockMvc mockMvc;
	@MockBean
	private EmployeeService employeeService;
	@Autowired
	private ObjectMapper objectMapper;

	private EmployeeController employeeController;

	@BeforeEach
	void setup() {
		// 1. Mock the service
		employeeService = mock(EmployeeService.class);

		// 2. Inject mock into the controller
		employeeController = new EmployeeController();
		employeeController.setEmployeeService(employeeService); // ← You need a setter or constructor

		// 3. Setup MockMvc with controller and exception handler
		mockMvc = MockMvcBuilders
				.standaloneSetup(employeeController)
				.setControllerAdvice(new GlobalExceptionHandler())
				.build();
	}
	/*
	 * Add create user API : POST /users
	 *  - Case 1 : Expect 400 bad request if request body contains invalid data
	 *  
	 */
	@Test
	public void testCreateEmployee_ShouldReturn400BadRequest() throws Exception {

		EmployeeDto invalidEmployee = new EmployeeDto();

		invalidEmployee.setFirstName("");
		invalidEmployee.setLastName("");
		invalidEmployee.setEmail("");
		invalidEmployee.setId(1L);


		String requestBody = objectMapper.writeValueAsString(invalidEmployee);
		//Mocking service layer
		Mockito.when(employeeService.createEmployee(Mockito.any(EmployeeDto.class)))
		.thenThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST));

		mockMvc.perform(post(END_POINT)
				.contentType("application/json")
				.content(requestBody))
		.andExpect(status().isBadRequest());

	}

	/*
	 * Case 2 : Expect status code 201 created if request body contains valid data
	 * 1. Response has content type "application/json"
	 * 2. Response contains a header "Location" with value is "/api/employees/{id}"
	 * 3. JSON response body has field "email" with value is user's email
	 */

	@Test
	public void testCreatedEmployee_ShouldReturn201Created() throws Exception {
		String email = "alwande@gmail.com";
		EmployeeDto validEmployee = new EmployeeDto();
		//validEmployee.setId(1L);
		validEmployee.setFirstName("Alwande");
		validEmployee.setLastName("Khaka");
		validEmployee.setEmail(email);

		when(employeeService.createEmployee(any(EmployeeDto.class)))
		.thenReturn(validEmployee); // Mocking the service layer

		String requestBody = objectMapper.writeValueAsString(validEmployee);

		mockMvc.perform(post(END_POINT)
				.contentType("application/json")
				.content(requestBody))
		.andExpect(status().isCreated())
		.andExpect(header().string("Location", is("/api/employees/" + validEmployee.getId())))
		.andExpect(jsonPath("$.email").value(email)); 
	}

	/*
	 * Test Get Employee API : GET /api/employees
	 * 
	 * 1. Case #1 Expected status 404 not found if no user found with the given ID
	 *
	 */
	@Test
	public void testListEmployeeById_shouldReturn404NotFound() throws Exception {

		Long employeeId = 999L;

		//Mocking service layer
		when(employeeService.getSingleEmployeeById(employeeId)).
		thenThrow(new ResourceNotFoundException("Employee", "Id", employeeId));

		mockMvc.perform(get(END_POINT + "/{id}", employeeId))
		.andExpect(status().isNotFound())
		.andExpect(content().string(containsString("Employee not found with Id : " + employeeId)))
		.andDo(print());


	}

	/*
	 * Test Get Employee API : GET /api/employees
	 * 
	 * 1. Case #1 Expected empty list if getting all employees from empty database
	 *
	 */
	@Test
	public void testListAllEmployees_shouldReturn204ContentNotFound() throws Exception {

		//Mocking service layer
		Mockito.when(employeeService.getAllEmployees()).thenReturn(Collections.emptyList());

		mockMvc.perform(get("/api/employees"))
		.andExpect(status().isNoContent());
	}

	//	/*
	//	 * 1. Case #2 Expect status 200 OK if a user is found with the given ID
	//	 * 2. Response has content type "application/json"
	//	 * 3. JSON in response body has field "email" with value is user's email
	//	 */
	@Test
	public void testListEmployeeById_shouldReturn200OK() throws Exception {

		Long employeeId = 1L;
		String email = "alwande@gmail.com";
		EmployeeDto validEmployee = new EmployeeDto();
		validEmployee.setId(1L);
		validEmployee.setFirstName("Alwande");
		validEmployee.setLastName("Khaka");
		validEmployee.setEmail(email);

		//Mocking the service
		when(employeeService.getSingleEmployeeById(employeeId)).thenReturn(validEmployee);
		mockMvc.perform(get("/api/employees/{id}", employeeId)
				.accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk())
		.andExpect(content().contentType(MediaType.APPLICATION_JSON))
		.andExpect(jsonPath("$.id").value(employeeId))
		.andExpect(jsonPath("$.firstName").value("Alwande"))
		.andExpect(jsonPath("$.lastName").value("Khaka"))
		.andExpect(jsonPath("$.email").value("alwande@gmail.com"))
		.andDo(print());


	}

	/*
	 * 1. Case #2 Expect status 200 OK if a user is found with the given ID
	 * 2. Response has content type "application/json"
	 * 3. JSON in response body has field "email" with value is user's email
	 */
	@Test
	public void testListAllEmployees_shouldReturn200OK() throws Exception {

		List<EmployeeDto> employees = new ArrayList<>();
		employees.add(new EmployeeDto(1L, "John", "Doe", "john@example.com"));
		employees.add(new EmployeeDto(2L, "Jane", "Smith", "jane@example.com"));

		//Mocking the service
		Mockito.when(employeeService.getAllEmployees()).thenReturn(employees);

		mockMvc.perform(get("/api/employees"))
		.andExpect(status().isOk())
		.andExpect(content().contentType(MediaType.APPLICATION_JSON))
		.andExpect(jsonPath("$.length()").value(2))
		.andExpect(jsonPath("$[0].email").value("john@example.com"))
		.andExpect(jsonPath("$[0].firstName").value("John"))
		.andExpect(jsonPath("$[0].lastName").value("Doe"))
		.andExpect(jsonPath("$[1].email").value("jane@example.com"))
		.andExpect(jsonPath("$[1].firstName").value("Jane"))
		.andExpect(jsonPath("$[1].lastName").value("Smith"));

	}


	/*
	 * Test Update User API: PUT /users/{id}
	 * 1. Case 1 : Expected 404 Not found if no user found with the given ID
	 * 
	 */
	@Test
	public void testUpdateEmployeeDetails_ShouldReturn404NotFound() throws Exception {

		Long employeeId = 999L;
		String requestURI = END_POINT + "/" + employeeId;

		String email = "alwande@gmail.com";
		EmployeeDto validEmployee = new EmployeeDto();

		validEmployee.setId(1L);
		validEmployee.setFirstName("Alwande Elihle");
		validEmployee.setLastName("Khaka");
		validEmployee.setEmail(email);

		String requestBody = objectMapper.writeValueAsString(validEmployee);

		Mockito.when(employeeService.updateEmployee(Mockito.eq(employeeId), Mockito.any(EmployeeDto.class)))
		.thenThrow(new ResourceNotFoundException("Employee", "Id", employeeId));

		mockMvc.perform(put(requestURI).contentType("application/json")
				.content(requestBody))
		.andExpect(status().isNotFound())
		.andExpect(jsonPath("$.status").value(404))
		.andExpect(jsonPath("$.message").exists())
		.andExpect(jsonPath("$.timeStamp").exists());

	}



	/*
	 * Test Update User API: PUT /users/{id}
	 * 1. Case 1 : Expected 400 Bad request if required field are invalid
	 * 
	 */
	@Test
	public void testUpdateEmployeeDetails_ShouldReturn400BadRequest() throws Exception {

		Long employeeId = 10L;
		String requestURI = END_POINT + "/" + employeeId;

		String email = "alwande@gmail.com";
		EmployeeDto invalidEmployee = new EmployeeDto();

		invalidEmployee.setFirstName("");
		invalidEmployee.setLastName("");
		invalidEmployee.setEmail(email);

		String requestBody = objectMapper.writeValueAsString(invalidEmployee);


		mockMvc.perform(put(requestURI).contentType("application/json")
				.content(requestBody))
		.andExpect(status().isBadRequest())
		.andExpect(jsonPath("$.status").value(400))
		.andExpect(jsonPath("$.message").exists())
		.andExpect(jsonPath("$.timeStamp").exists());

	}

	/*
	 * Test Update User API: PUT /users/{id}
	 * 1. Case 1 : Expected 200 OK if request body contains valid data
	 * 2. JSON in response body has field email valid user's edmail
	 * 
	 * 
	 */
	@Test
	public void testUpdateEmployeeDetails_ShouldReturn200OK() throws Exception {

		Long existingId = 1L;
		String requestURI = END_POINT + "/" + existingId;

		EmployeeDto existingEmployee = new EmployeeDto();
		existingEmployee.setId(existingId);
		existingEmployee.setFirstName("John");
		existingEmployee.setLastName("Doe");
		existingEmployee.setEmail("john.doe@example.com");

		Mockito.when(employeeService.getSingleEmployeeById(existingId))
		.thenReturn(existingEmployee);

		EmployeeDto updatedEmployee = new EmployeeDto();
		updatedEmployee.setId(existingId);
		updatedEmployee.setFirstName("Jane");
		updatedEmployee.setLastName("Doe");
		updatedEmployee.setEmail("jane.doe@example.com");

		Mockito.when(employeeService.updateEmployee(
				Mockito.eq(existingId),
				Mockito.any(EmployeeDto.class)))
		.thenReturn(updatedEmployee);

		String requestBody = objectMapper.writeValueAsString(updatedEmployee);

		mockMvc.perform(put(requestURI)
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestBody))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.firstName").value("Jane"))
		.andExpect(jsonPath("$.lastName").value("Doe"))
		.andExpect(jsonPath("$.email").value("jane.doe@example.com"))
		.andDo(print());
	}

	/*
	 * Test Delete User API: DELETE /api/edmployees
	 * 1. Case #1 : Expect status 404 not found if no user found with the given ID
	 * 
	 */
	@Test
	public void 
	testDeleteEmployee_ShouldReturn404NotFound_WhenEmployeeIdDoesNotExist() throws Exception {

		Long employeeId = 1111L;
		String requestURI = END_POINT + "/" + employeeId;

		Mockito.doThrow(new ResponseStatusException(HttpStatus.NOT_FOUND))
		.when(employeeService)
		.deleteEmployee(employeeId);

		mockMvc.perform(delete(requestURI, employeeId).contentType("application/json"))
		.andExpect(status().isNotFound())
		.andExpect(jsonPath("$.status").value(404))
		//.andExpect(jsonPath("$.message").exists())
		.andExpect(jsonPath("$.timeStamp").exists());

	}

	@Test
	public void testDeleteEmployee_ShouldReturn204NoContent() throws Exception {

		Long employeeId = 1L;
		String requestURI = END_POINT + "/" + employeeId;

		//Mockito.doThrow(new ResponseStatusException(HttpStatus.NO_CONTENT))
		//.when(employeeService)
		//.deleteEmployee(employeeId);

		doNothing().when(employeeService).deleteEmployee(employeeId);

		mockMvc.perform(delete(requestURI, employeeId).contentType("application/json"))
		.andExpect(status().isNoContent())
		.andDo(print());

	}

	//

	//
	//

}
