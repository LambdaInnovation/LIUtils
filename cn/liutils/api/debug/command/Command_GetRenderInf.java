/**
 * 
 */
package cn.liutils.api.debug.command;

import cn.liutils.api.debug.IItemRenderInfProvider;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatMessageComponent;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.IItemRenderer.ItemRenderType;
import net.minecraftforge.client.MinecraftForgeClient;

/**
 * @author WeAthFolD
 *
 */
public class Command_GetRenderInf extends CommandBase {


	/* (non-Javadoc)
	 * @see net.minecraft.command.ICommand#getCommandName()
	 */
	@Override
	public String getCommandName() {
		return "getinf";
	}

	/* (non-Javadoc)
	 * @see net.minecraft.command.ICommand#getCommandUsage(net.minecraft.command.ICommandSender)
	 */
	@Override
	public String getCommandUsage(ICommandSender icommandsender) {
		return "/getinf";
	}

	/* (non-Javadoc)
	 * @see net.minecraft.command.ICommand#processCommand(net.minecraft.command.ICommandSender, java.lang.String[])
	 */
	@Override
	public void processCommand(ICommandSender ics, String[] astring) {
		EntityPlayer player = CommandBase.getCommandSenderAsPlayer(ics);
		if(player != null) {
			ItemStack item = player.getCurrentEquippedItem();
			if(item != null) {
				IItemRenderer renderer = MinecraftForgeClient.getItemRenderer(item, ItemRenderType.EQUIPPED_FIRST_PERSON);
				if(renderer != null && renderer instanceof IItemRenderInfProvider) {
					ics.sendChatToPlayer(ChatMessageComponent.createFromText(((IItemRenderInfProvider)renderer).getFullInformation()));
				}
			}
		}
	}

}
