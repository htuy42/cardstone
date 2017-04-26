package game;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import com.google.gson.JsonObject;

import cardgamelibrary.AuraCard;
import cardgamelibrary.Board;
import cardgamelibrary.Card;
import cardgamelibrary.Creature;
import cardgamelibrary.Element;
import cardgamelibrary.Jsonifiable;
import cardgamelibrary.OrderedCardCollection;
import cardgamelibrary.PlayableCard;
import cardgamelibrary.SpellCard;
import cardgamelibrary.Zone;
import cards.templates.TargetsOtherCard;
import cards.templates.TargetsPlayer;
import events.CardPlayedEvent;
import events.CreatureAttackEvent;
import events.PlayerAttackEvent;
import events.TurnEndEvent;
import server.CommsWebSocket;

/**
 * Class to represent a game.
 *
 * @author Raghu
 *
 */
public class Game implements Jsonifiable {
	private static final int			PLAYER_START_LIFE	= 30;
	private Board									board;
	private Player								playerOne;
	private Player								playerTwo;
	private int										id;
	private static AtomicInteger	idGenerator				= new AtomicInteger(0);
	private static final String		PACKAGE_PATH			= "cards.";

	public Game(List<String> firstPlayerCards, List<String> secondPlayerCards, int playerOneId, int playerTwoId) {
		// Initialize both players with starting life.
		playerOne = new Player(PLAYER_START_LIFE, PlayerType.PLAYER_ONE, playerOneId);
		playerTwo = new Player(PLAYER_START_LIFE, PlayerType.PLAYER_TWO, playerTwoId);

		// set game id.
		this.id = idGenerator.incrementAndGet();

		// build decks from the lists of cards.
		OrderedCardCollection deckOne = new OrderedCardCollection(Zone.DECK, playerOne);
		OrderedCardCollection deckTwo = new OrderedCardCollection(Zone.DECK, playerTwo);

		// format the card names from the lists.
		for (String name : firstPlayerCards) {
			name.replaceAll("[^A-Za-z0-9]", "");
		}

		for (String name : secondPlayerCards) {
			name.replaceAll("[^A-Za-z0-9]", "");
		}

		// for first player
		List<PlayableCard> fCards = new ArrayList<>();
		// for second player
		List<PlayableCard> sCards = new ArrayList<>();

		// but how do we register players for all the cards?

		// maybe take in list of card names? Can invoke constructors like so.
		// some card names won't be class names b/c they have punctuation/long names
		// so will probably have to build some sort of map for this.
		// yeah judging by the number of exceptions this is probably no good lmao.
		try {
			// so declare players, then loop over lists of strings for card names,
			// invoking constructors as we go and adding to new list before adding all
			// to the OrderedCardCollections?
			for (String formattedName : firstPlayerCards) {
				Object p = Class.forName(PACKAGE_PATH + formattedName).getConstructor(Player.class).newInstance(playerOne);
				if (p instanceof Creature) {
					fCards.add((Creature) p);
				} else if (p instanceof AuraCard) {
					fCards.add((AuraCard) p);
				} else if (p instanceof SpellCard) {
					fCards.add((SpellCard) p);
				} else if (p instanceof Element) {
					fCards.add((Element) p);
				} else {
					throw new RuntimeException(
							"ERROR: Some sort of invalid card was trying to be added to Player One's deck. Name is : "
									+ formattedName);
				}
			}

			// repeat process with player two's deck.
			for (String formattedName : secondPlayerCards) {
				Object p = Class.forName(PACKAGE_PATH + formattedName).getConstructor(Player.class).newInstance(playerTwo);
				if (p instanceof Creature) {
					sCards.add((Creature) p);
				} else if (p instanceof AuraCard) {
					sCards.add((AuraCard) p);
				} else if (p instanceof SpellCard) {
					sCards.add((SpellCard) p);
				} else if (p instanceof Element) {
					sCards.add((Element) p);
				} else {
					throw new RuntimeException(
							"ERROR: Some sort of invalid card was trying to be added to Player Two's deck. Name is : "
									+ formattedName);
				}
			}

		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RuntimeException e) {
			System.out.println(e.getMessage());
		}

		// add cards to decks.
		deckOne.addAll(fCards);
		deckTwo.addAll(sCards);

		// shuffle decks.
		deckOne.shuffle();
		deckTwo.shuffle();

		// Some sort of board constructor goes here.
		board = new Board(deckOne, deckTwo);
	}

	public void setBoard(Board board) {
		this.board = board;
	}

	public void startGame() {
		while (playerOne.getLife() > 0 && playerTwo.getLife() > 0) {
			// if neither player has 0 life the game goes on.

		}
		if (playerOne.getLife() <= 0 && playerTwo.getLife() > 0) {
			System.out.println("Player One loses.");
		} else if (playerTwo.getLife() <= 0 && playerOne.getLife() > 0) {
			System.out.println("Player Two loses.");
		} else {
			System.out.println("Game is drawn.");
		}
	}

	/**
	 * Gets the id of the game.
	 *
	 * @return the game id.
	 */
	public int getId() {
		return id;
	}

	/**
	 * Checks to see if a player with a certain Id is in the game.
	 *
	 * @param playerId
	 *          the id of the player we are looking for.
	 * @return a boolean that represents whether a player with the input id is in
	 *         the game.
	 */
	public boolean inGame(int playerId) {
		return (playerOne.getId() == playerId) || (playerTwo.getId() == playerId);
	}

	/**
	 * Checks to see if it's a player with a certain id's turn.
	 *
	 * @param playerId
	 *          the player id.
	 * @return a boolean representing whether the the player with the input id is
	 *         the current active player. Outputs false if the input playerId is
	 *         not in the game.
	 */
	public boolean isTurn(int playerId) {
		return inGame(playerId) && board.getActivePlayer().getId() == playerId;
	}

	/**
	 * Used to send a message to a player saying their action was valid.
	 *
	 * @param playerId
	 *          the id of the player we are sending the message to.
	 */
	private void sendPlayerActionGood(int playerId) {
		try {
			CommsWebSocket.sendActionOk(playerId);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Used to send messages to players telling them their actions are bad.
	 *
	 * @param playerId
	 *          the id of the player we are sending a message to.
	 * @param message
	 *          the message we are sending them.
	 */
	private void sendPlayerActionBad(int playerId, String message) {
		try {
			CommsWebSocket.sendActionBad(playerId, message);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void sendWholeBoardToBoth() {
		try {
			CommsWebSocket.sendWholeBoardSate(this, playerOne.getId());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			CommsWebSocket.sendWholeBoardSate(this, playerTwo.getId());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Handles receiving turn end inputs from front end.
	 *
	 * @param userInput
	 *          a JsonObject representing the user's input.
	 * @param playerId
	 *          the id of the player who sent the action.
	 */
	public void handleTurnend(JsonObject userInput, int playerId) {
		if (!(isTurn(playerId))) {
			// player acting out of turn.
			sendPlayerActionBad(playerId, "Acting out of turn.");
		} else {
			sendPlayerActionGood(playerId);
			TurnEndEvent event = new TurnEndEvent(board.getActivePlayer());
			board.takeAction(event);
			// send board to both players.
			sendWholeBoardToBoth();
		}
	}

	/**
	 * Handles receiving card targeted inputs from front end.
	 *
	 * @param userInput
	 *          a JsonObject representing the user's input.
	 * @param playerId
	 *          the id of the player who sent the action.
	 */
	public void handleCardTargeted(JsonObject userInput, int playerId) {
		if (isTurn(playerId)) {

			Card targetter = board.getCardById(userInput.get("IID1").getAsInt());
			Card targetee = board.getCardById(userInput.get("IID2").getAsInt());

			// check to see if we can pay the card's cost.
			if (!(board.getActivePlayer().validateCost(targetter.getCost()))) {
				sendPlayerActionBad(playerId, "Cannot play card, insufficient resources.");
				return;
			}

			if ((targetter instanceof Creature) && (targetee instanceof Creature)) {
				// in this case we have an attack.

				Creature attacker = (Creature) targetter;
				Creature target = (Creature) targetee;

				if (attacker.getOwner().getId() != playerId) {
					sendPlayerActionBad(playerId, "Can't attack using an opponents creature!");
					return;
				}

				if (target.getOwner().getId() == playerId) {
					sendPlayerActionBad(playerId, "Can't attack your own creature!");
					return;
				}

				// at this point we know the attacker belongs to the player who sent the
				// action and the target
				// belongs to the player who didn't send the action (the opponent).

				if (!(attacker.canAttack())) {
					sendPlayerActionBad(playerId, "Creature can't attack anymore!");
					return;
				}

				// at this point we know the event is valid.
				CreatureAttackEvent event = new CreatureAttackEvent(attacker, target);

				// tell player their action was valid.
				sendPlayerActionGood(playerId);

				// execute action on the board.
				board.takeAction(event);

				// send board to both players.
				sendWholeBoardToBoth();
			} else if (targetter instanceof TargetsOtherCard) {
				// we have some sort of card on card action here son.

				// the targetted card wasn't a valid target.
				if (!(((TargetsOtherCard) targetter).isValidTarget(targetee))) {
					sendPlayerActionBad(playerId, "Invalid target!");
					return;
				}

			} else {
				// well the card that attempted to target something isn't allowed to
				// target stuff. Error time!
				sendPlayerActionBad(playerId, "The card you attempted to use to target something isn't allowed to do that.");
			}
		} else {
			// player acting out of turn.
			sendPlayerActionBad(playerId, "Acting out of turn.");
		}
	}

	/**
	 * Handles players being targeted.
	 *
	 * @param userInput
	 *          a JsonObject representing the user's input.
	 * @param playerId
	 *          the player who submitted the action.
	 */
	public void handlePlayerTargeted(JsonObject userInput, int playerId) {
		if (isTurn(playerId)) {
			// this is true if the target player is the player who is acting and
			// false if the target player is the player who is not acting.
			boolean target = userInput.get("self").getAsBoolean();

			// get the card.
			Card card = board.getCardById(userInput.get("IID1").getAsInt());

			if (target) {
				// TODO targeted self.
			} else {
				// targeted opponent.
				if (card instanceof Creature) {
					// we have an attack going down.
					Creature attacker = (Creature) card;

					if (!(attacker.canAttack())) {
						sendPlayerActionBad(playerId, "That creature can no longer attack.");
						return;
					}

					// creature can attack, so let's attack!

					PlayerAttackEvent event = new PlayerAttackEvent(board.getInactivePlayer(), attacker);

					// tell player their action is valid.
					sendPlayerActionGood(playerId);

					// execute event on board.
					board.takeAction(event);

					// send board to both players.
					sendWholeBoardToBoth();
				} else if (card instanceof TargetsPlayer) {
					// TODO Targeted opposing player.
				}
			}
		} else {
			sendPlayerActionBad(playerId, "Acting out of turn.");
		}
	}

	/**
	 * Handles a card being played.
	 *
	 * @param userInput
	 *          a JSON representing the user's input.
	 * @param playerId
	 *          the player who submitted the action.
	 */
	public void handleCardPlayed(JsonObject userInput, int playerId) {
		System.out.println("asdiujqwiuihqwiuibuqwdqiubiua");
		if (isTurn(playerId)) {
			// grab relevant card.
			Card card = board.getCardById(userInput.get("IID1").getAsInt());
			if (!(board.getActivePlayer().validateCost(card.getCost()))) {
				// in this case they can't play the card.
				sendPlayerActionBad(playerId, "Cannot pay card's cost.");
				return;
			}

			if (card instanceof TargetsOtherCard) {
				// send player a target request b/c their card requires a target.
				CommsWebSocket.sendTargetRequest(playerId);
				return;
			}
			Zone z;
			if (card instanceof Creature) {
				z = Zone.CREATURE_BOARD;
			} else if (card instanceof AuraCard) {
				z = Zone.AURA_BOARD;
			} else {
				// spell or element.
				z = Zone.GRAVE;
			}

			// create event representing CardPlayedEvent
			CardPlayedEvent event = new CardPlayedEvent(card, board.getOcc(board.getActivePlayer(), Zone.HAND),
					board.getOcc(board.getActivePlayer(), z));

			// tell player action was ok.
			sendPlayerActionGood(playerId);

			// execute event on board.
			board.takeAction(event);

			// send board to both players.
			sendWholeBoardToBoth();
		} else {
			sendPlayerActionBad(playerId, "Acting out of turn.");
		}
	}

	@Override
	public JsonObject jsonifySelf() {
		JsonObject payload = new JsonObject();
		payload.add("player1", playerOne.jsonifySelf());
		payload.add("player2", playerTwo.jsonifySelf());
		payload.add("board", board.jsonifySelf());
		return payload;
	}

	@Override
	public JsonObject jsonifySelfChanged() {
		JsonObject payload = new JsonObject();
		payload.add("player1", playerOne.jsonifySelf());
		payload.add("player2", playerTwo.jsonifySelf());
		payload.add("board", board.jsonifySelfChanged());
		return payload;
	}

	// Modify them so that the players can't see eachothers hands
	public JsonObject playerOneJsonify(JsonObject toMod) {
		toMod.get("board").getAsJsonObject().get("hand2").getAsJsonObject().remove("cards");
		return toMod;
	}

	public JsonObject playerTwoJsonify(JsonObject toMod) {
		toMod.get("board").getAsJsonObject().get("hand1").getAsJsonObject().remove("cards");
		return toMod;
	}
}
