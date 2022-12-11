package net.galievdev.moderncraftingtable.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;

public class ModernWorkbenchRecipe implements Recipe<SimpleInventory> {
    private final Identifier id;
    private final ItemStack output;
    private final DefaultedList<Ingredient> inputs;
    private final int time;
    private final int tier;

    public ModernWorkbenchRecipe(Identifier id, ItemStack output, DefaultedList<Ingredient> inputs, int time, int tier) {
        this.id = id;
        this.output = output;
        this.inputs = inputs;
        this.time = time;
        this.tier = tier;
    }

    @Override
    public DefaultedList<Ingredient> getIngredients() {
        return inputs;
    }

    @Override
    public boolean matches(SimpleInventory inventory, World world) {
        if (world.isClient && tier != 0) {
            return false;
        }

        for (int i = 0; i <= inventory.size(); i++) {
            if (inputs.get(i).test(inventory.getStack(i))) {
                return true;
            }
        }
        return false;
    }

    @Override
    public ItemStack craft(SimpleInventory inventory) {
        return output;
    }

    @Override
    public boolean fits(int width, int height) {
        return true;
    }

    public int getTier() {
        return tier;
    }

    public int getTime(){
        return time;
    }

    @Override
    public ItemStack getOutput() {
        return output.copy();
    }

    @Override
    public Identifier getId() {
        return id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return Serializer.INSTANCE;
    }

    @Override
    public RecipeType<?> getType() {
        return Type.INSTANCE;
    }

    public static class Type implements RecipeType<ModernWorkbenchRecipe> {
        private Type() { }
        public static final Type INSTANCE = new Type();
        public static final String ID = "modern_workbench";
    }

    public static class Serializer implements RecipeSerializer<ModernWorkbenchRecipe> {
        public static final Serializer INSTANCE = new Serializer();
        public static final String ID = "modern_workbench";

        @Override
        public ModernWorkbenchRecipe read(Identifier id, JsonObject json) {

            ItemStack output = ShapedRecipe.outputFromJson(JsonHelper.getObject(json, "output"));

            int time = JsonHelper.getInt(json, "time");
            int tier = JsonHelper.getInt(json, "tier");
            JsonArray ingredients = JsonHelper.getArray(json, "inputs");
            DefaultedList<Ingredient> inputs = DefaultedList.ofSize(9, Ingredient.EMPTY);

            for (int i = 0; i < inputs.size(); i++) {
                inputs.set(i, Ingredient.fromJson(ingredients.get(i)));
            }

            return new ModernWorkbenchRecipe(id, output, inputs, time, tier);
        }

        @Override
        public ModernWorkbenchRecipe read(Identifier id, PacketByteBuf buf) {
            DefaultedList<Ingredient> inputs = DefaultedList.ofSize(buf.readInt(), Ingredient.EMPTY);
            int time = buf.readInt();
            int tier = buf.readInt();

            for (int i = 0; i < inputs.size(); i++) {
                inputs.set(i, Ingredient.fromPacket(buf));
            }

            ItemStack output = buf.readItemStack();
            return new ModernWorkbenchRecipe(id, output, inputs, time, tier);
        }

        @Override
        public void write(PacketByteBuf buf, ModernWorkbenchRecipe recipe) {
            buf.writeInt(recipe.getIngredients().size());
            for (Ingredient ing : recipe.getIngredients()) {
                ing.write(buf);
            }
            buf.writeVarInt(recipe.time);
            buf.writeInt(recipe.tier);
            buf.writeItemStack(recipe.getOutput());
        }
    }
}
