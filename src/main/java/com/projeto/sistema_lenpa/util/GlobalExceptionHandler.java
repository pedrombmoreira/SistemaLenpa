package com.projeto.sistema_lenpa.util;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Object> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {

        String friendlyMessage = " ";
        // Pega a causa raiz do erro, que contém a mensagem específica do banco de dados.
        Throwable rootCause = ex.getRootCause();
        String message = rootCause != null ? rootCause.getMessage() : ex.getMessage();

        if (message.contains("compradores")) {
            friendlyMessage = "Não é possível excluir o comprador, pois ele está associado a uma ou mais entregas.";
        }
        else if (message.contains("plantas")) {
            if (message.contains("entregas")) {
                friendlyMessage = "Não é possível excluir a planta, pois ela está associada a uma ou mais entregas.";
            } else if (message.contains("lote_mudas")) {
                friendlyMessage = "Não é possível excluir a planta, pois ela está associada a um ou mais lotes de mudas.";
            }
        }
        else {
            // Uma mensagem genérica para qualquer outra violação de integridade que possa ocorrer.
            friendlyMessage = "Operação falhou: O registro não pode ser excluído pois está sendo utilizado por outra parte do sistema.";
        }
        Map<String, String> errorResponse = Map.of("ERRO", friendlyMessage);
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }
}
