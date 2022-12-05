package net.galievdev.moderncraftingtable.setup;

import net.galievdev.moderncraftingtable.ModernCraftingTable;
import net.galievdev.moderncraftingtable.recipe.ModernWorkbenchRecipe;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class RecipesReg {
    public static void registerRecipes() {
        Registry.register(Registry.RECIPE_SERIALIZER, new Identifier(ModernCraftingTable.MOD_ID, ModernWorkbenchRecipe.Serializer.ID),
                ModernWorkbenchRecipe.Serializer.INSTANCE);
        Registry.register(Registry.RECIPE_TYPE, new Identifier(ModernCraftingTable.MOD_ID, ModernWorkbenchRecipe.Type.ID),
                ModernWorkbenchRecipe.Type.INSTANCE);
    }
}
