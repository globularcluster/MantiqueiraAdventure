package itens;

public class Permanente extends Item {

	private int attack;
	private int defence;

	public Permanente(String nome, String descricao, int peso, int att, int def) {
		super(nome, descricao, peso);
		attack = att;
		defence = def;
	}

	public int getAttack() {
		return attack;
	}

	public int getDefence() {
		return defence;
	}
}
