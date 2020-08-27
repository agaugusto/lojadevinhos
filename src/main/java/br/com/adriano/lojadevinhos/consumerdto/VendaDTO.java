package br.com.adriano.lojadevinhos.consumerdto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VendaDTO {

    private String data;
    private String cliente;
    private List<ItemDTO> itens;
    private BigDecimal valorTotal;
}
