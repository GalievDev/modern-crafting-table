package net.galievdev.moderncraftingtable.setup;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.galievdev.moderncraftingtable.ModernCraftingTable;
import net.galievdev.moderncraftingtable.block.ModernWorkbenchBlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class BlockEntityReg {
    public static BlockEntityType<ModernWorkbenchBlockEntity> MODERN_WORKBENCH;

    public static void registerAllBlockEntities() {
        MODERN_WORKBENCH = Registry.register(Registry.BLOCK_ENTITY_TYPE,
                new Identifier(ModernCraftingTable.MOD_ID, "mythril_blaster"),
                FabricBlockEntityTypeBuilder.create(ModernWorkbenchBlockEntity::new,
                        BlocksReg.MODERN_WORKBENCH).build(null));
    }
}
