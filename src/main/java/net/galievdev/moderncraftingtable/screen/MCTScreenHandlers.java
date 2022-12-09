package net.galievdev.moderncraftingtable.screen;

import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.galievdev.moderncraftingtable.ModernCraftingTable;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;

public class MCTScreenHandlers {
    public static ScreenHandlerType<MWScreenHandler> MW_SCREEN_HANDLER;

    public static void registerAllScreenHandlers() {
        MW_SCREEN_HANDLER =
                ScreenHandlerRegistry.registerSimple(new Identifier(ModernCraftingTable.MOD_ID, "modern_workbench"),
                        MWScreenHandler::new);
    }
}
