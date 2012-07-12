package cpw.mods.ironchest;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import net.minecraft.src.Block;
import net.minecraft.src.BlockBed;
import net.minecraft.src.EntityAIBase;
import net.minecraft.src.EntityAIOcelotSit;
import net.minecraft.src.EntityOcelot;
import net.minecraft.src.TileEntityChest;
import net.minecraft.src.World;

public class IronChestAIOcelotSit extends EntityAIBase {
  private EntityOcelot ocelot;
  private final float speed;
  private int ticksRun = 0;
  private int tryTicks = 0;
  private int maxTicks = 0;
  private int targetX = 0;
  private int targetY = 0;
  private int targetZ = 0;
  private EntityAIOcelotSit sitAI;
  private Method isTargetBlockMethod;
  private boolean grabbedMethod;

  public IronChestAIOcelotSit(EntityOcelot par1EntityOcelot, float par2, EntityAIOcelotSit sitAI) {
    this.ocelot = par1EntityOcelot;
    this.speed = par2;
    this.sitAI = sitAI;
    this.setMutexBits(5);
    if (!grabbedMethod) {
      try {
        isTargetBlockMethod = EntityAIOcelotSit.class.getDeclaredMethod("a", World.class, int.class, int.class, int.class);
      } catch (Exception e) {
        // Can't find it
        try {
          isTargetBlockMethod = EntityAIOcelotSit.class.getDeclaredMethod("func_50078_a", World.class, int.class, int.class, int.class);
        } catch (Exception e1) {
        }
      }
      isTargetBlockMethod.setAccessible(true);
      grabbedMethod = true;
    }
  }

  public boolean shouldExecute()
  {
    return ocelot.isTamed() && !ocelot.isSitting() && ocelot.getRNG().nextDouble() <= 0.0065D && thereIsChestNearby();
  }

  /**
   * Returns whether an in-progress EntityAIBase should continue executing
   */
  public boolean continueExecuting()
  {
    return this.ticksRun <= this.maxTicks && this.tryTicks <= 60 && isBlockAChestBlock(ocelot.worldObj, this.targetX, this.targetY, this.targetZ);
  }

  /**
   * Execute a one shot task or start executing a continuous task
   */
  public void startExecuting()
  {
    this.ocelot.getNavigator().tryMoveToXYZ((double) ((float) this.targetX) + 0.5D, (double) (this.targetY + 1), (double) ((float) this.targetZ) + 0.5D,
        this.speed);
    this.ticksRun = 0;
    this.tryTicks = 0;
    this.maxTicks = this.ocelot.getRNG().nextInt(this.ocelot.getRNG().nextInt(1200) + 1200) + 1200;
    this.ocelot.func_50008_ai().setIsSitting(false);
  }

  /**
   * Resets the task
   */
  public void resetTask()
  {
    this.ocelot.setSitting(false);
  }

  /**
   * Updates the task
   */
  public void updateTask()
  {
    ++this.ticksRun;
    this.ocelot.func_50008_ai().setIsSitting(false);

    if (this.ocelot.getDistanceSq((double) this.targetX, (double) (this.targetY + 1), (double) this.targetZ) > 1.0D)
    {
      this.ocelot.setSitting(false);
      this.ocelot.getNavigator().tryMoveToXYZ((double) ((float) this.targetX) + 0.5D, (double) (this.targetY + 1), (double) ((float) this.targetZ) + 0.5D,
          this.speed);
      ++this.tryTicks;
    }
    else if (!this.ocelot.isSitting())
    {
      this.ocelot.setSitting(true);
    }
    else
    {
      --this.tryTicks;
    }
  }

  private boolean thereIsChestNearby() {
    int searchY = (int) this.ocelot.posY;
    double closestChestDistance = Integer.MAX_VALUE;

    for (int searchX = (int) this.ocelot.posX - 8; searchX < this.ocelot.posX + 8; searchX++) {
      for (int searchZ = (int) this.ocelot.posZ - 8; searchZ < this.ocelot.posZ + 8; searchZ++) {
        if (this.isBlockAChestBlock(this.ocelot.worldObj, searchX, searchY, searchZ) && this.ocelot.worldObj.isAirBlock(searchX, searchY + 1, searchZ)) {
          double chestDistance = this.ocelot.getDistanceSq((double) searchX, (double) searchY, (double) searchZ);

          if (chestDistance < closestChestDistance) {
            this.targetX = searchX;
            this.targetY = searchY;
            this.targetZ = searchZ;
            closestChestDistance = chestDistance;
          }
        }
      }
    }

    return closestChestDistance < Integer.MAX_VALUE;
  }

  private boolean isBlockAChestBlock(World world, int x, int y, int z) {
    if (world.getBlockId(x, y, z)==mod_IronChest.ironChestBlock.blockID) {
      return true;
    }
    try {
      return (Boolean) isTargetBlockMethod.invoke(sitAI, world, x, y, z);
    } catch (Exception e) {
    }
    return false;
  }
}
