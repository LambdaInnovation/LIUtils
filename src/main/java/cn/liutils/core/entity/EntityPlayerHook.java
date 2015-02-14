/**
 * Code by Lambda Innovation, 2013.
 */
package cn.liutils.core.entity;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import cn.annoreg.core.RegistrationClass;
import cn.annoreg.mc.RegEntity;
import cn.annoreg.mc.RegEventHandler;
import cn.annoreg.mc.RegEventHandler.Bus;
import cn.liutils.api.render.IPlayerRenderHook;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.PlayerTickEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * A entity that always in the position of a player entity, in aid of player additional rendering.
 * @author WeAthFolD
 */
@RegistrationClass
@RegEntity(clientOnly = true)
@RegEntity.HasRender
public class EntityPlayerHook extends Entity {

	public EntityPlayer player;
	public final boolean blend;
	
	static Set<EntityPlayer> initTable = new HashSet();
	static Set<IPlayerRenderHook> helpers_op = new HashSet(),
			helpers_al = new HashSet();
	
	@SideOnly(Side.CLIENT)
	@RegEntity.Render
	public static HookRender render;
	
	/**
	 */
	public EntityPlayerHook(EntityPlayer p, boolean _blend) {
		super(p.worldObj);
		player = p;
		blend = _blend;
		setPositionAndRotation(p.posX, p.posY, p.posZ, p.rotationYaw, p.rotationPitch);
	}
	
    @Override
	public void onUpdate() {
    	setPositionAndRotation(player.posX, player.posY, player.posZ, player.renderYawOffset, player.rotationPitch);
    }
    
    @Override
    public boolean shouldRenderInPass(int pass) {
        return blend ? pass == 1 : pass == 0;
    }
    
	public static void regPlayerRenderHook(IPlayerRenderHook hook, boolean alpha) {
		(alpha ? helpers_al : helpers_op).add(hook);
	}

	@Override
	protected void entityInit() {}

	@Override
	protected void readEntityFromNBT(NBTTagCompound nbttagcompound) {}

	@Override
	protected void writeEntityToNBT(NBTTagCompound nbttagcompound) {}
	
	public static class HookRender extends Render {

		@Override
		public void doRender(Entity ent, double x, double y,
				double z, float wtf, float var9) {
			EntityPlayerHook hook = (EntityPlayerHook) ent;
			GL11.glPushMatrix();
			GL11.glTranslated(x, y, z);
			traverse(hook, hook.blend ? helpers_al : helpers_op);
			GL11.glPopMatrix();
		}
		
		private void traverse(EntityPlayerHook ent, Set<IPlayerRenderHook> hooks) {
			for(IPlayerRenderHook hook : hooks) {
				GL11.glPushMatrix();
				GL11.glRotatef(ent.rotationYaw, 0.0F, -1.0F, 0.0F);
				GL11.glPushMatrix(); {
					GL11.glTranslated(0, -1.67, 0);
					hook.renderBody(ent.player, ent.worldObj);
				} GL11.glPopMatrix();
				GL11.glTranslatef(0.0F, 0.5F, 0.0F);
				hook.renderHead(ent.player, ent.worldObj);
				GL11.glPopMatrix();
			}
		}

		@Override
		protected ResourceLocation getEntityTexture(Entity var1) {
			return null;
		}
		
	}

	@RegEventHandler(Bus.FML)
	public static final class Tick {
		@SubscribeEvent
		public void playerTick(PlayerTickEvent event) {
			EntityPlayer player = event.player;
			if(!player.worldObj.isRemote)
				return;
			//Check if instance is out of date
			//TODO:Low efficiency
			Iterator<EntityPlayer> iter = initTable.iterator();
			while(iter.hasNext()) {
				EntityPlayer p = iter.next();
				if(p.equals(player) && !(p == player)) {
					iter.remove();
					break;
				}
			}
			
			if(!initTable.contains(player)) {
				initTable.add(player);
				player.worldObj.spawnEntityInWorld(new EntityPlayerHook(player, true));
				player.worldObj.spawnEntityInWorld(new EntityPlayerHook(player, false));
			}
			
		}
	}

}
