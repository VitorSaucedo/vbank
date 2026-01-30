package com.vitorsaucedo.vbank.exceptions;

import com.vitorsaucedo.vbank.config.GlobalExceptionHandler;

/**
 * Exceção lançada quando dados inválidos são fornecidos.
 *
 * Exemplos:
 * - CPF com formato inválido
 * - Chave PIX fora do padrão
 * - Valores negativos onde não são permitidos
 *
 * @apiNote Retorna HTTP 400 (Bad Request)
 * @see GlobalExceptionHandler#handleInvalidDataException(InvalidDataException)
 */
public class InvalidDataException extends VbankException {

    public InvalidDataException(String message) {
        super(message);
    }

    /**
     * Construtor para criar mensagem formatada de dados inválidos.
     *
     * @param fieldName Nome do campo com dados inválidos
     * @param reason Motivo da invalidez
     */
    public InvalidDataException(String fieldName, String reason) {
        super(String.format("Dados inválidos no campo '%s': %s", fieldName, reason));
    }
}
