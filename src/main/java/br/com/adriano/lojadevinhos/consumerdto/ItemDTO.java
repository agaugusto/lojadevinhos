package br.com.adriano.lojadevinhos.consumerdto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class ItemDTO {

    private String produto;
    private String variedade;
    private String pais;
    private String categoria;
    private String safra;
    private BigDecimal preco;

}
