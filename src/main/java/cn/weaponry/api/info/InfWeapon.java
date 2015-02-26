/**
 * Copyright (c) Lambda Innovation, 2013-2015
 * 本作品版权由Lambda Innovation所有。
 * http://www.lambdacraft.cn/
 *
 * LIUtils is open-source, and it is distributed under 
 * the terms of GNU General Public License. You can modify
 * and distribute freely as long as you follow the license.
 * LIUtils是一个开源项目，且遵循GNU通用公共授权协议。
 * 在遵照该协议的情况下，您可以自由传播和修改。
 * http://www.gnu.org/licenses/gpl.html
 */
package cn.weaponry.api.info;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import cn.liutils.api.draw.DrawObject.EventType;
import cn.liutils.util.GenericUtils;
import cn.weaponry.api.action.Action;
import cn.weaponry.api.client.render.RenderEffect;
import cn.weaponry.api.item.WeaponBase;
import cn.weaponry.api.state.WeaponState;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

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
	Set<EffectNode> activeEffects = new HashSet();
	
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
		
		//Update cur state
		curState.update(this);
		
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
		
		//Update render
		if(player.worldObj.isRemote) {
			updateRender();
		}
		
		++ticksExisted;
	}
	
	@SideOnly(Side.CLIENT)
	private void updateRender() {
		Iterator<EffectNode> iter = activeEffects.iterator();
		long time = Minecraft.getSystemTime();
		while(iter.hasNext()) {
			EffectNode node = iter.next();
			long dt = time - node.timeAdded;
			if(node.life > 0 && dt > node.life) {
				iter.remove();
			}
		}
	}
	
	public WeaponState getCurState() {
		return curState;
	}
	
	public void transitState(String name) {
//		if(!active())
//			return;
		System.out.println("Transit to: " + name);
		WeaponBase wb = (WeaponBase) lastStack.getItem();
		if(curState != null)
			curState.leaveState(this);
		curState = wb.getState(name);
		if(curState != null)
			curState.enterState(this);
		saveTick("_stateTick");
	}
	
	//---Ticker
	
	public void saveTick(String channel) {
		data.setInteger(channel, ticksExisted);
	}
	
	public int getTickChange(String channel) {
		return ticksExisted - data.getInteger(channel);
	}
	
	public int getStateAliveTick() {
		return getTickChange("_stateTick");
	}
	
	//---
	
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
	
	public void addRenderEffect(RenderEffect eff, long time) {
		activeEffects.add(new EffectNode(eff, time));
		eff.startEffect(this);
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
	
	@SideOnly(Side.CLIENT)
	public void postRenderEvent(String part) {
		for(EffectNode node : activeEffects) {
			node.eff.beforeDraw(part, this, Minecraft.getSystemTime() - node.timeAdded);
		}
	}
	
	public boolean active() {
		return lastStack != null && curState != null;
	}
	
	public NBTTagCompound getData() {
		return data;
	}
	
	public ItemStack getCurStack() {
		return lastStack;
	}
	
	private void reset() {
		lastStack = player.getCurrentEquippedItem();
		data = new NBTTagCompound();
		ticksExisted = 0;
		activeActions.clear();
		activeEffects.clear();
		
		if(lastStack != null && lastStack.getItem() instanceof WeaponBase) {
			WeaponBase weapon = (WeaponBase) lastStack.getItem();
			transitState(weapon.getInitStateName());
			weapon.onActivated(this);
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
	
	private class EffectNode {
		public final RenderEffect eff;
		public final long timeAdded;
		public final long life;
		public EffectNode(RenderEffect _eff, long _life) {
			eff = _eff;
			timeAdded = Minecraft.getSystemTime();
			life = _life;
		}
	}

}
