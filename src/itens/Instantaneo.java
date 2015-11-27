package itens;

public class Instantaneo extends Item{
	
	private int energia;

	public Instantaneo(String nome, String descricao, int peso, int energ) {
		super(nome, descricao, peso);
		energia = energ;
	}
	
	public int pegaEnergiaItem(){
		return energia;
	}
}
