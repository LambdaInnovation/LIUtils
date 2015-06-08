/**
 * Copyright (c) Lambda Innovation, 2013-2015
 * 本作品版权由Lambda Innovation所有。
 * http://www.li-dev.cn/
 *
 * This project is open-source, and it is distributed under  
 * the terms of GNU General Public License. You can modify
 * and distribute freely as long as you follow the license.
 * 本项目是一个开源项目，且遵循GNU通用公共授权协议。
 * 在遵照该协议的情况下，您可以自由传播和修改。
 * http://www.gnu.org/licenses/gpl.html
 */
package cn.liutils.fsm;

import cn.liutils.api.annotation.Experimental;

import com.google.common.collect.HashBiMap;

/**
 * An simple finite state machine implementation. Use string to identify different states.
 * You can add states into the machine and set the initial state. <br>
 * Before any actual using of the machine, use init() to set the current state to the init state. <br>
 * After then, use transitState(name) to transit the state of the state machine.
 * You can use the end() method to end the current state and start over again. <br>
 * This FSM DOESNT permit null states.
 * @author WeAthFolD
 */
@Experimental
public class StateMachine<T extends State> {
	
	private HashBiMap<String, T> states = HashBiMap.create();
	
	private T initial;
	private T current;
	
	public StateMachine() {}
	
	public StateMachine<T> addState(String name, T state) {
		states.put(name, state);
		state.machine = this;
		if(initial == null)
			initial = state;
		return this;
	}
	
	public StateMachine<T> removeState(String name) {
		T removed = states.remove(name);
		if(removed == current && removed == initial)
			throw new RuntimeException("Illegal removal: causes undefined initial state");
		if(removed == current) {
			transitState(initial);
		}
			
		return this;
	}
	
	public void setInitialState(String name) {
		T state = getState(name);
		if(state == null)
			throw new RuntimeException("State " + name + "not found");
		initial = state;
	}
	
	public void start() {
		current = initial;
		current.enterState();
	}
	
	public void end() {
		onStateEnd(current);
		current = null;
	}
	
	/**
	 * Transit the state. Will ignore if the current state==new state.
	 */
	public void transitState(String name) {
		transitState(getState(name));
	}
	
	public T getCurrentState() {
		return current;
	}
	
	protected void onStateEnd(T state) {
		state.leaveState();
	}
	
	private void transitState(T toChange) {
		if(current == toChange)
			return;
		
		if(toChange == null)
			throw new RuntimeException("Trying to transit to a null state!");
		
		onStateEnd(current);
		
		current = toChange;
		current.enterState();
	}
	
	public T getState(String name) {
		return states.get(name);
	}
	
	private String getStateName(T state) {
		return states.inverse().get(state);
	}
	
}
