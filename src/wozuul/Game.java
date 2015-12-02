package wozuul;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Stack;

import itens.Instantaneo;
import itens.Item;
import itens.Permanente;
import personagens.Heroi;
import personagens.Personagem;
import personagens.Vilao;

/**
 * Classe principal do jogo. Foi baseado na classe principal do Jogo World of
 * Zuul, criado por Michal Kolling and David J. Barnes, em 2008.03.30
 * 
 * @author Wagner F. de Oliveira Jr
 * @version 1/12/2015
 */

public class Game {
	private Parser parser;
	private Room currentRoom;
	private Heroi heroi;

	private Stack<Personagem> formigas = new Stack<Personagem>();
	private Stack<Personagem> vespas = new Stack<Personagem>();
	private Stack<Personagem> aranhas = new Stack<Personagem>();
	private Stack<Personagem> cobras = new Stack<Personagem>();
	private Stack<Personagem> oncas = new Stack<Personagem>();

	/**
	 * Cria o jogo. Recebe o nome do jogador para servir como nome do herói.
	 * 
	 */
	public Game() {
		createRooms();
		parser = new Parser();

		System.out.println("Digite o nome de seu personagem> ");
		@SuppressWarnings("resource")
		Scanner scanner = new Scanner(System.in);
		String name = scanner.nextLine();

		heroi = new Heroi(name, 10, 10, 100);
	}

	/**
	 * Main play routine. Loops until end of play.
	 */
	public void play() {
		printWelcome();

		// Enter the main command loop. Here we repeatedly read commands and
		// execute them until the game is over.

		boolean finished = false;
		while (!finished) {
			Command command = parser.getCommand();
			finished = processCommand(command);
		}
		System.out.println("Obrigado por jogar.");
	}

	/**
	 * Print out the opening message for the player.
	 */
	private void printWelcome() {
		System.out.println();
		System.out.println("Bem vindo, " + heroi.pegaNome() + ", ao Mantiqueira Adventure! - Vencendo as 7 Quedas.");
		System.out.println("Mantiqueira Adventure é um jogo estremamente simples, sem qualquer \n"
				+ "funcionalidades revolucionárias ou intenção de ser legal, criado \n"
				+ "apenas para aumentar skills em Programação Orientada a Objeto.");
		System.out.println("Escreva '" + CommandWord.HELP + "' se precisar de ajuda.");
		System.out.println();
		printHelp();
		System.out.println(currentRoom.getLongDescription());
	}

	/**
	 * Given a command, process (that is: execute) the command.
	 * 
	 * @param command
	 *            The command to be processed.
	 * @return true If the command ends the game, false otherwise.
	 */
	private boolean processCommand(Command command) {
		boolean wantToQuit = false;

		CommandWord commandWord = command.getCommandWord();

		if (commandWord == CommandWord.UNKNOWN) {
			System.out.println("I don't know what you mean...");
			return false;
		}

		if (commandWord == CommandWord.HELP) {
			printHelp();
		} else if (commandWord == CommandWord.GO) {
			goRoom(command);
		} else if (commandWord == CommandWord.QUIT) {
			wantToQuit = quit(command);
		} else if (commandWord == CommandWord.LOOK) {
			look();
		} else if (commandWord == CommandWord.ATTACK) {
			attack(command);
		} else if (commandWord == CommandWord.PICK) {
			pick(command);
		} else if (commandWord == CommandWord.USE) {
			use(command);
		} else if (commandWord == CommandWord.BACKPACK) {
			heroi.printMochila();
		} else if (commandWord == CommandWord.DROP) {
			drop(command);
		}
		// else command not recognised.
		return wantToQuit;

	}

	// implementations of user commands:

	/**
	 * Print out some help information. Here we print some stupid, cryptic
	 * message and a list of the command words.
	 */
	private void printHelp() {
		System.out.println("Você decidiu escalar a trilha das 7 Quedas com o seu cachorro.");
		System.out.println("Mesmo conhecendo o caminho, pisou em falso ed caiu de um pequeno penhasco.");
		System.out.println("Após algum tempo desacordado, acordou com sua mochila vazia e sem seu cahorro.");
		System.out.println("A saída está a Noroeste! Procure seu cachorro e algumas moedas pelo caminho.");
		System.out.println();
		System.out.println("Seus comando são:");
		parser.showCommands();
		System.out.println();
	}

	/**
	 * Try to go to one direction. If there is an exit, enter the new room,
	 * otherwise print an error message.
	 */
	private void goRoom(Command command) {
		if (!command.hasSecondWord()) {
			// if there is no second word, we don't know where to go...
			System.out.println("Go where?");
			return;
		}

		String direction = command.getSecondWord();

		// Try to leave current room.
		Room nextRoom = currentRoom.getExit(direction);

		if (nextRoom == null) {
			System.out.println("Não existe saída aí!");
		} else {
			currentRoom = nextRoom;
			System.out.println(currentRoom.getLongDescription());
		}
	}

	/**
	 * "Quit" was entered. Check the rest of the command to see whether we
	 * really quit the game.
	 * 
	 * @return true, if this command quits the game, false otherwise.
	 */
	private boolean quit(Command command) {
		if (command.hasSecondWord()) {
			System.out.println("Sair o que?");
			return false;
		} else {
			return true; // signal that we want to quit
		}
	}

	/**
	 * Imprime uma visao detalhada do lugar em que o personagem está.
	 */
	private void look() {
		System.out.println(currentRoom.getLongDescription());
	}

	/**
	 * Analiza o parâmetro para selecionar o personagem a atacar. Se o oponente
	 * estiver morto, os itens de seu inventário caem ao chão, junto com suas
	 * moedas
	 * 
	 * @param command
	 *            Oponente a ser atacado.
	 * 
	 */
	private void attack(Command command) {
		if (!command.hasSecondWord()) {
			// if there is no second word, we don't know where to go...
			System.out.println("Atacar quem?");
			return;
		}

		Personagem vilao = (Vilao) currentRoom.pegaPersonagem(command.getSecondWord());
		if (vilao != null) {

			heroi.lutar(vilao);

			if (vilao.estaMorto()) {
				Map<String, Item> mochilaTemp = ((Vilao) vilao).pegaMochila();
				int moedasTemp = vilao.removeMoedas();

				if (mochilaTemp.isEmpty())
					return;

				currentRoom.inserirMochilaChao((HashMap<String, Item>) mochilaTemp);
				currentRoom.inserirMoedasChao(moedasTemp);
				currentRoom.removerPersonagem(vilao);

				heroi.imprimir();
				System.out.println(currentRoom.pegaItensChaoString());

				return;
			}

			heroi.imprimir();
			vilao.imprimir();

		} else {
			System.out.println("O animal '" + command.getSecondWord() + "' não existe nesse local.");
		}
	}

	/**
	 * Função que pega um item ou moedas no chão do lugar em que está e o coloca
	 * na mochila.
	 * 
	 * @param command
	 *            Item a ser pego
	 */
	private void pick(Command command) {
		if (!command.hasSecondWord()) {
			System.out.println("Pegar o que?");
			return;
		}

		String itemStr = command.getSecondWord();

		if (itemStr.equals("moedas")) {
			if (currentRoom.hasMoedasChao()) {
				int n = currentRoom.pegarMoedasChao();
				heroi.adicionaMoedas(n);
				return;
			} else {
				System.out.println("Não há moedas para pegar!");
				return;
			}
		}

		HashMap<String, Item> itensNoChao = (HashMap<String, Item>) currentRoom.pegarItemChao();
		if (itensNoChao.isEmpty()) {
			System.out.println("Não há itens para pegar!");
			return;
		} else if (!itensNoChao.containsKey(itemStr)) {
			System.out.println("O item " + itemStr + " não está no chão!");
			return;
		} else {
			Item item = itensNoChao.get(itemStr);

			if (item != null) {

				if (item.pegaNome() == "Cachorro") // seta flag de item especial
					heroi.adicionaCachorro();

				if (heroi.inserirItemMochila(item)) // se o item foi adionado o
													// remove do chão
					itensNoChao.remove(itemStr);

				return;
			} else {
				// heroi.inserirItem() printa que não pegou o item
				return;
			}
		}
	}

	/**
	 * Remove um item da mochila e o coloca no chão.
	 * 
	 * @param command
	 */
	private void drop(Command command) {
		if (!command.hasSecondWord()) {
			System.out.println("Drop what?");
			return;
		}

		String itemStr = command.getSecondWord();
		Item item = heroi.removerItemMochila(itemStr);

		if (item != null) {

			if (item.pegaNome() == "Cachorro")
				heroi.removeCachorro();

			currentRoom.inserirItemChao(item);
			System.out.println(currentRoom.pegaItensChaoString());
		} else {
			// heroi.removerItem() printa que não existe item na mochila
		}
	}

	/**
	 * Usa um item instâneo que esteja na mochila do personagem.
	 * 
	 * @param command
	 */
	private void use(Command command) {
		if (!command.hasSecondWord()) {
			System.out.println("Usar o que?");
			return;
		}

		String itemS = command.getSecondWord();
		heroi.utilizar(itemS);
	}

	/**
	 * Metodo main. Cria um jogo e chama seu método play().
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		Game game = new Game();
		game.play();
	}

	/**
	 * Cria as salas do jogo, adiciona personagens e items.
	 */
	private void createRooms() {
		Room queda1, queda2, queda3, queda4, queda5, queda6, queda7, acamps1, acamps2;
		Room inicio, sala1, sala2, sala3, sala4, sala5, sala6, sala7, sala8, sala9, sala10;

		List<Room> salas = new ArrayList<Room>();
		List<Room> quedas = new ArrayList<Room>();

		inicio = new Room("no fundo de um pequeno penhasco");
		queda1 = new Room("na primeira queda");
		quedas.add(queda1);
		queda2 = new Room("na segunda queda");
		quedas.add(queda2);
		queda3 = new Room("na terceira queda");
		quedas.add(queda3);
		queda4 = new Room("na quarta queda");
		quedas.add(queda4);
		queda5 = new Room("na quinta queda");
		quedas.add(queda5);
		queda6 = new Room("na sexta queda");
		quedas.add(queda6);
		queda7 = new Room("na sétima e última queda");
		quedas.add(queda7);
		sala1 = new Room("a caminho da primeira queda");
		salas.add(sala1);
		sala2 = new Room("onde seu cachorro está! Pegue-o para te acompanhar");
		salas.add(sala2);
		sala3 = new Room("a caminho da segunda queda");
		salas.add(sala3);
		sala4 = new Room("a caminho da terceira queda");
		salas.add(sala4);
		sala5 = new Room("a caminho da quarta queda");
		salas.add(sala5);
		sala6 = new Room("a caminho da quarta queda");
		salas.add(sala6);
		sala7 = new Room("sem saída!");
		salas.add(sala7);
		sala8 = new Room("a caminho da sexta queda");
		salas.add(sala8);
		sala9 = new Room("a caminho da sétima queda");
		salas.add(sala9);
		sala10 = new Room("a caminho da sétima queda");
		salas.add(sala10);
		acamps1 = new Room("no primeiro lugar para acampar, procure algum item");
		acamps2 = new Room("no segundo lugar para acampar, procure algum item");

		inicio.setExit("oeste", sala1);
		queda1.setExit("norte", sala3);
		queda1.setExit("sul", sala1);
		queda2.setExit("norte", sala4);
		queda2.setExit("leste", sala3);
		queda3.setExit("leste", sala5);
		queda3.setExit("oeste", sala4);
		queda4.setExit("oeste", sala6);
		queda4.setExit("leste", sala5);
		queda5.setExit("leste", acamps2);
		queda5.setExit("sul", sala6);
		queda6.setExit("norte", sala9);
		queda6.setExit("leste", sala8);
		queda7.setExit("sul", sala10);

		acamps1.setExit("oeste", sala4);
		acamps1.setExit("leste", sala7);
		acamps2.setExit("norte", sala8);
		acamps2.setExit("oeste", queda5);

		sala1.setExit("norte", queda1);
		sala1.setExit("leste", inicio);
		sala2.setExit("oeste", sala3);
		sala3.setExit("leste", sala2);
		sala3.setExit("oeste", queda2);
		sala3.setExit("sul", queda1);
		sala4.setExit("oeste", acamps1);
		sala4.setExit("sul", queda2);
		sala4.setExit("leste", queda3);
		sala5.setExit("norte", queda4);
		sala5.setExit("oeste", queda3);
		sala6.setExit("norte", queda5);
		sala6.setExit("leste", queda4);
		sala7.setExit("oeste", acamps2);
		sala8.setExit("oeste", queda6);
		sala8.setExit("sul", acamps2);
		sala9.setExit("norte", sala10);
		sala9.setExit("sul", queda6);
		sala10.setExit("norte", queda7);
		sala10.setExit("sul", sala9);

		createVillains();

		// salas
		for (int i = 0; i < 10; i++) {
			Room room = salas.get(i);
			room.inserirPersonagem(formigas.pop());
			room.inserirPersonagem(vespas.pop());
		}

		// quedas
		for (int i = 0; i < 7; i++) {
			Room room = quedas.get(i);
			room.inserirPersonagem(formigas.pop());

			if (i == 0) // a partir da segunda queda...
				continue;
			room.inserirPersonagem(aranhas.pop());

			if (i == 1) // a partir da terceira queda...
				continue;
			room.inserirPersonagem(oncas.pop());
		}

		// acamps
		acamps1.inserirPersonagem(vespas.pop());
		acamps1.inserirPersonagem(vespas.pop());
		acamps1.inserirPersonagem(aranhas.pop());
		acamps1.inserirPersonagem(cobras.pop());
		acamps2.inserirPersonagem(vespas.pop());
		acamps2.inserirPersonagem(vespas.pop());
		acamps2.inserirPersonagem(aranhas.pop());
		acamps2.inserirPersonagem(cobras.pop());

		// ITEM ESPECIAL
		sala2.inserirItemChao(new Permanente("Cachorro", "Seu fiel amigo. (+1 dano extra)", 0, 1, 0));
		acamps2.inserirItemChao(new Permanente("blusa", "Uma blusa. (+3 defesa)", 40, 0, 3));
		
		currentRoom = inicio;
	}

	/**
	 * Cria os openentes do jogo, para ser adicionado nas salas.
	 */
	private void createVillains() {

		// um para cada sala e cada queda
		for (int i = 0; i < 17; i++) {
			Personagem formiga = new Vilao("formiga", 2, 1);
			formiga.adicionaMoedas(50);
			((Vilao) formiga).inserirItemMochila(new Instantaneo("agua", "um gole de água. (+1 vida)", 10, 1));

			formigas.push(formiga);
		}

		// um para cada sala e dois para ccda acamps
		for (int i = 0; i < 14; i++) {
			Personagem vespa = new Vilao("vespa", 5, 2);
			vespa.adicionaMoedas(100);
			((Vilao) vespa).inserirItemMochila(new Instantaneo("banana", "Uma banana. (+2 vida)", 20, 2));
			((Vilao) vespa).inserirItemMochila(new Permanente("graveto", "Um graveto. (+2 ataque)", 30, 2, 0));

			vespas.push(vespa);
		}

		// da segunda a setima queda e acamps
		for (int i = 0; i < 8; i++) {
			Personagem aranha = new Vilao("aranha", 10, 3);
			aranha.adicionaMoedas(150);
			((Vilao) aranha).inserirItemMochila(new Instantaneo("banana", "Uma banana. (+2 vida)", 20, 2));

			aranhas.push(aranha);
		}

		// cada acamps
		for (int i = 0; i < 2; i++) {
			Personagem cobra = new Vilao("cobra", 15, 4);
			cobra.adicionaMoedas(200);
			((Vilao) cobra).inserirItemMochila(new Permanente("cajado", "Um cajado. (+5 ataque)", 40, 5, 0));
			((Vilao) cobra).inserirItemMochila(new Instantaneo("agua", "um gole de água. (+1 vida)", 10, 1));

			cobras.push(cobra);
		}

		// da terceira a setima queda
		for (int i = 0; i < 5; i++) {
			Personagem onca = new Vilao("onca", 25, 10);
			onca.adicionaMoedas(300);
			((Vilao) onca).inserirItemMochila(new Instantaneo("peixe", "Um peixe. (+3 vida)", 30, 3));
			((Vilao) onca).inserirItemMochila(new Permanente("bota", "Uma bota. (+1 defesa)", 50, 0, 1));

			oncas.push(onca);
		}

	}

}