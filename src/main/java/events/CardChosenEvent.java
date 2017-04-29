package events;

import cardgamelibrary.Card;
import cardgamelibrary.Event;
import cardgamelibrary.EventType;
import cards.templates.PlayerChoosesCards;

public class CardChosenEvent implements Event {

	private PlayerChoosesCards	chooser;
	private Card								chosen;

	public CardChosenEvent(PlayerChoosesCards chooser, Card chosen) {
		this.chooser = chooser;
		this.chosen = chosen;
	}

	/**
	 * Gets the card that prompted the choice.
	 *
	 * @return the card that prompted the choice.
	 */
	public PlayerChoosesCards getChooser() {
		return chooser;
	}

	/**
	 * Gets the card chosen.
	 *
	 * @return the card chosen by the player.
	 */
	public Card getChosen() {
		return chosen;
	}

	@Override
	public EventType getType() {
		// TODO Auto-generated method stub
		return EventType.CARD_CHOSEN;
	}

}
