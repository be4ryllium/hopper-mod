// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;

import java.util.Random;

// Referenced classes of package net.minecraft.src:
//            BlockContainer, Material, Block, World, 
//            IBlockAccess, TileEntityDispenser, EntityPlayer, ItemStack, 
//            Item, EntityArrow, EntityEgg, EntitySnowball, 
//            EntityItem, EntityLiving, MathHelper, IInventory, 
//            TileEntity

public class BlockHopper extends BlockContainer
{

    protected BlockHopper(int i, int j)
    {
        super(i, j, Material.iron);
        random = new Random();
    }

    public int tickRate()
    {
        return 4;
    }

    public int idDropped(int i, Random random)
    {
        return Block.hopper.blockID;
    }

    public boolean blockActivated(World world, int i, int j, int k, EntityPlayer entityplayer)
    {
        if(world.multiplayerWorld)
        {
            return true;
        } else
        {
            TileEntityHopper tileentityhopper = (TileEntityHopper)world.getBlockTileEntity(i, j, k);
            if (tileentityhopper != null) {
                entityplayer.displayGUIHopper(tileentityhopper);
            }
            return true;
        }
    }

    /*public boolean itemsAreOnTop(World world, int i, int j, int k) {
        for (int u; u < world.getLoadedEntityList().size(); u++) {
            Entity entity = (Entity)world.getLoadedEntityList().get(u);
            if (entity.equals(new EntityItem(world))) {
                if (entity.posX + 0.125F > i && entity.posX + 0.125F < i + 1 && entity.posY > j + 0.5F && entity.posY < j + 1.5F && entity.posZ + 0.125F > k && entity.posZ + 0.125F < k + 1) {
                    entity.kill();
                }
            }
        }
    }*/

    public void onNeighborBlockChange(World world, int i, int j, int k, int l)
    {
        if(l > 0 && Block.blocksList[l].canProvidePower())
        {
            boolean flag = world.isBlockIndirectlyGettingPowered(i, j, k);
            TileEntityHopper tileentityhopper = (TileEntityHopper)world.getBlockTileEntity(i, j, k);
            tileentityhopper.setPowered(flag);
            if(flag)
            {
                world.scheduleBlockUpdate(i, j, k, blockID, tickRate());
            }
        }
    }

    public void updateItemFlow(World world, int i, int j, int k) {
        
        TileEntityHopper tileentityhopper = (TileEntityHopper)world.getBlockTileEntity(i, j, k);
        TileEntity overTileEntity = world.getBlockTileEntity(i, j + 1, k);
        TileEntity underTileEntity = world.getBlockTileEntity(i, j - 1, k);
        
        if (underTileEntity != null) {
            tileentityhopper.moveFirstAvailableItem(world);
        }

        if (overTileEntity != null) {
            tileentityhopper.getFirstAvailableItem(world);
        }
    }

    public void updateTick(World world, int i, int j, int k, Random random) {}

    protected TileEntity getBlockEntity()
    {
        return new TileEntityHopper();
    }

    public void onBlockAdded(World world, int i, int j, int k) {
        world.addHopper(i, j, k);
        super.onBlockAdded(world, i, j, k);
    }

    public void onBlockRemoval(World world, int i, int j, int k)
    {
        TileEntityHopper tileentityhopper = (TileEntityHopper)world.getBlockTileEntity(i, j, k);
label0:
        for(int l = 0; l < tileentityhopper.getSizeInventory(); l++)
        {
            ItemStack itemstack = tileentityhopper.getStackInSlot(l);
            if(itemstack == null)
            {
                continue;
            }
            float f = random.nextFloat() * 0.8F + 0.1F;
            float f1 = random.nextFloat() * 0.8F + 0.1F;
            float f2 = random.nextFloat() * 0.8F + 0.1F;
            do
            {
                if(itemstack.stackSize <= 0)
                {
                    continue label0;
                }
                int i1 = random.nextInt(21) + 10;
                if(i1 > itemstack.stackSize)
                {
                    i1 = itemstack.stackSize;
                }
                itemstack.stackSize -= i1;
                EntityItem entityitem = new EntityItem(world, (float)i + f, (float)j + f1, (float)k + f2, new ItemStack(itemstack.itemID, i1, itemstack.getItemDamage()));
                float f3 = 0.05F;
                entityitem.motionX = (float)random.nextGaussian() * f3;
                entityitem.motionY = (float)random.nextGaussian() * f3 + 0.2F;
                entityitem.motionZ = (float)random.nextGaussian() * f3;
                world.entityJoinedWorld(entityitem);
            } while(true);
        }

        world.removeHopper(i, j, k);

        super.onBlockRemoval(world, i, j, k);
    }

    public boolean isOpaqueCube()
    {
        return false;
    }

    public boolean chestOpenable() {
        return true;
    }

    private Random random;
}
