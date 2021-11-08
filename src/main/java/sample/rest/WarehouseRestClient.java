package sample.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import sample.dto.WarehouseModuleDto;

public class WarehouseRestClient {

    public static final String WAREHOUSE_MODULE_DATA_URL = "http://localhost:8080/warehouse_module_data";

    private final RestTemplate restTemplate;


    public WarehouseRestClient() {
        restTemplate = new RestTemplate();
    }


    public WarehouseModuleDto getWarehouseModuleDto(){
        ResponseEntity<WarehouseModuleDto> responseEntity = restTemplate.getForEntity(WAREHOUSE_MODULE_DATA_URL, WarehouseModuleDto.class);
        return responseEntity.getBody();
    }

    public WarehouseModuleDto getWarehouseModuleDto(Long idWarehouse){
        ResponseEntity<WarehouseModuleDto> responseEntity = restTemplate.getForEntity(WAREHOUSE_MODULE_DATA_URL + "?idWarehouse=" + idWarehouse, WarehouseModuleDto.class);
        return responseEntity.getBody();
    }
}
