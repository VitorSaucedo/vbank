package com.vitorsaucedo.vbank.exceptions;

import com.vitorsaucedo.vbank.config.GlobalExceptionHandler;

/**
 * Exceção lançada quando um recurso solicitado não é encontrado.
 *
 * Exemplos:
 * - Usuário não encontrado
 * - Conta não encontrada
 * - Chave PIX não encontrada
 * - Transação não encontrada
 *
 * @apiNote Retorna HTTP 404 (Not Found)
 * @see GlobalExceptionHandler#handleResourceNotFoundException(ResourceNotFoundException)
 */
public class ResourceNotFoundException extends VbankException {

    /**
     * Construtor simples com nome do recurso.
     *
     * @param resourceName Nome do recurso não encontrado
     */
    public ResourceNotFoundException(String resourceName) {
        super(resourceName + " não encontrado(a).");
    }

    /**
     * Construtor com nome do recurso e identificador.
     *
     * @param resourceName Nome do recurso (ex: "Usuário", "Chave PIX")
     * @param identifier Identificador usado na busca (ex: UUID, e-mail)
     */
    public ResourceNotFoundException(String resourceName, String identifier) {
        super(String.format("%s não encontrado(a) com identificador: %s", resourceName, identifier));
    }
}
