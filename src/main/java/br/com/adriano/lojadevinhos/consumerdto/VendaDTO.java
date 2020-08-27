package br.com.adriano.lojadevinhos.consumerdto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class VendaDTO {

    private String data;
    private String cliente;
    private List<ItemDTO> itens;
    private BigDecimal valorTotal;
}
