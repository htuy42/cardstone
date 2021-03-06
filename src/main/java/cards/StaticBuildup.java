package cards;

import cardgamelibrary.Board;
import cardgamelibrary.Card;
import cardgamelibrary.CardType;
import cardgamelibrary.Effect;
import cardgamelibrary.ManaPool;
import cardgamelibrary.SpellCard;
import cardgamelibrary.Zone;
import devotions.AirDevotion;
import effects.CardDamageEffect;
import effects.DamageInterface;
import effects.EffectType;
import effects.EmptyEffect;
import effects.PlayerDamageEffect;
import game.Player;

public class StaticBuildup extends SpellCard{

	private static final String defaultImage = "images/StaticBuildup.jpg";
	private static final String defaultName = "Static Buildup";
	private static final String defaultText = "For the rest of the turn, every damaging spell does 1 more damage for every 2 storm charge you have.";
	private static final CardType defaultType = CardType.SPELL;
	private int turnsLeft;
	private int buildup;

	public StaticBuildup(Player owner) {
		super(new ManaPool(50, 0, 0, 0, 4, 0), defaultImage, owner, defaultName, defaultText, defaultType);
		turnsLeft = 0;
	}
	
	public Effect onThisPlayed(Card c, Zone z){
		turnsLeft = 1;
		return EmptyEffect.create();
	}
	
	public Effect onTurnStart(Player p, Zone z){
		turnsLeft--;
		return EmptyEffect.create();
	}
	
	public boolean onProposedEffect(Effect e, Zone z, Board b){
		if(turnsLeft == 1 && z == Zone.GRAVE){
			if(e instanceof DamageInterface){
				if(e.getSrc().getOwner().equals(getOwner())){
					return true;
				}
			}
		}
		return false;
	}
	
	public Effect getNewProposition(Effect e, Zone z){
		if(e instanceof DamageInterface){
			DamageInterface di = (DamageInterface) e;
			di.setDamage(di.getDamage() + AirDevotion.getLevelOfAir(getOwner().getDevotion()) / 2);
			return di;
		}
		return e;
	}
	
}
