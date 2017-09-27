package com.ufla.map;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

public class MapaConcretoTeste {

	@Test
	public void teste1() {
		Mapa<String, Set<String>> mapa = new MapaConcreto<>();
		String[] disciplinas1Str = { "LinPro3", "ArqSof", "Comp" };
		String[] disciplinas2Str = { "EngSof", "ArqComp", "PAA" };
		Set<String> disciplinas1 = new HashSet<>(
				Arrays.asList(disciplinas1Str));
		Set<String> disciplinas2 = new HashSet<>(
				Arrays.asList(disciplinas2Str));
		mapa.put("Disciplinas1", disciplinas1);
		mapa.put("Disciplinas2", disciplinas2);

		assertEquals(disciplinas1, mapa.get("Disciplinas1"));
		assertEquals(disciplinas2, mapa.get("Disciplinas2"));
		assertEquals(null, mapa.get("Disciplinas3"));
	}

	@Test
	public void teste2() {
		Mapa<Integer, String> mapa = new MapaConcreto<>();
		mapa.put(1, "LinPro3");
		mapa.put(2, "ArqSoft");
		assertEquals("LinPro3", mapa.get(1));
		assertEquals("ArqSoft", mapa.get(2));
		assertEquals(null, mapa.get(-1));
		mapa.put(2, "PAA");
		assertEquals("PAA", mapa.get(2));
	}

}
