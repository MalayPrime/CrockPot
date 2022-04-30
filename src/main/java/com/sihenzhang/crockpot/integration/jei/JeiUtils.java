package com.sihenzhang.crockpot.integration.jei;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IFocus;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("ALL")
public final class JeiUtils {
    public static List<List<ItemStack>> getPagedIngredients(IRecipeLayout recipeLayout, IIngredients ingredients, int size, boolean input) {
        List<List<ItemStack>> ingredientStacks;
        if (input) {
            ingredientStacks = ingredients.getInputs(VanillaTypes.ITEM);
        } else {
            ingredientStacks = ingredients.getOutputs(VanillaTypes.ITEM);
        }
        IFocus<ItemStack> focus = recipeLayout.getFocus(VanillaTypes.ITEM);
        if (focus != null && ((input && focus.getMode() == IFocus.Mode.INPUT) || (!input && focus.getMode() == IFocus.Mode.OUTPUT))) {
            ingredientStacks = ingredientStacks.stream().filter(list -> ItemStack.isSame(list.get(0), focus.getValue())).collect(Collectors.toList());
        }
        if (ingredientStacks.size() > size) {
            List<List<ItemStack>> pagedIngredients = new ArrayList<>();
            for (int i = 0; i < size; i++) {
                pagedIngredients.add(new ArrayList<>(ingredientStacks.get(i)));
            }
            int pages = (int) Math.ceil((double) ingredientStacks.size() / size);
            for (int i = 1; i < pages; i++) {
                for (int j = 0; j < size; j++) {
                    pagedIngredients.get(j).add(i * size + j < ingredientStacks.size() ? ingredientStacks.get(i * size + j).get(0) : null);
                }
            }
            ingredientStacks = pagedIngredients;
        }
        return ingredientStacks;
    }
}
