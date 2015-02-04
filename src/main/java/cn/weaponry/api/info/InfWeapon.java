/**
 * 
 */
package cn.weaponry.api.info;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import cn.weaponry.api.action.Action;
import cn.weaponry.api.item.WeaponBase;
import cn.weaponry.api.state.WeaponState;

/**
 * @author WeathFolD
 *
 */
public final class InfWeapon {
	
	public final EntityPlayer player;
	ItemStack lastStack; //For validity check
	
	WeaponState curState;
	
	NBTTagCompound data = new NBTTagCompound(); //any weapon runtime data
	
	Map<String, ActionNode> activeActions = new HashMap();
	
	private int ticksExisted;

	public InfWeapon(EntityPlayer _player) {
		player = _player;
	}
	
	public void onUpdate() {
		//validity check
		ItemStack cur = player.getCurrentEquippedItem();
		if(cur == null || !cur.equals(lastStack)) {
			reset();
		}
		if(!active())
			return;
		
		//Update actions
		Iterator<ActionNode> iter = activeActions.values().iterator();
		while(iter.hasNext()) {
			ActionNode node = iter.next();
			int passed = ticksExisted - node.tickAdded;
			if(node.action.onActionTick(this, passed, node.life) || 
				(node.life > 0 && passed > node.life)) {
				node.action.onActionEnd(this, passed, node.life);
				iter.remove();
			}
		}
		
		++ticksExisted;
	}
	
	public void transitState(WeaponState state) {
		curState.leaveState(this);
		curState = state;
	}
	
	public boolean addAction(Action act, int life) {
		if(!active()) {
			return false;
		}
		
		String ID = act.getID();
		if(activeActions.containsKey(ID)) {
			return false;
		}
		
		activeActions.put(ID, new ActionNode(act, life));
		act.onActionBegin(this, life);
		return true;
	}
	
	public void removeAction(String ID) {
		removeAction(ID, false);
	}
	
	public void removeAction(String ID, boolean silent) {
		ActionNode node = activeActions.remove(ID);
		if(node != null && !silent) {
			node.action.onActionEnd(this, ticksExisted - node.tickAdded, node.life);
		}
	}
	
	public void handleRawInput(int keyid, boolean down) {
		if(active()) {
			curState.keyChanged(this, keyid, down);
		}
	}
	
	public void handleKeyTick(int keyid) {
		if(active()) {
			curState.keyTick(this, keyid);
		}
	}
	
	public boolean active() {
		return lastStack != null && curState != null;
	}
	
	public NBTTagCompound getData() {
		return data;
	}
	
	private void reset() {
		lastStack = player.getCurrentEquippedItem();
		data = new NBTTagCompound();
		ticksExisted = 0;
		activeActions.clear();
		
		if(lastStack != null && lastStack.getItem() instanceof WeaponBase) {
			WeaponBase weapon = (WeaponBase) lastStack.getItem();
			curState = weapon.getInitialState();
		} else {
			curState = null;
		}
	}
	
	private class ActionNode {
		public final Action action;
		public final int tickAdded;
		public final int life;
		public ActionNode(Action _act, int _life) {
			action = _act;
			tickAdded = ticksExisted;
			life = _life;
		}
	}

}
