package personagens;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import itens.Item;

public abstract class Personagem {
	private String nome;
	private int energia;
	private int moedas;
	protected Map<String, Item> mochila;

	private static Random dado = new Random();
	
	public Personagem(String nome, int energia) {
		this.nome = nome;
		this.energia = energia;
		this.mochila = new HashMap<String, Item>();
	}
	
	public String pegaNome() {
		return nome;
	}
	
	public int pegaEnergia() {
		return energia;
	}
	
	public int pegaMoedas(){
		return moedas;
	}
	
	public boolean estaMorto() {
		if (energia == 0)
			return true;
		else
		    return false;
	}
	
	public abstract int pegaEnergiaMaxima();
	
	public void incremento() {
		if (energia < pegaEnergiaMaxima())
			energia++;
	}
	
	public void decremento() {
		if (energia > 0)
			energia--;
		if (energia == 0)
			System.out.println("\n# " + nome + " esta morto!");
	}
	
	public int sorte(int valorMaximo) {
		return dado.nextInt(valorMaximo) + 1;
	}
	
 	public void imprimir() {
		System.out.println("+-------------------------");
		System.out.println("| Nome: " + nome);
		System.out.println("| Energia: " + energia);
	}
 	
 	public void adicionaMoedas(int n){
 		moedas += n;
 	}
 	
 	public int removeMoedas(){
 		int n = moedas;
 		moedas = 0;
 		return n;
 	}
 	
 	public abstract boolean inserirItemMochila(Item item);
 	
 	public Item removerItemMochila(String nome){
 		Item item = mochila.get(nome);
		if (item != null)
			mochila.remove(nome);
		else
			System.out.println("\n# O item '" + nome + "' nao esta na mochila!\n");
		return item;
 	}
}
