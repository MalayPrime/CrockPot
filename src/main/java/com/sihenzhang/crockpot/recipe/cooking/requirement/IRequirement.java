package com.sihenzhang.crockpot.recipe.cooking.requirement;

import com.google.gson.JsonElement;
import com.google.gson.JsonSyntaxException;
import com.sihenzhang.crockpot.recipe.cooking.CrockPotCookingRecipe;
import com.sihenzhang.crockpot.util.JsonUtils;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.GsonHelper;

import java.util.function.Predicate;

public interface IRequirement extends Predicate<CrockPotCookingRecipe.Wrapper> {
    static IRequirement fromJson(JsonElement json) {
        if (json == null || json.isJsonNull()) {
            throw new JsonSyntaxException("Json cannot be null");
        }
        var object = GsonHelper.convertToJsonObject(json, "requirement");
        var type = JsonUtils.getAsEnum(object, "type", RequirementType.class);
        return switch (type) {
            case CATEGORY_MAX -> RequirementCategoryMax.fromJson(object);
            case CATEGORY_MAX_EXCLUSIVE -> RequirementCategoryMaxExclusive.fromJson(object);
            case CATEGORY_MIN -> RequirementCategoryMin.fromJson(object);
            case CATEGORY_MIN_EXCLUSIVE -> RequirementCategoryMinExclusive.fromJson(object);
            case MUST_CONTAIN_INGREDIENT -> RequirementMustContainIngredient.fromJson(object);
            case MUST_CONTAIN_INGREDIENT_LESS_THAN -> RequirementMustContainIngredientLessThan.fromJson(object);
            case COMBINATION_AND -> RequirementCombinationAnd.fromJson(object);
            case COMBINATION_OR -> RequirementCombinationOr.fromJson(object);
        };
    }

    JsonElement toJson();

    static IRequirement fromNetwork(FriendlyByteBuf buffer) {
        var type = buffer.readEnum(RequirementType.class);
        return switch (type) {
            case CATEGORY_MAX -> RequirementCategoryMax.fromNetwork(buffer);
            case CATEGORY_MAX_EXCLUSIVE -> RequirementCategoryMaxExclusive.fromNetwork(buffer);
            case CATEGORY_MIN -> RequirementCategoryMin.fromNetwork(buffer);
            case CATEGORY_MIN_EXCLUSIVE -> RequirementCategoryMinExclusive.fromNetwork(buffer);
            case MUST_CONTAIN_INGREDIENT -> RequirementMustContainIngredient.fromNetwork(buffer);
            case MUST_CONTAIN_INGREDIENT_LESS_THAN -> RequirementMustContainIngredientLessThan.fromNetwork(buffer);
            case COMBINATION_AND -> RequirementCombinationAnd.fromNetwork(buffer);
            case COMBINATION_OR -> RequirementCombinationOr.fromNetwork(buffer);
        };
    }

    void toNetwork(FriendlyByteBuf buffer);
}
