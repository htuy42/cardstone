package effects;

import cardgamelibrary.Card;
import cardgamelibrary.Effect;

public interface DamageInterface extends Effect {

	public void setDamage(int dmg);

	public int getDamage();

}
