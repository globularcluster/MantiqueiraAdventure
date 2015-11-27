package wozuul;

import java.io.BufferedReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import itens.*;
import itens.Permanente;
import personagens.Heroi;
import personagens.Personagem;
import personagens.Vilao;

/**
 * This class is the main class of the "World of Zuul" application.
 * "World of Zuul" is a very simple, text based adventure game. Users can walk
 * around some scenery. That's all. It should really be extended to make it more
 * interesting!
 * 
 * To play this game, create an instance of this class and call the "play"
 * method.
 * 
 * This main class creates and initialises all the others: it creates all rooms,
 * creates the parser and starts the game. It also evaluates and executes the
 * commands that the parser returns.
 * 
 * @author Michael Kolling and David J. Barnes
 * @version 2008.03.30
 */

public class Game {
	private Parser parser;
	private Room currentRoom;
	private Heroi heroi;

	/**
	 * Create the game and initialise its internal map.
	 */
	public Game() {
		createRooms();
		parser = new Parser();
		
		System.out.println("Digite seu nome> ");
		Scanner scanner = new Scanner(System.in);
		String name = scanner.nextLine();
		
		heroi = new Heroi(name, 10, 10, 100);
	}

	/**
	 * Create all the rooms and link their exits together.
	 */
	private void createRooms() {
		Room outside, theatre, pub, lab, office;

		// create the rooms
		outside = new Room("outside the main entrance of the university");
		theatre = new Room("in a lecture theatre");
		pub = new Room("in the campus pub");
		lab = new Room("in a computing lab");
		office = new Room("in the computing admin office");

		// initialise room exits
		outside.setExit("east", theatre);
		outside.setExit("south", lab);
		outside.setExit("west", pub);

		theatre.setExit("west", outside);

		pub.setExit("east", outside);

		lab.setExit("north", outside);
		lab.setExit("east", office);
		Personagem capanga1 = new Vilao("c1", 1, 1);
		Personagem capanga2 = new Vilao("c2", 5, 3);
		capanga1.adicionaMoedas(50);
		((Vilao) capanga1).inserirItemMochila(new Permanente("espada", "espada foda",10, 2, 0));
		((Vilao) capanga1).inserirItemMochila(new Instantaneo("agua", "agua da boa", 1, 2));

		outside.inserirPersonagem(capanga1);
		outside.inserirPersonagem(capanga2);
		outside.inserirItemChao(new Permanente("Cachorro", "seu fiel amigo", 0, 0, 0));

		office.setExit("west", lab);
		Personagem coringa = new Vilao("Coringa", 5, 5);
		office.inserirPersonagem(coringa);

		currentRoom = outside; // start game outside
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
		System.out.println("Thank you for playing.  Good bye.");
	}

	/**
	 * Print out the opening message for the player.
	 */
	private void printWelcome() {
		System.out.println();
		System.out.println("Bem vindo ao Mantiqueira Adventure! - Vencendo as 7 Quedas");
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
			System.out.println("There is no door!");
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
			System.out.println("Quit what?");
			return false;
		} else {
			return true; // signal that we want to quit
		}
	}

	private void look() {
		System.out.println(currentRoom.getLongDescription());
	}

	private void attack(Command command) {
		if (!command.hasSecondWord()) {
			// if there is no second word, we don't know where to go...
			System.out.println("Attack who?");
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

	private void pick(Command command) {
		if (!command.hasSecondWord()) {
			System.out.println("Pick what?");
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
				
				if(item.pegaNome() == "Cachorro")
					heroi.adicionaCachorro();
				
				heroi.inserirItemMochila(item);
				itensNoChao.remove(itemStr);
				return;
			} else {
				// heroi.inserirItem() printa que não pegou o item
				return;
			}
		}
	}

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

	private void use(Command command) {
		if (!command.hasSecondWord()) {
			System.out.println("Use what?");
			return;
		}

		String itemS = command.getSecondWord();
		heroi.utilizar(itemS);
	}

	public static void main(String[] args) {
		Game game = new Game();
		game.play();
	}
}
