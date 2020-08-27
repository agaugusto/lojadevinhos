package br.com.adriano.lojadevinhos.consumerdto;


import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class ClienteDTO {

    private Integer id;
    private String nome;
    private String cpf;
}
