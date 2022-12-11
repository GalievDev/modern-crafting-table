package net.galievdev.moderncraftingtable.block;

import net.fabricmc.fabric.api.registry.FuelRegistry;
import net.galievdev.moderncraftingtable.item.inventory.ImplementedInventory;
import net.galievdev.moderncraftingtable.recipe.ModernWorkbenchRecipe;
import net.galievdev.moderncraftingtable.screen.MWScreenHandler;
import net.galievdev.moderncraftingtable.setup.BlockEntityReg;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class ModernWorkbenchBlockEntity extends BlockEntity implements NamedScreenHandlerFactory, ImplementedInventory {
    private final DefaultedList<ItemStack> inventory =
            DefaultedList.ofSize(10, ItemStack.EMPTY);

    protected final PropertyDelegate propertyDelegate;
    private int progress = 0;
    private int maxProgress = 72;
    private int time = 0;
    private int maxTime = 0;

    public ModernWorkbenchBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityReg.MODERN_WORKBENCH, pos, state);
        this.propertyDelegate = new PropertyDelegate() {
            public int get(int index) {
                return switch (index) {
                    case 0 -> ModernWorkbenchBlockEntity.this.progress;
                    case 1 -> ModernWorkbenchBlockEntity.this.maxProgress;
                    case 2 -> ModernWorkbenchBlockEntity.this.time;
                    default -> 0;
                };
            }

            public void set(int index, int value) {
                switch (index) {
                    case 0 -> ModernWorkbenchBlockEntity.this.progress = value;
                    case 1 -> ModernWorkbenchBlockEntity.this.maxProgress = value;
                    case 2 -> ModernWorkbenchBlockEntity.this.time = value;
                }
            }

            public int size() {
                return 10;
            }
        };
    }

    @Override
    public DefaultedList<ItemStack> getItems() {
        return inventory;
    }

    @Override
    public Text getDisplayName() {
        return new LiteralText("Modern Workbench");
    }

    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
        return new MWScreenHandler(syncId, inv);
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        Inventories.writeNbt(nbt, inventory);
        nbt.putInt("workbench.progress", progress);
        nbt.putInt("workbench.time", time);
        nbt.putInt("workbench.maxTime", maxTime);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        Inventories.readNbt(nbt, inventory);
        super.readNbt(nbt);
        progress = nbt.getInt("workbench.progress");
        time = nbt.getInt("workbench.time");
        maxTime = nbt.getInt("workbench.maxTime");
    }


    public static void tick(World world, BlockPos pos, BlockState state, ModernWorkbenchBlockEntity entity) {
        if(isConsumingFuel(entity)) {
            entity.time--;
        }

        if(hasRecipe(entity)) {
            if(isConsumingFuel(entity)) {
                entity.progress++;
                if(entity.progress > entity.maxProgress) {
                    craftItem(entity);
                }
            }
        } else {
            entity.resetProgress();
        }
    }

    private static boolean isConsumingFuel(ModernWorkbenchBlockEntity entity) {
        return entity.time > 0;
    }

    private static boolean hasRecipe(ModernWorkbenchBlockEntity entity) {
        World world = entity.world;
        SimpleInventory inventory = new SimpleInventory(entity.inventory.size());
        for (int i = 0; i < entity.inventory.size(); i++) {
            inventory.setStack(i, entity.getStack(i));
        }

        assert world != null;
        Optional<ModernWorkbenchRecipe> match = world.getRecipeManager()
                .getFirstMatch(ModernWorkbenchRecipe.Type.INSTANCE, inventory, world);

        return match.isPresent() && canInsertAmountIntoOutputSlot(inventory)
                && canInsertItemIntoOutputSlot(inventory, match.get().getOutput());
    }

    private static void craftItem(ModernWorkbenchBlockEntity entity) {
        World world = entity.world;
        SimpleInventory inventory = new SimpleInventory(entity.inventory.size());
        for (int i = 0; i < entity.inventory.size(); i++) {
            inventory.setStack(i, entity.getStack(i));
        }

        assert world != null;
        Optional<ModernWorkbenchRecipe> match = world.getRecipeManager()
                .getFirstMatch(ModernWorkbenchRecipe.Type.INSTANCE, inventory, world);

        if(match.isPresent()) {
            entity.removeStack(1,1);
            entity.removeStack(2,1);

            entity.setStack(3, new ItemStack(match.get().getOutput().getItem(),
                    entity.getStack(3).getCount() + 1));

            entity.resetProgress();
        }
    }

    private void resetProgress() {
        this.progress = 0;
    }

    private static boolean canInsertItemIntoOutputSlot(SimpleInventory inventory, ItemStack output) {
        return inventory.getStack(3).getItem() == output.getItem() || inventory.getStack(3).isEmpty();
    }

    private static boolean canInsertAmountIntoOutputSlot(SimpleInventory inventory) {
        return inventory.getStack(3).getMaxCount() > inventory.getStack(3).getCount();
    }
}
