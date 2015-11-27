package personagens;

public class SimuladorDeBatalha {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int round = 1;
		
		Heroi batman = new Heroi("Batman", 8, 10, 100);
		Vilao coringa = new Vilao("Coringa", 7, 1);
		
		while (!batman.estaMorto() && !coringa.estaMorto()) {
			System.out.println("\n*** Round: " + round + "***");
			batman.imprimir();
			coringa.imprimir();
			batman.lutar(coringa);
			++round;
		}
	}

}
