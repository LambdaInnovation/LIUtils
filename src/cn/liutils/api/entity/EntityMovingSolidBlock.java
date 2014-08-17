package cn.liutils.api.entity;

import net.minecraft.block.Block;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ReportedException;
import net.minecraft.world.World;

/**
 * EntityMovingSolidBlock
 * 
 * @author mkpoli
 *
 */
public class EntityMovingSolidBlock extends Entity {
//	private int blockId, metadata;
	public Block block;

	/**
	 * Default Generate
	 * 
	 * @param arg0
	 */
	public EntityMovingSolidBlock(World world) {
		super(world);
	}

//	/**
//	 * Generate
//	 * 
//	 * @param world
//	 * @param blockId
//	 */
//	public EntityMovingSolidBlock(World world, int blockId) {
//		this(world, blockId, 0);
//	}
//
//	/**
//	 * Generate
//	 * 
//	 * @param world
//	 * @param blockId
//	 */
//	public EntityMovingSolidBlock(World world, int blockId, int metadata) {
//		super(world);
//		this.blockId = blockId;
//		this.metadata = metadata;
//	}
	
	public EntityMovingSolidBlock(World world, Block block) {
		super(world);
		this.block = block;
	}

	@Override
	protected void entityInit() {
		
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound arg0) {
//		try {
//			this.blockId = arg0.getInteger("BlockId");
//			this.metadata = arg0.getInteger("BlockMeta");
//		} catch (Throwable throwable) {
//			CrashReport crashreport = CrashReport.makeCrashReport(throwable,
//					"Saving entity NBT");
//			CrashReportCategory crashreportcategory = crashreport
//					.makeCategory("Entity being saved");
//			this.addEntityCrashInfo(crashreportcategory);
//			throw new ReportedException(crashreport);
//		}
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound arg0) {
//		try {
//			arg0.setInteger("BlockId", this.blockId);
//			arg0.setInteger("BlockMeta", this.metadata);
//		} catch (Throwable throwable) {
//			CrashReport crashreport = CrashReport.makeCrashReport(throwable,
//					"Loading entity NBT");
//			CrashReportCategory crashreportcategory = crashreport
//					.makeCategory("Entity being loaded");
//			this.addEntityCrashInfo(crashreportcategory);
//			throw new ReportedException(crashreport);
//		}
	}

}
