package com.ufla.map;

/**
 * Interface define o contrato de um mapa, esse contrato incluio recuperar um
 * valor através de uma chave e também adicionar uma tupla chave e valor.
 * 
 * @author carlos
 * 
 * @param <C>
 *            Determina a classe das chaves do mapa.
 * @param <V>
 *            Determina a classe dos valores do mapa.
 */
public interface Mapa<C, V> {

	/**
	 * Recupera um valor referente a uma determinada chave.
	 * 
	 * @param chave
	 *            chave utilizada para encontrar seu valor referente
	 * @return valor referente a chave ou null caso esta chave não pertence ao
	 *         mapa
	 */
	V get(C chave);

	/**
	 * Adiciona uma tupla chave/valor no dicionário.
	 * 
	 * @param chave
	 *            chave a ser adicionada
	 * @param valor
	 *            valor a ser adicionado referente a chave
	 */
	void put(C chave, V valor);
}
