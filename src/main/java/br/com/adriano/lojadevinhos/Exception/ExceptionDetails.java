package br.com.adriano.lojadevinhos.Exception;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ExceptionDetails {
    private String title;
    private int status;
    private String detail;
    private Long timeStamp;
    private String developorMessage;
}
