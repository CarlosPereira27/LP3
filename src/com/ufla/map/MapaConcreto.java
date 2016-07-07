package com.ufla.map;

class No<C, V> {
	
	private Object chave;
	private Object valor;
	
	public No(C chave, V valor) {
		this.chave = chave;
		this.valor = valor;
	}
	
	@SuppressWarnings("unchecked")
	public C getChave() {
		return (C) chave;
	}
	public void setChave(C chave) {
		this.chave = chave;
	}
	@SuppressWarnings("unchecked")
	public V getValor() {
		return (V) valor;
	}
	public void setValor(V valor) {
		this.valor = valor;
	}
	
	@Override
	public String toString() {
		return chave+";"+valor;
	}
	
}

public class MapaConcreto<C, V> implements Mapa<C, V> {
	
	private No<C, V>[] entradas;
	private double limite;
	private int tamanhoMaximo;
	private int qtdEntradas;
	
	public MapaConcreto() {
		this(10, 0.7);
	}
	
	@SuppressWarnings("unchecked")
	public MapaConcreto(int tamanho, double limite) {
		if(tamanho < 5) {
			tamanho = 5;
		} 
		if(limite < 0.45 ||limite > 0.8) {
			limite = 0.70;
		}
		this.entradas = new No[tamanho];
		this.limite = limite;
		this.tamanhoMaximo = (int) (tamanho * limite);
		this.qtdEntradas = 0;
	}

	@Override
	public V get(C chave) {
		if(chave == null) {
			return null;
		}
		int posicao = hash(chave);
		if(entradas[posicao] != null && 
				entradas[posicao].getChave().equals(chave)) {
			return entradas[posicao].getValor();
		} else {
			posicao++;
			while(posicao < entradas.length) {
				if(entradas[posicao] != null && 
						entradas[posicao].getChave().equals(chave)) {
					return entradas[posicao].getValor();
				}
				posicao++;
			}
			posicao = 0;
			int hash = hash(chave);
			while(posicao < hash) {
				if(entradas[posicao] != null && 
						entradas[posicao].getChave().equals(chave)) {
					return entradas[posicao].getValor();
				}
				posicao++;
			}
		}
		return null;
	}

	@Override
	public void set(C chave, V valor) {
		if(chave == null) {
			return;
		}
		int posicao = hash(chave);
		if(entradas[posicao] == null) {
			entradas[posicao] = new No<C, V>(chave, valor);
			qtdEntradas++;
		} else if(entradas[posicao].getChave().equals(chave)) {
			entradas[posicao] = new No<C, V>(chave, valor);
		} else {
			boolean set = false;
			posicao++;
			while(!set && posicao < entradas.length) {
				if(entradas[posicao] == null) {
					entradas[posicao] = new No<C, V>(chave, valor);
					qtdEntradas++;
					set = true;
				} else if(entradas[posicao].getChave().equals(chave)) {
					entradas[posicao] = new No<C, V>(chave, valor);
					set = true;
				}
				posicao++;
			}
			posicao = 0;
			int hash = hash(chave);
			while(!set && posicao < hash) {
				if(entradas[posicao] == null) {
					entradas[posicao] = new No<C, V>(chave, valor);
					qtdEntradas++;
					set = true;
				} else if(entradas[posicao].getChave().equals(chave)) {
					entradas[posicao] = new No<C, V>(chave, valor);
					set = true;
				}
				posicao++;
			}
		}
		if(qtdEntradas == tamanhoMaximo) {
			redimensionar();
		}
	}
	
	@SuppressWarnings("unchecked")
	private void redimensionar() {
		No<C, V>[] entradasAntigas = entradas;
		qtdEntradas = 0;
		tamanhoMaximo = (int) (entradasAntigas.length*2*limite);
		entradas = new No[entradasAntigas.length*2];
		
		for(int i = 0; i < entradasAntigas.length; i++) {
			if(entradasAntigas[i] != null) {
				set(entradasAntigas[i].getChave(), entradasAntigas[i].getValor());
			}
		}
	}
	
	private int hash(Object chave) {
		return Math.abs(chave.hashCode()) % entradas.length;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < entradas.length; i++) {
			if(entradas[i] != null) {
				sb.append(entradas[i]).append('\n');
			}
		}
		return sb.toString();
	}

}
