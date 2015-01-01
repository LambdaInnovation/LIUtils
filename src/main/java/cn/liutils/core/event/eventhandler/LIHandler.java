package cn.liutils.core.event.eventhandler;

import cpw.mods.fml.common.eventhandler.Event;

/**
 * 
 * @author Violet
 *
 */
public abstract class LIHandler {
	
	private boolean dead = false;
	
	public final boolean isDead() {
		return dead;
	}
	
	protected final void setDead() {
		dead = true;
	}

	public final void trigger(Event event) {
		if (!onEvent(event))
			throw new RuntimeException("Unexpected event(" + event.getClass().getName() + ") for " + this.getClass().getName());
	}
	
	protected abstract boolean onEvent(Event event);
}
