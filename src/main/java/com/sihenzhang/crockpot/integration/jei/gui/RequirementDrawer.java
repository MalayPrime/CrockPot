package com.sihenzhang.crockpot.integration.jei.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.datafixers.util.Pair;
import com.sihenzhang.crockpot.CrockPot;
import com.sihenzhang.crockpot.base.FoodCategory;
import com.sihenzhang.crockpot.recipe.cooking.requirement.*;
import com.sihenzhang.crockpot.util.MathUtils;
import mezz.jei.api.gui.drawable.IDrawable;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.util.*;
import java.util.Map.Entry;
import java.util.function.Function;

public abstract class RequirementDrawer<T extends IRequirement> implements IDrawable {
    public static class DrawInfo {
        public ItemStack is;
        public int x;
        public int y;

        public DrawInfo(ItemStack is, int x, int y) {
            this.is = is;
            this.x = x;
            this.y = y;
        }
    }

    public static class RequirementCMD extends RequirementDrawer<RequirementCategoryMax> {
        private final String description;

        public RequirementCMD(RequirementCategoryMax requirement) {
            super(requirement);
            this.description = MathUtils.fuzzyEquals(requirement.getMax(), 0.0F) ? "No" : "≤ " + requirement.getMax();
        }

        @Override
        public int getWidth() {
            return 26 + Minecraft.getInstance().font.width(description);
        }

        @Override
        public int getHeight() {
            return 24;
        }

        @Override
        public void draw(RequirementCategoryMax requirement, MatrixStack matrixStack, int xOffset, int yOffset) {
            drawRequirementBackground(matrixStack, xOffset, yOffset, this.getWidth(), this.getHeight());
            Minecraft.getInstance().font.draw(matrixStack, description, MathUtils.fuzzyEquals(requirement.getMax(), 0.0F) ? xOffset + 4 : xOffset + 22, yOffset + 8, 0);
        }

        @Override
        public List<DrawInfo> drawItem(RequirementCategoryMax requirement, int xOffset, int yOffset) {
            return Arrays.asList(new DrawInfo(FoodCategory.getItemStack(requirement.getCategory()), MathUtils.fuzzyEquals(requirement.getMax(), 0.0F) ? xOffset + this.getWidth() - 20 : xOffset + 4, yOffset + 4));
        }


    }

    public static class RequirementCMED extends RequirementDrawer<RequirementCategoryMaxExclusive> {
        private final String description;

        public RequirementCMED(RequirementCategoryMaxExclusive requirement) {
            super(requirement);
            this.description = "< " + requirement.getMax();
        }

        @Override
        public int getWidth() {
            return 26 + Minecraft.getInstance().font.width(description);
        }

        @Override
        public int getHeight() {
            return 24;
        }

        @Override
        public void draw(RequirementCategoryMaxExclusive requirement, MatrixStack matrixStack, int xOffset,
                         int yOffset) {
            drawRequirementBackground(matrixStack, xOffset, yOffset, getWidth(), getHeight());
            Minecraft.getInstance().font.draw(matrixStack, description, xOffset + 22, yOffset + 8, 0);
        }

        @Override
        public List<DrawInfo> drawItem(RequirementCategoryMaxExclusive requirement, int xOffset, int yOffset) {
            return Arrays.asList(new DrawInfo(FoodCategory.getItemStack(requirement.getCategory()), xOffset + 4, yOffset + 4));
        }


    }

    public static class RequirementCND extends RequirementDrawer<RequirementCategoryMin> {


        public RequirementCND(RequirementCategoryMin requirement) {
            super(requirement);
        }

        @Override
        public int getWidth() {
            return 35;
        }

        @Override
        public int getHeight() {
            return 16;
        }

        @Override
        public void draw(RequirementCategoryMin requirement, MatrixStack matrixStack, int xOffset, int yOffset) {
            Minecraft.getInstance().font.draw(matrixStack, ">=" + requirement.getMin(), xOffset + 17, yOffset + 5, 0);
        }

        @Override
        public List<DrawInfo> drawItem(RequirementCategoryMin requirement, int xOffset, int yOffset) {
            return Arrays.asList(new DrawInfo(FoodCategory.getItemStack(requirement.getCategory()), xOffset, yOffset));
        }


    }

    public static class RequirementCNED extends RequirementDrawer<RequirementCategoryMinExclusive> {

        public RequirementCNED(RequirementCategoryMinExclusive requirement) {
            super(requirement);
        }

        @Override
        public int getWidth() {
            return 35;
        }

        @Override
        public int getHeight() {
            return 16;
        }

        @Override
        public void draw(RequirementCategoryMinExclusive requirement, MatrixStack matrixStack, int xOffset,
                         int yOffset) {
            Minecraft.getInstance().font.draw(matrixStack, ">" + requirement.getMin(), xOffset + 17, yOffset + 5, 0);
        }

        @Override
        public List<DrawInfo> drawItem(RequirementCategoryMinExclusive requirement, int xOffset, int yOffset) {
            return Arrays.asList(new DrawInfo(FoodCategory.getItemStack(requirement.getCategory()), xOffset, yOffset));
        }


    }

    public static class RequirementCMID extends RequirementDrawer<RequirementMustContainIngredient> {

        public RequirementCMID(RequirementMustContainIngredient requirement) {
            super(requirement);
        }

        @Override
        public int getWidth() {
            return 35;
        }

        @Override
        public int getHeight() {
            return 16;
        }

        @Override
        public void draw(RequirementMustContainIngredient requirement, MatrixStack matrixStack, int xOffset, int yOffset) {
            Minecraft.getInstance().font.draw(matrixStack, ">=" + requirement.getQuantity(), xOffset + 17, yOffset + 5, 0);
        }

        @Override
        public List<DrawInfo> drawItem(RequirementMustContainIngredient requirement, int xOffset, int yOffset) {
            return Arrays.asList(new DrawInfo(requirement.getIngredient().getItems()[0], xOffset, yOffset));
        }


    }

    public static class RequirementCMLD extends RequirementDrawer<RequirementMustContainIngredientLessThan> {

        public RequirementCMLD(RequirementMustContainIngredientLessThan requirement) {
            super(requirement);
        }

        @Override
        public int getWidth() {
            return 35;
        }

        @Override
        public int getHeight() {
            return 16;
        }

        @Override
        public void draw(RequirementMustContainIngredientLessThan requirement, MatrixStack matrixStack, int xOffset,
                         int yOffset) {
            Minecraft.getInstance().font.draw(matrixStack, "<" + requirement.getQuantity(), xOffset + 17, yOffset + 5, 0);
        }

        @Override
        public List<DrawInfo> drawItem(RequirementMustContainIngredientLessThan requirement, int xOffset, int yOffset) {
            return Arrays.asList(new DrawInfo(requirement.getIngredient().getItems()[0], xOffset, yOffset));
        }


    }

    public static class RequirementAnd extends RequirementDrawer<RequirementCombinationAnd> {
        Pair<IDrawable, List<DrawInfo>> left;
        Pair<IDrawable, List<DrawInfo>> right;

        public RequirementAnd(RequirementCombinationAnd requirement) {
            super(requirement);
        }

        @Override
        public int getWidth() {
            return Math.max(left.getFirst().getWidth(), right.getFirst().getWidth()) + 4;
        }

        @Override
        public int getHeight() {
            return left.getFirst().getHeight() + right.getFirst().getHeight() + 4;
        }

        @Override
        public void draw(RequirementCombinationAnd requirement, MatrixStack matrixStack, int xOffset,
                         int yOffset) {

            init(xOffset, yOffset);

            drawRequirementBackground(matrixStack, xOffset + 1, yOffset + 1, getWidth(), getHeight());
            xOffset += 2;
            yOffset += 2;
            left.getFirst().draw(matrixStack, xOffset, yOffset);
            //Minecraft.getInstance().font.draw(matrixStack,"&",xOffset+left.getFirst().getWidth(),yOffset+5,0);
            right.getFirst().draw(matrixStack, xOffset, yOffset + left.getFirst().getHeight());
        }

        public void init(int xOffset, int yOffset) {
            left = RequirementDrawer.drawItems(requirement.getFirst(), xOffset + 2, yOffset + 2);
            right = RequirementDrawer.drawItems(requirement.getSecond(), xOffset + 2, yOffset + 2 + left.getFirst().getHeight());
        }

        @Override
        public List<DrawInfo> drawItem(RequirementCombinationAnd requirement, int xOffset, int yOffset) {
            init(xOffset, yOffset);
            List<DrawInfo> all = new ArrayList<>();

            all.addAll(left.getSecond());
            all.addAll(right.getSecond());
            return all;
        }


    }

    public static class RequirementOr extends RequirementDrawer<RequirementCombinationOr> {
        Pair<IDrawable, List<DrawInfo>> left;
        Pair<IDrawable, List<DrawInfo>> right;

        public RequirementOr(RequirementCombinationOr requirement) {
            super(requirement);
        }

        @Override
        public int getWidth() {
            return left.getFirst().getWidth() + right.getFirst().getWidth() + 8;
        }

        @Override
        public int getHeight() {
            return Math.max(left.getFirst().getHeight(), right.getFirst().getHeight()) + 4;
        }

        @Override
        public void draw(RequirementCombinationOr requirement, MatrixStack matrixStack, int xOffset,
                         int yOffset) {
            init(xOffset, yOffset);

            drawRequirementBackground(matrixStack, xOffset + 1, yOffset + 1, getWidth(), getHeight());
            xOffset += 2;
            yOffset += 2;
            left.getFirst().draw(matrixStack, xOffset, yOffset);
            Minecraft.getInstance().font.draw(matrixStack, "/", xOffset + left.getFirst().getWidth(), yOffset + 5, 0);
            right.getFirst().draw(matrixStack, xOffset + left.getFirst().getWidth() + 5, yOffset);
        }

        public void init(int xOffset, int yOffset) {
            left = RequirementDrawer.drawItems(requirement.getFirst(), xOffset + 2, yOffset + 2);
            right = RequirementDrawer.drawItems(requirement.getSecond(), xOffset + left.getFirst().getWidth() + 7, yOffset + 2);
        }

        @Override
        public List<DrawInfo> drawItem(RequirementCombinationOr requirement, int xOffset, int yOffset) {
            init(xOffset, yOffset);
            List<DrawInfo> all = new ArrayList<>();

            all.addAll(left.getSecond());
            all.addAll(right.getSecond());
            return all;
        }


    }

    T requirement;
    static Map<Class<? extends IRequirement>, Function<IRequirement, RequirementDrawer<? extends IRequirement>>> drawers = new HashMap<>();

    static <T extends IRequirement> void register(Class<T> req, Function<T, RequirementDrawer<T>> cont) {
        drawers.put(req, (Function) cont);
    }

    static {
        register(RequirementCategoryMax.class, RequirementCMD::new);
        register(RequirementCategoryMaxExclusive.class, RequirementCMED::new);
        register(RequirementCategoryMin.class, RequirementCND::new);
        register(RequirementCategoryMinExclusive.class, RequirementCNED::new);
        register(RequirementMustContainIngredient.class, RequirementCMID::new);
        register(RequirementMustContainIngredientLessThan.class, RequirementCMLD::new);
        register(RequirementCombinationAnd.class, RequirementAnd::new);
        register(RequirementCombinationOr.class, RequirementOr::new);
    }

    public RequirementDrawer(T requirement) {
        this.requirement = requirement;
    }

    @Override
    public void draw(MatrixStack matrixStack, int xOffset, int yOffset) {
        this.draw(requirement, matrixStack, xOffset, yOffset);
    }

    public List<DrawInfo> drawItem(int xOffset, int yOffset) {
        return this.drawItem(requirement, xOffset, yOffset);

    }

    public abstract List<DrawInfo> drawItem(T requirement, int xOffset, int yOffset);

    public abstract void draw(T requirement, MatrixStack matrixStack, int xOffset, int yOffset);

    public static IDrawable drawRequirement(IRequirement requirement, MatrixStack matrixStack, int xOffset, int yOffset) {
        for (Entry<Class<? extends IRequirement>, Function<IRequirement, RequirementDrawer<? extends IRequirement>>> i : drawers.entrySet()) {
            if (i.getKey().isInstance(requirement)) {
                RequirementDrawer<? extends IRequirement> o = i.getValue().apply(requirement);
                o.draw(matrixStack, xOffset, yOffset);
                return o;
            }
        }
        return new IDrawable() {

            @Override
            public int getWidth() {
                return 0;
            }

            @Override
            public int getHeight() {
                return 0;
            }

            @Override
            public void draw(MatrixStack matrixStack, int xOffset, int yOffset) {
            }

        };
    }

    public static Pair<IDrawable, List<DrawInfo>> drawItems(IRequirement requirement, int xOffset, int yOffset) {
        for (Entry<Class<? extends IRequirement>, Function<IRequirement, RequirementDrawer<? extends IRequirement>>> i : drawers.entrySet()) {
            if (i.getKey().isInstance(requirement)) {
                RequirementDrawer<? extends IRequirement> o = i.getValue().apply(requirement);
                return new Pair<>(o, o.drawItem(xOffset, yOffset));
            }
        }
        return new Pair<>(new IDrawable() {

            @Override
            public int getWidth() {
                return 0;
            }

            @Override
            public int getHeight() {
                return 0;
            }

            @Override
            public void draw(MatrixStack matrixStack, int xOffset, int yOffset) {
            }

        }, Arrays.asList());
    }

    public static void drawRequirementBackground(MatrixStack matrixStack, int xOffset, int yOffset, int width, int height) {
        RenderSystem.enableAlphaTest();
        IDrawable drawable = new DrawableNineSliceResource(new ResourceLocation(CrockPot.MOD_ID, "textures/gui/jei/requirement_background.png"), 0, 0, 64, 64, width, height, 16, 16, 16, 16, 64, 64);
        drawable.draw(matrixStack, xOffset, yOffset);
        RenderSystem.disableAlphaTest();
    }
}