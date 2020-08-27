package br.com.adriano.lojadevinhos.handler;

import br.com.adriano.lojadevinhos.Exception.CadastroClienteNotFoundException;
import br.com.adriano.lojadevinhos.Exception.ExceptionDetails;
import br.com.adriano.lojadevinhos.Exception.HistoricoNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Date;

@ControllerAdvice
public class RestExceptionHandler {
    private static String title = "Resource not found";

    @ExceptionHandler(HistoricoNotFoundException.class)
    public ResponseEntity<?> handleValorIndisponivelException(HistoricoNotFoundException exception) {
        ExceptionDetails eDatails = ExceptionDetails.builder()
                .timeStamp(new Date().getTime())
                .status(HttpStatus.NOT_FOUND.value())
                .title(title)
                .detail(exception.getMessage())
                .developorMessage(exception.getClass().getName())
                .build();
        return new ResponseEntity<>(eDatails, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CadastroClienteNotFoundException.class)
    public ResponseEntity<?> handleValorIndisponivelException(CadastroClienteNotFoundException exception) {
        ExceptionDetails eDatails = ExceptionDetails.builder()
                .timeStamp(new Date().getTime())
                .status(HttpStatus.NOT_FOUND.value())
                .title(title)
                .detail(exception.getMessage())
                .developorMessage(exception.getClass().getName())
                .build();
        return new ResponseEntity<>(eDatails, HttpStatus.NOT_FOUND);
    }
}
