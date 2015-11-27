package personagens;

import java.util.HashMap;
import java.util.Map;

import itens.Item;

public class Vilao extends Personagem {
	private static final int energiaMaxima = 7;
	private int ataque;

	public Vilao(String nome, int energia, int atk) {
		super(nome, energia);
		ataque = atk;
	}
	
	public int pegaEnergiaMaxima() {
		return energiaMaxima;
	}
	
	public int pegaAtaque(){
		return ataque;
	}
	
	public void imprimir() {
		System.out.println("+-------------------------");
		System.out.println("| Dados do oponente");
		super.imprimir();
	}
	
	public Map<String, Item> pegaMochila(){
		Map<String, Item> mochilaTemp = new HashMap<String, Item>();
		mochilaTemp.putAll(mochila);
		mochila.clear();
		
		return mochilaTemp;
	}
	
	public boolean inserirItemMochila(Item item){
		mochila.put(item.pegaNome(), item);
		return true;
	}
}
