package game;

import com.google.gson.JsonObject;

/**
 * Class to keep track of all games currently running and handle playing them.
 *
 * @author Raghu
 *
 */
public class GameManager {

	// some sort of method to add games.
	public static void addGame() {

	}

	// remove games when they complete.
	public static void removeGame(Game game) {

	}

	public static void receiveUnderstoodBoardState(int playerId, JsonObject message) {

	}

	public static void recieveTargetedCard(int playerId, JsonObject message) {

	}

	public static void recieveTargetedPlayer(int playerId, JsonObject message) {

	}

	// create a different method for each message I can receive (see
	// messageTypeEnum). They should all take some playerId and
	// a JSON object (the payload).
}
