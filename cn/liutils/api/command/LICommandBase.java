/**
 * Code by Lambda Innovation, 2013.
 */
package cn.liutils.api.command;

import net.minecraft.command.CommandBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatMessageComponent;

/**
 * @author WeAthFolD
 *
 */
public abstract class LICommandBase extends CommandBase {

	protected void sendChatToPlayer(EntityPlayer player, String str) {
		player.sendChatToPlayer(ChatMessageComponent.createFromText(str));
	}

}
