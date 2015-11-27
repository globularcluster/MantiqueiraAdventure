package personagens;

import java.util.Set;

import itens.*;

public class Heroi extends Personagem {
	private int energiaMaxima;
	private int limiteDePeso;
	private boolean cachorro;

	public Heroi(String nome, int energia, int energiaMaxima, int limiteDePeso) {
		super(nome, energia);
		this.energiaMaxima = energiaMaxima;
		this.limiteDePeso = limiteDePeso;
	}

	private int calcularPeso() {
		int pesoTotal = 0;
		for (Item item : mochila.values()) {
			pesoTotal += item.pegaPeso();
		}
		return pesoTotal;
	}

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
	 * 
	 * @param oponente
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
			for(i=0; i<=atkAdicional; i++){
				oponente.decremento();
				
				if(oponente.estaMorto())
					break;
			}
			System.out.println("Você hitou: " + (i+1) + " pontos");
			
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
	 * @return imprime dados do herói.
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
	
	public void adicionaCachorro(){
		cachorro = true;
	}
	
	public void removeCachorro(){
		cachorro = false;
	}

}
