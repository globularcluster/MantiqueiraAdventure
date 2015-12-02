package wozuul;

import java.util.Set;

import itens.Item;

import java.util.HashMap;
import java.util.Map;

import personagens.Personagem;

/**
 * Class Room - a room in an adventure game.
 *
 * This class is part of the "World of Zuul" application. "World of Zuul" is a
 * very simple, text based adventure game.
 *
 * A "Room" represents one location in the scenery of the game. It is connected
 * to other rooms via exits. For each existing exit, the room stores a reference
 * to the neighboring room.
 * 
 * @author Michael Kolling and David J. Barnes
 * @version 2008.03.30
 */

public class Room {
	private String description;
	private int moedasNoChao;
	private Map<String, Room> exits; // stores exits of this room.
	private Map<String, Personagem> personagens;
	private Map<String, Item> chao;

	/**
	 * Create a room described "description". Initially, it has no exits.
	 * "description" is something like "a kitchen" or "an open court yard".
	 * 
	 * @param description
	 *            The room's description.
	 */
	public Room(String description) {
		this.description = description;
		exits = new HashMap<String, Room>();
		personagens = new HashMap<String, Personagem>();
		chao = new HashMap<String, Item>();
	}

	/**
	 * Define an exit from this room.
	 * 
	 * @param direction
	 *            The direction of the exit.
	 * @param neighbor
	 *            The room to which the exit leads.
	 */
	public void setExit(String direction, Room neighbor) {
		exits.put(direction, neighbor);
	}
	

	/**
	 * @return The short description of the room (the one that was defined in
	 *         the constructor).
	 */
	public String getShortDescription() {
		return description;
	}

	/**
	 * Return a description of the room in the form: You are in the kitchen.
	 * Exits: north west
	 * 
	 * @return A long description of this room
	 */
	public String getLongDescription() {
		String returnString = "Você está " + description + ".\n" + getExitString() + "\n" + pegaPersonagensString();
		
		if(!chao.isEmpty())
			returnString += "\n" + pegaItensChaoString();
		
		return returnString;
	}

	/**
	 * Return a string describing the room's exits, for example
	 * "Exits: north west".
	 * 
	 * @return Details of the room's exits.
	 */
	private String getExitString() {
		String returnString = "Exits:";
		Set<String> keys = exits.keySet();
		for (String exit : keys) {
			returnString += " " + exit;
		}
		return returnString;
	}

	private String pegaPersonagensString() {
		if(personagens.isEmpty())
			return "";
		
		String returnString = "Villains:";
		Set<String> keys = personagens.keySet();
		for (String nome : keys) {
			returnString += " " + nome;
		}
		return returnString;
	}
	
	public String pegaItensChaoString(){
		String returnString = "\nItens no chão: \t";
		Set<String> keys = chao.keySet();
		for (String nome : keys) {
			returnString += nome + " ";
		}
		if(hasMoedasChao()){
			returnString += "\nMoedas: \t" + moedasNoChao;
		}
		return returnString;
	}

	/**
	 * Return the room that is reached if we go from this room in direction
	 * "direction". If there is no room in that direction, return null.
	 * 
	 * @param direction
	 *            The exit's direction.
	 * @return The room in the given direction.
	 */
	public Room getExit(String direction) {
		return exits.get(direction);
	}

	/**
	 * Pega um personagem da sala.
	 * @param nome Nome em string do personagem.
	 * @return O personagem da classe Personagem.
	 */
	public Personagem pegaPersonagem(String nome) {
		return personagens.get(nome);
	}

	/**
	 * Inseri um personagem na sala
	 * @param personagem Personagem a ser inserido.
	 */
	public void inserirPersonagem(Personagem personagem) {
		personagens.put(personagem.pegaNome(), personagem);
	}

	/**
	 * Remove um personagem da sala.
	 * @param personagem Personagem a ser removido.
	 */
	public void removerPersonagem(Personagem personagem) {
		personagens.remove(personagem.pegaNome());
	}

	/**
	 * Inseri uma mochila toda de um Personagem no chão da sala.
	 * Usado quando um personagem morre.
	 * @param mochila Mochila do personagem.
	 */
	public void inserirMochilaChao(HashMap<String, Item> mochila) {
		chao.putAll(mochila);
	}
	
	/**
	 * Dropa um item no chão da sala. Usado quando o herói dropa um item.
	 * @param item Item a ser dropado.
	 */
	public void inserirItemChao(Item item){
		chao.put(item.pegaNome(), item);		
	}

	/**
	 * @return Retorna uma mochila com todos itens no chão.
	 */
	public Map<String, Item> pegarItemChao() {
		return chao;
	}

	/**
	 * Insere moedas no chão
	 * @param n Numero de moedas a ser inserido.
	 */
	public void inserirMoedasChao(int n) {
		moedasNoChao += n;
	}

	/**
	 * Pega todas moedas que estão no chão.
	 * @return Numero que moedas que foi pego.
	 */
	public int pegarMoedasChao() {
		int n = moedasNoChao;
		moedasNoChao = 0;

		return n;
	}

	/**
	 * Verifica se há moedas no chão.
	 * @return true se existe, false se não existe.
	 */
	public boolean hasMoedasChao() {
		if (moedasNoChao == 0)
			return false;
		else
			return true;
	}

	/**
	 * Verifica se há vilões no local
	 * 
	 * @return true se existe, false se está vazio
	 */
	public boolean hasVillains() {
		if (personagens.isEmpty())
			return true;
		else
			return false;
	}
}
