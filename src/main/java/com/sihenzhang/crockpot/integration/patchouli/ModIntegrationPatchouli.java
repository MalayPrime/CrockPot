package com.sihenzhang.crockpot.integration.patchouli;

import com.sihenzhang.crockpot.CrockPot;
import com.sihenzhang.crockpot.base.FoodCategory;
import vazkii.patchouli.api.PatchouliAPI;

public class ModIntegrationPatchouli {
    public static final String MOD_ID = "patchouli";

    public static void addConfigFlag() {
        for (FoodCategory category : FoodCategory.values()) {
            for (float value = 0.25F; value <= 4.0F; value += 0.25F) {
                PatchouliAPI.instance.setConfigFlag(CrockPot.MOD_ID + ":" + category.name().toLowerCase() + ":" + value, !CrockPot.FOOD_CATEGORY_MANAGER.getMatchingItems(category, value).isEmpty());
            }
        }
    }
}