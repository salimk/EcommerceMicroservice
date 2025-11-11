package com.ecom.commandesservice.feignrequests;

import lombok.Data;
import lombok.Value;

@Data
public class ProduitDto {
    private Long id;
    private  String nom;
    private  String description;
    private  String sku;
    private  double prix;
    private  int qteStock;
}
