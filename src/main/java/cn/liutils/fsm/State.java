package cn.liutils.fsm;

public abstract class State {
	
	StateMachine machine;
	
	/**
	 * Called when leaving the state.
	 */
	public abstract void enterState();
	
	/**
	 * Called when entering the state.
	 */
	public abstract void leaveState();
	
	protected void transitState(String name) {
		machine.transitState(name);
	}
	
}
