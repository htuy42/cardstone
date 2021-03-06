package game;

import java.io.Serializable;

import com.google.gson.JsonObject;

import cardgamelibrary.Card;
import cardgamelibrary.DevotionType;
import cardgamelibrary.Effect;
import cardgamelibrary.Element;
import cardgamelibrary.ElementType;
import cardgamelibrary.ManaPool;
import devotions.Devotion;
import devotions.NoDevotion;
import effects.ApplyDevotionEffect;
import effects.EmptyEffect;

/**
 * Class to represent a player in the game.
 *
 * @author Raghu
 *
 */
public class Player implements Serializable {
	private PlayerType playerType;
	private int life;
	private ManaPool manaPool;
	private final int id;
	private Devotion devotion;

	// keeps track of amount of resources to gain at start of a turn.
	private int maxResources = 0;

	// how much the amount of resources you gain per turn increments.
	private static final int RESOURCE_GAIN = 10;

	public Player(int l, PlayerType p, int id) {
		playerType = p;
		life = l;
		manaPool = new ManaPool(0, 0, 0, 0, 0, 0);
		this.id = id;
		this.devotion = new NoDevotion(this);
	}

	public PlayerType getPlayerType() {
		return playerType;
	}

	public int getLife() {
		return life;
	}

	public int getId() {
		return id;
	}

	public void changeResources(int newCount) {
		manaPool.setResources(newCount);
	}

	/**
	 * Increases the resources of the player by the appropriate amount for the
	 * start of a turn.
	 */
	public void startTurn() {
		// Increment by fixed amount.
		maxResources += RESOURCE_GAIN;

		// reset resource count at turn start.
		manaPool.setResources(0);

		// update manapool
		manaPool.setResources(manaPool.getResources() + maxResources);
		// resources capped
		if (manaPool.getResources() > 130) {
			manaPool.setResources(130);
		}
	}

	public void setElement(ElementType type, int elem) {
		manaPool.setElement(type, elem);
	}

	public int getResources() {
		return manaPool.getResources();
	}

	public int getElem(ElementType type) {
		return manaPool.getElement(type);
	}

	public void setLife(int newLife) {
		life = newLife;
	}

	public void takeDamage(int damage) {
		life -= damage;
	}

	public void healDamage(int heal) {
		life += heal;
		System.out.println("i helaed for" + heal);
	}

	@Override
	public boolean equals(Object o) {
		if (o != null && this != null && o instanceof Player) {
			return ((Player) o).getId() == this.getId();
		}
		return false;
	}

	/**
	 * Checks to see if a player can pay a cost.
	 *
	 * @param cost
	 *          the cost.
	 * @return a boolean representing whether the player has sufficient resources
	 *         to pay the cost.
	 */
	public boolean validateCost(ManaPool cost) {
		return manaPool.canPay(cost);
	}

	/**
	 * makes the player pay the specified cost.
	 *
	 * @param cost
	 *          the cost to pay.
	 */
	public void payCost(ManaPool cost) {
		// assert we can pay the cost.
		assert (validateCost(cost));
		manaPool.payCost(cost);
	}
	
	public Devotion getDevotion(){
		return devotion;
	}
	
	
	
	public Effect tryApplyDevotion(ElementType type,Card src){
		if(this.devotion.getDevotionType().equals(DevotionType.NO_DEVOTION)){
			return new ApplyDevotionEffect(this,type,src);
		}
		return EmptyEffect.create();
	}

	public JsonObject jsonifySelf() {
		JsonObject result = new JsonObject();
		result.addProperty("health", life);
		result.addProperty("resources", manaPool.getResources());
		result.addProperty("playerId", getId());
		JsonObject elementObject = new JsonObject();
		elementObject.addProperty("fire", manaPool.getElement(ElementType.FIRE));
		elementObject.addProperty("water", manaPool.getElement(ElementType.WATER));
		elementObject.addProperty("air", manaPool.getElement(ElementType.AIR));
		elementObject.addProperty("earth", manaPool.getElement(ElementType.EARTH));
		elementObject.addProperty("balance", manaPool.getElement(ElementType.BALANCE));
		result.add("element", elementObject);
		result.add("devotion", devotion.jsonifySelf());
		return result;
	}

	public void setDevotion(ElementType src) {
		this.devotion = DevotionType.getDevotion(this,src);
	}
}
