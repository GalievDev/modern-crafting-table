package net.galievdev.moderncraftingtable;

import net.fabricmc.api.ModInitializer;
import net.galievdev.moderncraftingtable.setup.BlocksReg;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ModernCraftingTable implements ModInitializer {
    public static final String MOD_ID = "moderncraftingtable";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);
    @Override
    public void onInitialize() {
        BlocksReg.registerModBlocks();
    }
}
