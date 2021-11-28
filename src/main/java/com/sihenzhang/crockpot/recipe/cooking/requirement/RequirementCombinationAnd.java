package com.sihenzhang.crockpot.recipe.cooking.requirement;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.sihenzhang.crockpot.recipe.cooking.CrockPotCookingRecipeInput;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;

public class RequirementCombinationAnd implements IRequirement {
    private final IRequirement first, second;

    public RequirementCombinationAnd(IRequirement first, IRequirement second) {
        this.first = first;
        this.second = second;
    }

    public IRequirement getFirst() {
        return first;
    }

    public IRequirement getSecond() {
        return second;
    }

    @Override
    public boolean test(CrockPotCookingRecipeInput recipeInput) {
        return first.test(recipeInput) && second.test(recipeInput);
    }

    public static RequirementCombinationAnd fromJson(JsonObject object) {
        IRequirement first = IRequirement.fromJson(JSONUtils.getAsJsonObject(object, "first"));
        IRequirement second = IRequirement.fromJson(JSONUtils.getAsJsonObject(object, "second"));
        return new RequirementCombinationAnd(first, second);
    }

    @Override
    public JsonElement toJson() {
        JsonObject obj = new JsonObject();
        obj.addProperty("type", RequirementType.COMBINATION_AND.name());
        obj.add("first", first.toJson());
        obj.add("second", second.toJson());
        return obj;
    }

    public static RequirementCombinationAnd fromNetwork(PacketBuffer buffer) {
        return new RequirementCombinationAnd(IRequirement.fromNetwork(buffer), IRequirement.fromNetwork(buffer));
    }

    @Override
    public void toNetwork(PacketBuffer buffer) {
        buffer.writeEnum(RequirementType.COMBINATION_AND);
        first.toNetwork(buffer);
        second.toNetwork(buffer);
    }
}