package sample.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import sample.dto.EmployeeDto;
import sample.handler.SavedEmployeeHandler;

import java.util.Arrays;
import java.util.List;

public class EmployeeRestClient {

    private static final String EMPLOYEES_URL = "http://localhost:8080/employees";

    private final RestTemplate restTemplate;


    public EmployeeRestClient(){
        restTemplate = new RestTemplate();
    }


    public List<EmployeeDto> getEmployees(){
        ResponseEntity<EmployeeDto[]> employees = restTemplate.getForEntity(EMPLOYEES_URL, EmployeeDto[].class);
        return Arrays.asList(employees.getBody());
    }

    public void saveEmployee(EmployeeDto employeeDto, SavedEmployeeHandler employeeHandler) {
        ResponseEntity<EmployeeDto> responseEntity = restTemplate.postForEntity(EMPLOYEES_URL, employeeDto, EmployeeDto.class);
        if (HttpStatus.OK.equals(responseEntity.getStatusCode())){
            employeeHandler.handle();
        } else {
            //TODO warning window
        }
    }

    public EmployeeDto getEmployee(Long idEmployee) {
        String url = EMPLOYEES_URL + "/" + idEmployee;
        ResponseEntity<EmployeeDto> responseEntity = restTemplate.getForEntity(url, EmployeeDto.class);
        if (HttpStatus.OK.equals(responseEntity.getStatusCode())){
            return responseEntity.getBody();
        } else {

            //TODO implement
            throw new RuntimeException("Can't load Employee");
        }
    }
}
