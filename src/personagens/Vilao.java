package personagens;

import java.util.HashMap;
import java.util.Map;

import itens.Item;

/**
 * Classe  Vilao. Derivada da classe Personagem, implementa os oponentes do jogo.
 * @author Wagner
 *
 */
public class Vilao extends Personagem {
	private static final int energiaMaxima = 7;
	private int ataque;

	/**
	 * Construtor.
	 * @param nome Nome do oponetne.
	 * @param energia Energia atual do oponente.
	 * @param atk Poder de ataque do oponente.
	 */
	public Vilao(String nome, int energia, int atk) {
		super(nome, energia);
		ataque = atk;
	}
	
	/**
	 * Retorna a energia máxima do oponente.
	 */
	public int pegaEnergiaMaxima() {
		return energiaMaxima;
	}
	
	/**
	 * @return Retorna o poder de ataque do oponente.
	 */
	public int pegaAtaque(){
		return ataque;
	}
	
	/**
	 * Imprime dados do oponente.
	 */
	public void imprimir() {
		System.out.println("+-------------------------");
		System.out.println("| Dados do oponente");
		super.imprimir();
	}
	
	/**
	 * Pega a mochila do oponente. Utilizado quando o mesmo morre.
	 * @return	Mochila do oponente.
	 */
	public Map<String, Item> pegaMochila(){
		Map<String, Item> mochilaTemp = new HashMap<String, Item>();
		mochilaTemp.putAll(mochila);
		mochila.clear();
		
		return mochilaTemp;
	}
	
	/**
	 * Inseri um item na mochila.
	 */
	public boolean inserirItemMochila(Item item){
		mochila.put(item.pegaNome(), item);
		return true;
	}
}
