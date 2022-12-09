package net.galievdev.moderncraftingtable.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.galievdev.moderncraftingtable.screen.MCTScreenHandlers;
import net.galievdev.moderncraftingtable.screen.MWScreen;
import net.galievdev.moderncraftingtable.setup.BlocksReg;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.render.RenderLayer;

@net.fabricmc.api.Environment(net.fabricmc.api.EnvType.CLIENT)
public class ModernCraftingTableClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        BlockRenderLayerMap.INSTANCE.putBlock(BlocksReg.MODERN_WORKBENCH, RenderLayer.getCutout());

        HandledScreens.register(MCTScreenHandlers.MW_SCREEN_HANDLER, MWScreen::new);
    }
}
