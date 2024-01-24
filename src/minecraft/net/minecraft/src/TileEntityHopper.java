// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;

import java.util.List;
import java.util.Random;

// Referenced classes of package net.minecraft.src:
//            TileEntity, IInventory, ItemStack, NBTTagCompound, 
//            NBTTagList, World, EntityPlayer

public class TileEntityHopper extends TileEntity
    implements IInventory
{

    public TileEntityHopper()
    {
        hopperContents = new ItemStack[9];
        powered = false;
    }

    public int getSizeInventory()
    {
        return 9;
    }

    public ItemStack getStackInSlot(int i)
    {
        return hopperContents[i];
    }

    public void setPowered(boolean powered) {
        this.powered = powered;
    }

    public void moveFirstAvailableItem(World world) {
        if (powered) {
            return;
        }
        TileEntity tileEntity = world.getBlockTileEntity(xCoord, yCoord - 1, zCoord);
        ItemStack itemtobemoved;
        for (int y = 0; y < getSizeInventory(); y++) {
            itemtobemoved = hopperContents[y];
            if (itemtobemoved != null && itemtobemoved.itemID != 0) {
                for (int u = 0; u < tileEntity.getSizeInventory(); u++) {
                    ItemStack itemstack = tileEntity.getStackInSlot(u);
                    if (itemstack == null || (itemstack.itemID == itemtobemoved.itemID && itemstack.stackSize < itemstack.getItem().maxStackSize)) {
                        int newstacksize = 1;
                        if (itemstack != null) {
                            newstacksize += itemstack.stackSize;
                        }
                        tileEntity.setInventorySlotContents(u, new ItemStack(itemtobemoved.getItem(), newstacksize));
                        decrStackSize(y, 1);
                        return;
                    }
                }
            }
        }
    }

    public void getFirstAvailableItem(World world) {
        if (powered) {
            return;
        }
        TileEntity tileEntity = world.getBlockTileEntity(xCoord, yCoord + 1, zCoord);
        if (tileEntity != null) {
            if (world.getBlockId(xCoord, yCoord + 1, zCoord) == Block.hopper.blockID) {
                TileEntityHopper tileEntityHopper = (TileEntityHopper)world.getBlockTileEntity(xCoord, yCoord + 1, zCoord);
                if (!tileEntityHopper.powered) {
                    return;
                }
            }
            ItemStack itemtobemoved;
            for (int y = 0; y < tileEntity.getSizeInventory(); y++) {
                itemtobemoved = tileEntity.getStackInSlot(y);
                if (itemtobemoved != null && itemtobemoved.itemID != 0) {
                    for (int u = 0; u < getSizeInventory(); u++) {
                        ItemStack itemstack = hopperContents[u];
                        if (itemstack == null || (itemstack.itemID == itemtobemoved.itemID && itemstack.stackSize < itemstack.getItem().maxStackSize)) {
                            int newstacksize = 1;
                            if (itemstack != null) {
                                newstacksize += itemstack.stackSize;
                            }
                            setInventorySlotContents(u, new ItemStack(itemtobemoved.getItem(), newstacksize));
                            tileEntity.decrStackSize(y, 1);
                            return;
                        }
                    }
                }
            }
        } else {
            List<Entity> entityList = world.getLoadedEntityList();
            for (int u = 0; u < entityList.size(); u++) {
                Entity entity = entityList.get(u);
                if (entity.equals(new EntityItem(world))) {
                    EntityItem entityItem = (EntityItem)entityList.get(u);
                    if (entityItem.posX + 0.125F > xCoord && entityItem.posX + 0.125F < xCoord + 1 && entityItem.posY > yCoord && entityItem.posY < yCoord + 2 && entityItem.posZ + 0.125F > zCoord && entityItem.posZ + 0.125F < zCoord + 1) {

                        entityItem.kill();
                    }
                }
            }
        }
    }

    /*public moveFirstAvailableItem(TileEntityFurnace tileentityfurnace) {
        ItemStack firstItem;
        for (int u = 0; u < tileEntityMovingInto.)
    }*/

    public ItemStack decrStackSize(int i, int j)
    {
        if(hopperContents[i] != null)
        {
            if(hopperContents[i].stackSize <= j)
            {
                ItemStack itemstack = hopperContents[i];
                hopperContents[i] = null;
                onInventoryChanged();
                return itemstack;
            }
            ItemStack itemstack1 = hopperContents[i].splitStack(j);
            if(hopperContents[i].stackSize == 0)
            {
                hopperContents[i] = null;
            }
            onInventoryChanged();
            return itemstack1;
        } else
        {
            return null;
        }
    }
    
    public void setInventorySlotContents(int i, ItemStack itemstack)
    {
        hopperContents[i] = itemstack;
        if(itemstack != null && itemstack.stackSize > getInventoryStackLimit())
        {
            itemstack.stackSize = getInventoryStackLimit();
        }
        onInventoryChanged();
    }

    public String getInvName()
    {
        return "Hopper";
    }

    public void readFromNBT(NBTTagCompound nbttagcompound)
    {
        super.readFromNBT(nbttagcompound);
        NBTTagList nbttaglist = nbttagcompound.getTagList("Items");
        hopperContents = new ItemStack[getSizeInventory()];
        for(int i = 0; i < nbttaglist.tagCount(); i++)
        {
            NBTTagCompound nbttagcompound1 = (NBTTagCompound)nbttaglist.tagAt(i);
            int j = nbttagcompound1.getByte("Slot") & 0xff;
            if(j >= 0 && j < hopperContents.length)
            {
                hopperContents[j] = new ItemStack(nbttagcompound1);
            }
        }

    }

    public void writeToNBT(NBTTagCompound nbttagcompound)
    {
        super.writeToNBT(nbttagcompound);
        NBTTagList nbttaglist = new NBTTagList();
        for(int i = 0; i < hopperContents.length; i++)
        {
            if(hopperContents[i] != null)
            {
                NBTTagCompound nbttagcompound1 = new NBTTagCompound();
                nbttagcompound1.setByte("Slot", (byte)i);
                hopperContents[i].writeToNBT(nbttagcompound1);
                nbttaglist.setTag(nbttagcompound1);
            }
        }

        nbttagcompound.setTag("Items", nbttaglist);
    }

    public int getInventoryStackLimit()
    {
        return 64;
    }

    public boolean canInteractWith(EntityPlayer entityplayer)
    {
        if(worldObj.getBlockTileEntity(xCoord, yCoord, zCoord) != this)
        {
            return false;
        }
        return entityplayer.getDistanceSq((double)xCoord + 0.5D, (double)yCoord + 0.5D, (double)zCoord + 0.5D) <= 64D;
    }

    private boolean powered;
    private ItemStack hopperContents[];
}
