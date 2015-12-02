package personagens;

import java.util.Set;

import itens.*;

/**
 * Classe Herói. O personagem que o jogador irá controlar através do jogo.
 * @author Wagner
 */
public class Heroi extends Personagem {
	private int energiaMaxima;
	private int limiteDePeso;
	private boolean cachorro;

	/**
	 * Construtor do Herói.
	 * @param nome	Nome do personagem.
	 * @param energia	Energia atual do personagem.
	 * @param energiaMaxima Energia máxima do Herói.	
	 * @param limiteDePeso	Seu limite de peso.
	 */
	public Heroi(String nome, int energia, int energiaMaxima, int limiteDePeso) {
		super(nome, energia);
		this.energiaMaxima = energiaMaxima;
		this.limiteDePeso = limiteDePeso;
	}

	/**
	 * Calcula o peso atual da mochila.
	 * @return Inteiro informando o peso.
	 */
	private int calcularPeso() {
		int pesoTotal = 0;
		for (Item item : mochila.values()) {
			pesoTotal += item.pegaPeso();
		}
		return pesoTotal;
	}

	/**
	 * Inseri um item na mochila do Herói verificando o peso atual e o peso máximo.
	 * @param item Item a ser adicionado.
	 * @return true se foi adicionado, false se não foi possível.
	 */
	public boolean inserirItemMochila(Item item) {
		if (calcularPeso() + item.pegaPeso() <= limiteDePeso) {
			mochila.put(item.pegaNome(), item);
			return true;
		} else {
			System.out.println("\n# O " + pegaNome() + " nao pode carregar mais itens na mochila!\n");
			return false;
		}
	}


	/**
	 * Utiliza um item instantâneo
	 * @param nome Nome do item
	 */
	public void utilizar(String nome) {

		Item item = mochila.get(nome);

		if (item != null && item instanceof Instantaneo) {
			if (this.pegaEnergia() == this.pegaEnergiaMaxima()) {
				System.out.println("Sua energia já está cheia!");
				return;
			}
			
			Instantaneo itemInstant = (Instantaneo) item;
			int count =0;	// conta o número de energia que foi aumentado
			for (int i = 0; i < itemInstant.pegaEnergiaItem() && this.pegaEnergia() < this.pegaEnergiaMaxima(); i++){
				this.incremento();
				count++;
			}
			System.out.println("Você recuperou " + count + " pontos de energia.");
			this.removerItemMochila(nome);
		} else {
			System.out.println("Não foi possível utilizar esse item!");
		}
	}
	
	/**
	 * Printa todos itens na mochila.
	 */
	public void printMochila(){
		String returnString = "Mochila: ";
		Set<String> keys = mochila.keySet();
		for(String itens : keys){
			returnString += " " + itens;
		}
		returnString += "\nMoedas: " + pegaMoedas();
		System.out.println(returnString);
	}

	/**
	 * Realiza um ataque no oponente com base nos dados de cada um.
	 * Se os dados impatam, a vida de ambos é decrementada.
	 * Se o herói vence, sua vida é incrementada e o oponente é decrementada. Se o oponente morre, seus
	 * itens e moedas vão ao chão.
	 * Se o oponente vence, sua vida é incrementada e do herói decrementada.
	 * 
	 * @param oponente Oponente a ser atacado.
	 */
	public void lutar(Personagem oponente) {
		int dadoDoHeroi = sorte(6);
		int dadoDoOponente = sorte(1);
		int atkAdicional = ataqueAdicional();
		System.out.println("ataque adicional: " + atkAdicional);

		if (dadoDoHeroi == dadoDoOponente) {
			System.out.println("Empate!");
			
			decremento(); 
			oponente.decremento();
			
		} else if (dadoDoHeroi > dadoDoOponente) {
			incremento(); 
			
			int i;
			for(i=1; i<=atkAdicional; i++){
				oponente.decremento();
				
				if(oponente.estaMorto())
					break;
			}
			System.out.println("Você hitou: " + (i) + " pontos");
			
		} else { // Quando o oponente vence
			oponente.incremento();
			int dano = 0;
			
			dano += ((Vilao) oponente).pegaAtaque();
			dano -= defesaAdicional();

			int i;
			for(i=0; i<dano; i++){
				decremento();
			}
			
			System.out.println("Você perdeu: " + (i+1) + " pontos.");
		}
		
		if(cachorro && !oponente.estaMorto()){
			int dadoDoCachorro = sorte(6);
			if(dadoDoCachorro > dadoDoOponente){
				oponente.decremento();
				System.out.println("cachorro hitou.");
			}
		}
	}
	
	/**
	 * Imprime dados do herói.
	 * @return 
	 */
	public void imprimir() {
		System.out.println("\n+-------------------------");
		System.out.println("| Dados do seu Personagem");
		super.imprimir();
	}

	/**
	 * @return retorna a energia máxima do herói
	 */
	public int pegaEnergiaMaxima() {	
		return energiaMaxima;
	}
	
	/**
	 * Calcula quanto dano vale o hit do herói de acordo com os itens permanentes e com 
	 * pontos de ataque que estão na mochila
	 * 
	 * @return valor do ataque
	 */
	private int ataqueAdicional(){
		int atk = 0;
		Set<String> keys = mochila.keySet();
		
		for (String key : keys) {
			Item item = mochila.get(key);
			
			if(item instanceof Instantaneo || item.pegaNome().equals("cachorro"))
				continue;
			else if(item instanceof Permanente)			
				atk += ((Permanente) item).getAttack();
		}
		
		return atk;
	}
	
	/**
	 * Calcula quantos pontos de defesa o herói possui, de acordo com os itens permanentes e com
	 * pontos de defesa que estão na mochila
	 * 
	 * @return valor da defesa
	 */
	private int defesaAdicional(){
		int def = 0;
		Set<String> keys = mochila.keySet();
		
		for (String key : keys) {
			Item item = mochila.get(key);
			
			if(item instanceof Instantaneo)
				continue;
			
			def += ((Permanente) item).getDefence();
		}
		
		return def;
	}
	
	/**
	 * Se o herói pega seu cachorro.
	 */
	public void adicionaCachorro(){
		cachorro = true;
	}
	
	/**
	 * Se o herói dropa seu cachorro.
	 */
	public void removeCachorro(){
		cachorro = false;
	}

}
