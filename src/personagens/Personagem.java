package personagens;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import itens.Item;

/**
 * Classe abstrata Personagem. Dela se derivam as classes Heroi e Vilao.
 * 
 * @author Wagner F O jr
 */
public abstract class Personagem {
	private String nome;
	private int energia;
	private int moedas;
	protected Map<String, Item> mochila;

	private static Random dado = new Random();
	
	/**
	 * Construtor de Personagem.
	 * @param nome Nome do personagem
	 * @param energia Energia atual que o personagem possui.
	 */
	public Personagem(String nome, int energia) {
		this.nome = nome;
		this.energia = energia;
		this.mochila = new HashMap<String, Item>();
	}
	
	/**
	 * Pega o nome do personagem.
	 * @return Nome do personagem.
	 */
	public String pegaNome() {
		return nome;
	}
	
	/**
	 * Retorna energia atual do personagem.
	 * @return
	 */
	public int pegaEnergia() {
		return energia;
	}
	
	/**
	 * Retorna a quantidade de moedas que o personagem possui
	 * @return
	 */
	public int pegaMoedas(){
		return moedas;
	}
	
	/**
	 * Verifica se o personagem est� no jogo.
	 * @return true se est� morto, false se est� vivo.
	 */
	public boolean estaMorto() {
		if (energia == 0)
			return true;
		else
		    return false;
	}
	
	/**
	 * Metodo abstrato. Retorna a energia m�xima do personagem.
	 * @return
	 */
	public abstract int pegaEnergiaMaxima();
	
	/**
	 * Adiciona um ponto na vida do personagem.
	 */
	public void incremento() {
		if (energia < pegaEnergiaMaxima())
			energia++;
	}
	
	/**
	 * Remove um ponto na vida do personagem.
	 */
	public void decremento() {
		if (energia > 0)
			energia--;
		if (energia == 0)
			System.out.println("\n# " + nome + " esta morto!");
	}
	
	/**
	 * Gera um n�mero randomico para lutar com um oponente, imitando um dado.
	 * @param valorMaximo A quantidade de sorte do personagem, ou a quantidade de lados de seu dado.
	 * @return	Um n�mero gerado aleat�riamente de 0 at� o valorMaximo.
	 */
	public int sorte(int valorMaximo) {
		return dado.nextInt(valorMaximo) + 1;
	}
	
	/**
	 * Imprime os dados do personagem.
	 */
 	public void imprimir() {
		System.out.println("+-------------------------");
		System.out.println("| Nome: " + nome);
		System.out.println("| Energia: " + energia);
	}
 	
 	/**
 	 * Adiciona moedas ao personagem.
 	 * @param n Quantidade de moedas a ser adicionado.
 	 */
 	public void adicionaMoedas(int n){
 		moedas += n;
 	}
 	
 	/**
 	 * Remove todas moedas do personagem. � usado apenas quando um oponente morre.
 	 * @return	Todas moedas que o personagem possui.
 	 */
 	public int removeMoedas(){
 		int n = moedas;
 		moedas = 0;
 		return n;
 	}
 	
 	/**
 	 * M�todo abstrato para inserir um item na mochila de um personagem.
 	 * @param item	Item a ser adicionado.
 	 * @return	true se foi adicionado, false se n�o foi poss�vel.
 	 */
 	public abstract boolean inserirItemMochila(Item item);
 	
 	/**
 	 * Remove um item na mochila do personagem.
 	 * @param nome Nome do item a ser removido.
 	 * @return O item removido.
 	 */
 	public Item removerItemMochila(String nome){
 		Item item = mochila.get(nome);
		if (item != null)
			mochila.remove(nome);
		else
			System.out.println("\n# O item '" + nome + "' nao esta na mochila!\n");
		return item;
 	}
}
