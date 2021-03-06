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
package cn.liutils.debug;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;
import cn.annoreg.core.Registrant;
import cn.annoreg.mc.RegCommand;
import cn.liutils.template.command.LICommandBase;

/**
 * @author WeathFolD
 *
 */
public class CmdMineStatistics extends LICommandBase {

    public CmdMineStatistics() {}
    
    @Override
    public String getCommandName() {
        return "minestat";
    }

    @Override
    public String getCommandUsage(ICommandSender var1) {
        return "/minestat or /minestat <size>";
    }

    @Override
    public void processCommand(ICommandSender var1, String[] var2) {
        EntityPlayer player = this.getCommandSenderAsPlayer(var1);
        int cx = ((int)player.posX) >> 4;
        int cz = ((int)player.posZ) >> 4;
        int size = var2.length == 0 ? 32 : Integer.valueOf(var2[0]);
        Thread t = new Thread(new Calc(player, cx, cz, size));
        t.setName("Mine statistics thread");
        t.start();
    }
    
    private class Calc implements Runnable {
        
        Set<Integer> filteredDicts = new HashSet(Arrays.asList(new Integer[] {
            OreDictionary.getOreID("logWood"), OreDictionary.getOreID("stone")
        }));
        
        EntityPlayer player;
        World world;
        int x, z;
        int size;
        
        Map<Integer, Integer> resMap = new HashMap();
        
        public Calc(EntityPlayer _player, int cx, int cz, int sampleSize) {
            player = _player;
            world = player.worldObj;
            x = cx << 4;
            z = cz << 4;
            size = sampleSize;
        }

        @Override
        public void run() {
            sendChat(player, "Starting statistics, this may take some time......");
            int total = 0;
            for(int i = x; i < x + size; ++i) {
                for(int j = 0; j < 65; ++j) {
                    for(int k = z; k < z + size; ++k) {
                        Block b = null;
                        b = world.getBlock(i, j, k);
                        if(Item.getItemFromBlock(b) == null) continue;
                        for(int id : OreDictionary.getOreIDs(new ItemStack(b))) {
                            if(filteredDicts.contains(id))
                                continue;
                            Integer it = resMap.get(id);
                            if(it == null) it = 0;
                            resMap.put(id, it + 1);
                            ++total;
                        }
                    }
                }
            }
            
            synchronized(this) {
                sendChat(player, String.format("stat info at chunk (%d, %d) with sample size %d: ", x >> 4, z >> 4, size));
                for(Entry<Integer, Integer> ent : resMap.entrySet()) {
                    sendChat(player, OreDictionary.getOreName(ent.getKey()) + " appeared " + 
                            ent.getValue() + " times, weight " + String.format("%.2f%%%%", (float)ent.getValue() / total * 100));
                }
            }
        }
        
    }

}
