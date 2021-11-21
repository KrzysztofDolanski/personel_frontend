package sample.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import sample.dto.ItemDto;
import sample.dto.ItemSaveDto;
import sample.handler.ProcessFinishHandler;

import java.util.Arrays;
import java.util.List;

public class ItemRestClient {

    private static final String ITEMS_URL = "http://localhost:8080/items";

    private final RestTemplate restTemplate;

    public ItemRestClient() {
        restTemplate = new RestTemplate();
    }


    public List<ItemDto> getItems(){
        ResponseEntity<ItemDto[]> itemResponseEntity = restTemplate.getForEntity(ITEMS_URL, ItemDto[].class);
        return Arrays.asList(itemResponseEntity.getBody());
    }

    public void saveItem(ItemSaveDto dto, ProcessFinishHandler processFinishHandler) {
        ResponseEntity<ItemDto> responseEntity = restTemplate.postForEntity(ITEMS_URL, dto, ItemDto.class);
        if (responseEntity.getStatusCode().equals(HttpStatus.OK)){
            processFinishHandler.handle();
        }else {
            throw new RuntimeException("Can't save dto " + dto);
        }
    }

    public ItemDto getItem(Long idItem) {
        ResponseEntity<ItemDto> forEntity = restTemplate.getForEntity(ITEMS_URL + "/" + idItem, ItemDto.class);
        return forEntity.getBody();
    }
}
