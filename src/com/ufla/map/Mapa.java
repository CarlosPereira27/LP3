package com.ufla.map;

public interface Mapa<C, V> {

	public V get(C chave);
	public void set(C chave, V valor);
}
