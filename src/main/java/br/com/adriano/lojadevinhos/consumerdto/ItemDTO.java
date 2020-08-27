package br.com.adriano.lojadevinhos.consumerdto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ItemDTO {

    private String produto;
    private String variedade;
    private String pais;
    private String categoria;
    private String safra;
    private BigDecimal preco;

}
