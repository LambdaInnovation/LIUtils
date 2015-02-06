/**
 * 
 */
package cn.weaponry.template.classic;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import cn.liutils.util.GenericUtils;
import cn.weaponry.api.info.InfWeapon;
import cn.weaponry.api.item.WeaponBase;
import cn.weaponry.api.state.WeaponState;
import cn.weaponry.template.classic.action.ClassicShoot;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * Old half-life/quake style weapon. Greedy-fill reloading preference,
 * and simple one-field-ammo-value handling method.
 * @author WeathFolD
 */
public abstract class WeaponClassic extends WeaponBase {
	
	private final int maxAmmo;
	public final Item ammoType;
	
	public int shootRate = 10;
	public int jamRate = 10;
	public int reloadTime = 30;
	
	ClassicShoot actionShoot;

	public WeaponClassic(int _maxAmmo, Item _ammoType) {
		maxAmmo = _maxAmmo;
		ammoType = _ammoType;
		actionShoot = new ClassicShoot(1);
		initStates();
	}
	
	protected void initStates() {
		this.addState("idle", new StateIdle());
		this.addState("jam", new StateJam());
		this.addState("shoot", new StateShoot());
		this.addState("reload", new StateReload());
	}
    
	public void setBulletDamage(float _dmg) {
		actionShoot.dmg = _dmg;
	}
	
	/**
	 * @param player
	 * @param req
	 * @return how many successfully consumed from inv
	 */
	protected int tryReload(EntityPlayer player, int req) {
		return req;
	}
	
	public abstract boolean isAutomatic();
	
	public abstract String getJamSound();
	
	public abstract String getShootSound();
	
    //---Ammo---
	public int getAmmo(ItemStack stack) {
		return GenericUtils.loadCompound(stack).getShort("ammo");
	}
	
	public void setAmmo(ItemStack stack, int n) {
		n = Math.max(0, Math.min(n, maxAmmo)); //wrap
		GenericUtils.loadCompound(stack).setShort("ammo", (short) n);
	}
	
	public boolean hasAmmo(ItemStack stack) {
		return getAmmo(stack) > 0;
	}
	
	public int getMaxAmmo() {
		return maxAmmo;
	}

	@SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean wtf) {
		String str = getAmmo(stack) + "/" + getMaxAmmo();
		list.add(str);
	}
	
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs cct, List list) {
    	ItemStack res = new ItemStack(item, 1, 0);
    	setAmmo(res, getMaxAmmo());
        list.add(res);
    }
    
    //States
	public class StateIdle extends WeaponState {
		@Override
		public void keyChanged(InfWeapon info, int keyid, boolean down) {
			if(down) {
				switch(keyid) {
				case 0:
					if(isAutomatic()) {
						info.transitState(hasAmmo(info.getCurStack()) ? "shoot" : "jam");
					} else {
						//handle simple shooting without state transition
						int dt = info.getTickChange("idle");
						if(dt >= shootRate) {
							info.saveTick("idle");
							if(hasAmmo(info.getCurStack())) {
								info.player.playSound(getShootSound(), 0.5F, 1.0F);
								info.addAction(actionShoot, 0);
							} else {
								info.player.playSound(getJamSound(), 0.5F, 1.0F);
							}
						}
					}
					break;
				case 2:
					ItemStack stack = info.getCurStack();
					if(getAmmo(stack) < getMaxAmmo()) {
						info.transitState("reload");
					}
					break;
				case 1:
					break;
				}
			}
		}
	}
	
	public class StateShoot extends WeaponState {
		@Override
		public void keyChanged(InfWeapon info, int keyid, boolean down) {
			info.transitState("idle");
		}
		@Override
		public void update(InfWeapon info) {
			int dt = info.getStateAliveTick();
			if(dt % shootRate == 0 && hasAmmo(info.getCurStack())) {
				info.player.playSound(getJamSound(), 0.5F, 1.0F);
			}
		}
	}
	
	public class StateJam extends WeaponState {
		@Override
		public void keyChanged(InfWeapon info, int keyid, boolean down) {
			info.transitState("idle");
		}
		@Override
		public void update(InfWeapon info) {
			int dt = info.getStateAliveTick();
			if(dt % jamRate == 0) {
				if(hasAmmo(info.getCurStack())) {
					info.player.playSound(getShootSound(), 0.5F, 1.0F);
					info.addAction(actionShoot, 0);
				} else {
					info.transitState("idle");
				}
			}
		}
	}
	
	public class StateReload extends WeaponState {
		@Override
		public void keyChanged(InfWeapon info, int keyid, boolean down) {
			if(keyid == 0 && down) {
				info.transitState("idle");
			}
		}
		@Override
		public void update(InfWeapon info) {
			int dt = info.getStateAliveTick();
			ItemStack stack = info.getCurStack();
			if(dt >= reloadTime) {
				//Finished reloading, go back to idle.
				int na = getAmmo(stack);
				na += tryReload(info.player, getMaxAmmo() - na);
				setAmmo(stack, na);
				info.transitState("idle");
			}
		}
	}
}
