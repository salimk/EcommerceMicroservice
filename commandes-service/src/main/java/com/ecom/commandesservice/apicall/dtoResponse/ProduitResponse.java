package com.ecom.commandesservice.apicall.dtoResponse;

import lombok.Data;

@Data
public class ProduitResponse {
    private  Long id;
    private  String nom;
    private  String description;
    private  String sku;
    private  double prix;
}
