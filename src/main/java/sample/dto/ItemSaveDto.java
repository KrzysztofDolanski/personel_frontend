package sample.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemSaveDto {

    private String name;
    private Double quantity;
    private Long idQuantityType;
    private Long idWarehouse;
}
