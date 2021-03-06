package cards;

import cardgamelibrary.Card;
import cardgamelibrary.CardType;
import cardgamelibrary.Creature;
import cardgamelibrary.Effect;
import cardgamelibrary.ManaPool;
import cardgamelibrary.SpellCard;
import cardgamelibrary.Zone;
import effects.SummonEffect;
import effects.TransformEffect;
import game.Player;
import templates.OnOwnDeathCard;
import templates.TargetsOtherCard;

public class SandTomb extends SpellCard implements TargetsOtherCard {

	public SandTomb(Player owner) {
		super(defaultCost, defaultImage, owner, defaultName, defaultText, defaultType);
	}

	private static final ManaPool defaultCost = new ManaPool(25, 0, 0, 1, 0, 0);
	private static final String defaultImage = "images/SandTomb.jpg";
	private static final String defaultName = "Sand Tomb";
	private static final String defaultText = "Imprison an enemy minion in a 0/8 Tomb with taunt on your side of the board. When the Tomb is destroyed, return the minion.";
	private static final CardType defaultType = CardType.SPELL;

	@Override
	public boolean cardValidTarget(Card card, Zone targetIn) {
		if (card.getType() != CardType.CREATURE) {
			return false;
		}
		if (this.getOwner() == card.getOwner()) {
			return false;
		}
		if (targetIn != Zone.CREATURE_BOARD) {
			return false;
		}
		return true;
	}

	@Override
	public Effect impactCardTarget(Card target, Zone targetIn) {
		assert cardValidTarget(target, targetIn);
		Creature tomb = new Tomb(target, getOwner());
		return new TransformEffect(target, tomb, Zone.CREATURE_BOARD,this);
	}

	private static class Tomb extends Creature implements OnOwnDeathCard {
		private static final String defaultImage = "images/Tomb.jpg";
		private static final String defaultName = "Tomb";
		private static final String defaultText = "Destroy this to free the imprisoned minion!";
		private static final int defaultHealth = 8;
		private static final int defaultAttack = 0;
		private static final CardType defaultType = CardType.CREATURE;
		Card resummon;

		private Tomb(Card resummon, Player owner) {
			super(defaultAttack, defaultHealth, new ManaPool(0, 0, 0, 1, 0, 0), defaultImage, owner, defaultName, defaultText, defaultType);
			this.resummon = resummon;
		}

		@Override
		public Effect onDeathEffect(Zone z) {
			return new SummonEffect(resummon, Zone.CREATURE_BOARD,this);
		}

	}

}
