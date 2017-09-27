package com.ufla.map;

import java.lang.reflect.Array;

/**
 * Implementação de um Mapa utilizando a estratégia de hashing em um array
 * único. O array é redimensionado quando atingir um determinado limite do
 * array. O tratamento de colisões é feito na próxima posição vaga no array após
 * a colisão.
 * 
 * @author carlos
 *
 * @param <C>
 *            Determina a classe das chaves do mapa.
 * @param <V>
 *            Determina a classe dos valores do mapa.
 */
public class MapaConcreto<C, V> implements Mapa<C, V> {

	/**
	 * Define um elemento de um mapa, uma tupla chave/valor.
	 * 
	 * @author carlos
	 *
	 * @param <C>
	 *            Determina a classe da chave da tupla.
	 * @param <V>
	 *            Determina a classe do valor da tupla.
	 */
	static class No<C, V> {

		private C chave;
		private V valor;

		public No(C chave, V valor) {
			this.chave = chave;
			this.valor = valor;
		}

		public C getChave() {
			return chave;
		}

		public void setChave(C chave) {
			this.chave = chave;
		}

		public V getValor() {
			return valor;
		}

		public void setValor(V valor) {
			this.valor = valor;
		}

		@Override
		public String toString() {
			return chave + ";" + valor;
		}

	}

	// array com as entradas, tuplas chave/valor
	private No<C, V>[] entradas;
	// limite de ocupação do array, quando atingir esse limite ele redimensiona
	// (em porcentagem)
	private double limite;
	// limite de ocupação do array, quando atingir esse limite ele redimensiona
	// (em quantidade)
	private int tamanhoMaximo;
	// contador de tuplas chave/valor contidas no mapa
	private int qtdEntradas;

	public MapaConcreto() {
		this(10, 0.7);
	}

	/**
	 * Criação de mapa utilizando um tamanho e um limite determinado pelo
	 * usuário.
	 * 
	 * @param tamanho
	 *            tamanho inicial do array. O tamanho deverá ser maior que 5, em
	 *            outros casos será definido como 5.
	 * @param limite
	 *            limite de ocupação do array (em porcentagem/razão 0-1). O
	 *            limite deve estar entre 0.45 e 0.8, um valor inválido
	 *            resultará em um limite de 0.7;
	 */
	@SuppressWarnings("unchecked")
	public MapaConcreto(int tamanho, double limite) {
		if (tamanho < 5) {
			tamanho = 5;
		}
		if (limite < 0.45 || limite > 0.8) {
			limite = 0.70;
		}
		this.entradas = (No<C, V>[]) Array.newInstance(No.class, tamanho);
		this.limite = limite;
		this.tamanhoMaximo = (int) (tamanho * limite);
		this.qtdEntradas = 0;
	}

	/**
	 * Compara a chave recebida por parâmetro com a chave relacionada com a
	 * posição recebido em relação ao array.
	 * 
	 * @param chave
	 *            chave a ser comparada
	 * @param posicao
	 *            posicao da chave que deverá ser usada na comparação
	 * @return true, se as chaves forem iguais, caso contrário false
	 */
	private boolean igualdadeChaves(C chave, int posicao) {
		return chave.equals(entradas[posicao].getChave());
	}

	/**
	 * Realiza a busca de uma chave no array, e retorna o índice onde a chave se
	 * encontra. Em caso da chave não estar no array retorna o índice de onde
	 * deveria estar. Se não for possível identificar a chave nem a posição que
	 * deveria estar, retorna -1.
	 * 
	 * @param chave
	 *            chave utilizada para realizar a buscar
	 * @param posicaoInicial
	 *            posiciao inicial da busca no array de tuplas
	 * @param posicaoLimite
	 *            posiciao limite, não inclusa, da busca no array de tuplas
	 * @return o índice onde a chave se encontra. Em caso da chave não estar no
	 *         array retorna o índice de onde deveria estar. Se não for possível
	 *         identificar a chave nem a posição que deveria estar, retorna -1.
	 */
	private int buscarChave(C chave, int posicaoInicial, int posicaoLimite) {
		for (int i = posicaoInicial; i < posicaoLimite; i++) {
			if (entradas[i] == null) {
				return i;
			}
			if (igualdadeChaves(chave, i)) {
				return i;
			}
		}
		return -1;
	}

	@Override
	public V get(C chave) {
		if (chave == null) {
			return null;
		}
		int posicao = hash(chave);
		int indice = buscarChave(chave, posicao, entradas.length);
		if (indice != -1) {
			if (entradas[indice] == null) {
				return null;
			}
			return entradas[indice].getValor();
		}
		indice = buscarChave(chave, 0, posicao);
		if (indice == -1 || entradas[indice] == null) {
			return null;
		}
		return entradas[indice].getValor();
	}

	/**
	 * Realiza a inserção de uma tupla chave/valor em um índice do array, em
	 * caso do índice ser -1 não realiza nenhuma operação.
	 * 
	 * @param chave
	 *            chave da tupla a ser inserida
	 * @param valor
	 *            valor da tupla a ser inserida
	 * @param indice
	 *            índice onde a tupla deve ser inserida
	 */
	private void putExec(C chave, V valor, int indice) {
		if (indice == -1) {
			return;
		}
		if (entradas[indice] == null) {
			entradas[indice] = new No<C, V>(chave, valor);
			qtdEntradas++;
		} else {
			entradas[indice].setValor(valor);
		}
	}

	@Override
	public void put(C chave, V valor) {
		if (chave == null) {
			return;
		}
		int posicao = hash(chave);
		int indice = buscarChave(chave, posicao, entradas.length);
		putExec(chave, valor, indice);
		indice = buscarChave(chave, 0, posicao);
		putExec(chave, valor, indice);
		if (qtdEntradas == tamanhoMaximo) {
			redimensionar();
		}
	}

	/**
	 * Redimensiona o array que contém as tuplas chave/valor, dobra o tamanho do
	 * array e reinsere as entradas.
	 */
	@SuppressWarnings("unchecked")
	private void redimensionar() {
		No<C, V>[] entradasAntigas = entradas;
		qtdEntradas = 0;
		tamanhoMaximo = (int) ((entradasAntigas.length >> 2) * limite);
		entradas = (No<C, V>[]) Array.newInstance(No.class,
				(entradasAntigas.length >> 2));
		reinsercaoDeEntradas(entradasAntigas);
	}

	/**
	 * Reinsere as entradas antigas no novo array.
	 * 
	 * @param entradasAntigas
	 *            entradas contidas no array antigo
	 */
	private void reinsercaoDeEntradas(No<C, V>[] entradasAntigas) {
		for (int i = 0; i < entradasAntigas.length; i++) {
			if (entradasAntigas[i] != null) {
				put(entradasAntigas[i].getChave(),
						entradasAntigas[i].getValor());
			}
		}
	}

	/**
	 * Calcula a hash de uma chave e retorna em módulo do tamanho do array
	 * indicando a posição da chave no melhor caso, (em caso de conflitos não
	 * é).
	 * 
	 * @param chave
	 *            chave a gerar hash
	 * 
	 * @return hash da chave em módulo pela quantidade de entradas
	 */
	private int hash(C chave) {
		return Math.abs(chave.hashCode()) % entradas.length;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < entradas.length; i++) {
			if (entradas[i] != null) {
				sb.append(entradas[i]).append('\n');
			}
		}
		return sb.toString();
	}

}
