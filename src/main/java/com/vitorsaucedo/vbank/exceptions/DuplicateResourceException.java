package com.vitorsaucedo.vbank.exceptions;

import com.vitorsaucedo.vbank.config.GlobalExceptionHandler;

/**
 * Exceção lançada quando se tenta cadastrar um recurso duplicado.
 *
 * Exemplos:
 * - E-mail já cadastrado
 * - CPF já cadastrado
 * - Chave PIX já cadastrada
 *
 * @apiNote Retorna HTTP 409 (Conflict)
 * @see GlobalExceptionHandler#handleDuplicateResourceException(DuplicateResourceException)
 */
public class DuplicateResourceException extends VbankException {

    public DuplicateResourceException(String message) {
        super(message);
    }

    /**
     * Construtor para criar mensagem formatada de recurso duplicado.
     *
     * @param resourceName Nome do recurso (ex: "Usuário", "Chave PIX")
     * @param field Campo duplicado (ex: "e-mail", "CPF")
     */
    public DuplicateResourceException(String resourceName, String field) {
        super(String.format("%s já cadastrado(a) com este(a) %s.", resourceName, field));
    }
}
