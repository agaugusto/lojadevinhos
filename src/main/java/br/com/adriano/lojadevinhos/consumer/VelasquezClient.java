package br.com.adriano.lojadevinhos.consumer;

import br.com.adriano.lojadevinhos.consumerdto.ClienteDTO;
import br.com.adriano.lojadevinhos.consumerdto.VendaDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Arrays;
import java.util.List;

@Service
public class VelasquezClient {

    private RestTemplate template = new RestTemplate();

    private UriComponents uriComponents() {
        return UriComponentsBuilder.newInstance()
                .scheme("http")
                .host("www.mocky.io")
                .path("v2")
                .build();
    }

    public List<ClienteDTO> buscarClientes() {
        var uri = uriComponents().toUriString() + "/598b16291100004705515ec5";
        ResponseEntity<ClienteDTO[]> clienteDTOList = template.getForEntity(uri, ClienteDTO[].class);

        return Arrays.asList(clienteDTOList.getBody());
    }

    public List<VendaDTO> buscarHistoricoVendas() {
        var uri = uriComponents().toUriString() + "/598b16861100004905515ec7";
        ResponseEntity<VendaDTO[]> vendaDTOList = template.getForEntity(uri, VendaDTO[].class);

        return Arrays.asList(vendaDTOList.getBody());
    }

}
