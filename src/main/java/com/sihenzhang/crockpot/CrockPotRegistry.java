package com.sihenzhang.crockpot;

import com.sihenzhang.crockpot.base.CrockPotDamageSource;
import com.sihenzhang.crockpot.block.*;
import com.sihenzhang.crockpot.container.CrockPotContainer;
import com.sihenzhang.crockpot.item.CrockPotBlockItem;
import com.sihenzhang.crockpot.item.CrockPotCropsBlockItem;
import com.sihenzhang.crockpot.item.CrockPotFoodCategoryItem;
import com.sihenzhang.crockpot.item.food.*;
import com.sihenzhang.crockpot.tile.CrockPotTileEntity;
import net.minecraft.block.Block;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.Item;
import net.minecraft.potion.Effects;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Objects;

@SuppressWarnings("ALL")
public final class CrockPotRegistry {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, CrockPot.MOD_ID);
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, CrockPot.MOD_ID);
    public static final DeferredRegister<TileEntityType<?>> TILES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, CrockPot.MOD_ID);
    public static final DeferredRegister<ContainerType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, CrockPot.MOD_ID);

    // Pots
    public static RegistryObject<Block> crockPotBasicBlock = BLOCKS.register("crock_pot_basic", () -> new CrockPotBlock() {
        @Override
        public int getPotLevel() {
            return 0;
        }
    });
    public static RegistryObject<Item> crockPotBasicBlockItem = ITEMS.register("crock_pot_basic", () -> new CrockPotBlockItem(crockPotBasicBlock.get()));
    public static RegistryObject<Block> crockPotAdvancedBlock = BLOCKS.register("crock_pot_advanced", () -> new CrockPotBlock() {
        @Override
        public int getPotLevel() {
            return 1;
        }
    });
    public static RegistryObject<Item> crockPotAdvancedBlockItem = ITEMS.register("crock_pot_advanced", () -> new CrockPotBlockItem(crockPotAdvancedBlock.get()));
    public static RegistryObject<Block> crockPotUltimateBlock = BLOCKS.register("crock_pot_ultimate", () -> new CrockPotBlock() {
        @Override
        public int getPotLevel() {
            return 2;
        }
    });
    public static RegistryObject<Item> crockPotUltimateBlockItem = ITEMS.register("crock_pot_ultimate", () -> new CrockPotBlockItem(crockPotUltimateBlock.get()));
    public static RegistryObject<TileEntityType<CrockPotTileEntity>> crockPotTileEntity = TILES.register("crock_pot", () -> TileEntityType.Builder.create(CrockPotTileEntity::new, crockPotBasicBlock.get(), crockPotAdvancedBlock.get(), crockPotUltimateBlock.get()).build(null));
    public static RegistryObject<ContainerType<CrockPotContainer>> crockPotContainer = CONTAINERS.register("crock_pot", () -> IForgeContainerType.create((windowId, inv, data) -> {
        BlockPos pos = data.readBlockPos();
        TileEntity tileEntity = inv.player.world.getTileEntity(pos);
        return new CrockPotContainer(windowId, inv, (CrockPotTileEntity) Objects.requireNonNull(tileEntity));
    }));

    // Crops
    public static RegistryObject<Block> asparagusBlock = BLOCKS.register("asparaguses", AsparagusBlock::new);
    public static RegistryObject<Item> asparagus = ITEMS.register("asparagus", () -> new CrockPotCropsBlockItem(asparagusBlock.get(), 3, 0.6F));
    public static RegistryObject<Block> cornBlock = BLOCKS.register("corns", CornBlock::new);
    public static RegistryObject<Item> cornSeeds = ITEMS.register("corn_seeds", () -> new CrockPotCropsBlockItem(cornBlock.get()));
    public static RegistryObject<Item> corn = ITEMS.register("corn", () -> CrockPotFood.builder().hunger(3).saturation(0.6F).build());
    public static RegistryObject<Item> popcorn = ITEMS.register("popcorn", () -> CrockPotFood.builder().hunger(3).saturation(0.6F).duration(FoodUseDuration.FAST).build());
    public static RegistryObject<Block> onionBlock = BLOCKS.register("onions", OnionBlock::new);
    public static RegistryObject<Item> onion = ITEMS.register("onion", () -> new CrockPotCropsBlockItem(onionBlock.get(), 3, 0.6F));
    public static RegistryObject<Block> tomatoBlock = BLOCKS.register("tomatoes", TomatoBlock::new);
    public static RegistryObject<Item> tomatoSeeds = ITEMS.register("tomato_seeds", () -> new CrockPotCropsBlockItem(tomatoBlock.get()));
    public static RegistryObject<Item> tomato = ITEMS.register("tomato", () -> CrockPotFood.builder().hunger(3).saturation(0.6F).build());

    // Materials
    public static RegistryObject<Item> milkBottle = ITEMS.register("milk_bottle", () -> CrockPotFood.builder().hunger(0).saturation(0.0F).setAlwaysEdible().setDrink().tooltip("milk_bottle").build());
    public static RegistryObject<Item> syrup = ITEMS.register("syrup", () -> CrockPotFood.builder().hunger(1).saturation(0.3F).setDrink().build());

    // Foods
    public static RegistryObject<Item> asparagusSoup = ITEMS.register("asparagus_soup", () -> CrockPotFood.builder().hunger(4).saturation(0.3F).duration(FoodUseDuration.FAST).setAlwaysEdible().setDrink().removePotion(Effects.WEAKNESS).removePotion(Effects.MINING_FATIGUE).removePotion(Effects.BLINDNESS).removePotion(Effects.HUNGER).build());
    public static RegistryObject<Item> avaj = ITEMS.register("avaj", Avaj::new);
    public static RegistryObject<Item> baconEggs = ITEMS.register("bacon_eggs", () -> CrockPotFood.builder().hunger(12).saturation(0.8F).heal(4.0F).build());
    public static RegistryObject<Item> boneSoup = ITEMS.register("bone_soup", () -> CrockPotFood.builder().hunger(10).saturation(0.6F).effect(Effects.ABSORPTION, 2 * 60 * 20, 2).build());
    public static RegistryObject<Item> boneStew = ITEMS.register("bone_stew", () -> CrockPotFood.builder().hunger(20).saturation(0.3F).duration(FoodUseDuration.SUPER_SLOW).effect(Effects.INSTANT_HEALTH, 1, 1).build());
    public static RegistryObject<Item> californiaRoll = ITEMS.register("california_roll", () -> CrockPotFood.builder().hunger(8).saturation(0.6F).effect(Effects.DOLPHINS_GRACE, 5 * 20).build());
    public static RegistryObject<Item> candy = ITEMS.register("candy", Candy::new);
    public static RegistryObject<Item> ceviche = ITEMS.register("ceviche", () -> CrockPotFood.builder().hunger(7).saturation(0.7F).setAlwaysEdible().effect(Effects.SPEED, 20 * 20, 2).effect(Effects.RESISTANCE, 20 * 20, 1).effect(Effects.ABSORPTION, 20 * 20, 2).build());
    public static RegistryObject<Item> fishSticks = ITEMS.register("fish_sticks", () -> CrockPotFood.builder().hunger(7).saturation(0.7F).effect(Effects.REGENERATION, 30 * 20, 1).build());
    public static RegistryObject<Item> fishTacos = ITEMS.register("fish_tacos", () -> CrockPotFood.builder().hunger(8).saturation(0.9F).build());
    public static RegistryObject<Item> flowerSalad = ITEMS.register("flower_salad", FlowerSalad::new);
    public static RegistryObject<Item> fruitMedley = ITEMS.register("fruit_medley", () -> CrockPotFood.builder().hunger(8).saturation(0.4F).effect(Effects.SPEED, 3 * 60 * 20).build());
    public static RegistryObject<Item> gazpacho = ITEMS.register("gazpacho", () -> CrockPotFood.builder().hunger(6).saturation(0.4F).duration(FoodUseDuration.FAST).setAlwaysEdible().setDrink().effect(Effects.FIRE_RESISTANCE, 8 * 60 * 20).build());
    public static RegistryObject<Item> honeyHam = ITEMS.register("honey_ham", () -> CrockPotFood.builder().hunger(12).saturation(0.8F).effect(Effects.REGENERATION, 20 * 20).effect(Effects.ABSORPTION, 60 * 20, 1).heal(6.0F).build());
    public static RegistryObject<Item> honeyNuggets = ITEMS.register("honey_nuggets", () -> CrockPotFood.builder().hunger(8).saturation(0.3F).effect(Effects.REGENERATION, 5 * 20).build());
    public static RegistryObject<Item> hotChili = ITEMS.register("hot_chili", () -> CrockPotFood.builder().hunger(9).saturation(0.8F).effect(Effects.STRENGTH, (60 + 30) * 20).effect(Effects.HASTE, (60 + 30) * 20).build());
    public static RegistryObject<Item> hotCocoa = ITEMS.register("hot_cocoa", () -> CrockPotFood.builder().hunger(2).saturation(0.1F).setAlwaysEdible().setDrink().effect(Effects.SPEED, 8 * 60 * 20, 1).removePotion(Effects.SLOWNESS).removePotion(Effects.MINING_FATIGUE).build());
    public static RegistryObject<Item> iceCream = ITEMS.register("ice_cream", IceCream::new);
    public static RegistryObject<Item> icedTea = ITEMS.register("iced_tea", () -> CrockPotFood.builder().hunger(3).saturation(0.1F).duration(FoodUseDuration.FAST).setAlwaysEdible().setDrink().effect(Effects.SPEED, 10 * 60 * 20, 1).effect(Effects.JUMP_BOOST, 5 * 60 * 20, 1).build());
    public static RegistryObject<Item> jammyPreserves = ITEMS.register("jammy_preserves", () -> CrockPotFood.builder().hunger(6).saturation(0.3F).duration(FoodUseDuration.FAST).build());
    public static RegistryObject<Item> kabobs = ITEMS.register("kabobs", () -> CrockPotFood.builder().hunger(7).saturation(0.7F).build());
    public static RegistryObject<Item> meatBalls = ITEMS.register("meat_balls", () -> CrockPotFood.builder().hunger(9).saturation(0.3F).build());
    public static RegistryObject<Item> monsterLasagna = ITEMS.register("monster_lasagna", () -> CrockPotFood.builder().hunger(7).saturation(0.2F).effect(Effects.HUNGER, 15 * 20).effect(Effects.POISON, 2 * 20).damage(CrockPotDamageSource.MONSTER_FOOD, 6.0F).build());
    public static RegistryObject<Item> monsterTartare = ITEMS.register("monster_tartare", () -> CrockPotFood.builder().hunger(8).saturation(0.7F).effect(Effects.STRENGTH, (60 + 30) * 20, 1).build());
    public static RegistryObject<Item> moqueca = ITEMS.register("moqueca", () -> CrockPotFood.builder().hunger(14).saturation(0.7F).duration(FoodUseDuration.SLOW).effect(Effects.REGENERATION, 2 * 60 * 20, 1).heal(6.0F).build());
    public static RegistryObject<Item> perogies = ITEMS.register("perogies", () -> CrockPotFood.builder().hunger(8).saturation(0.8F).heal(6.0F).build());
    public static RegistryObject<Item> potatoSouffle = ITEMS.register("potato_souffle", () -> CrockPotFood.builder().hunger(8).saturation(0.7F).effect(Effects.RESISTANCE, (60 + 30) * 20, 1).build());
    public static RegistryObject<Item> potatoTornado = ITEMS.register("potato_tornado", () -> CrockPotFood.builder().hunger(5).saturation(0.7F).duration(FoodUseDuration.FAST).removePotion(Effects.HUNGER).build());
    public static RegistryObject<Item> powCake = ITEMS.register("pow_cake", () -> CrockPotFood.builder().hunger(2).saturation(0.1F).setAlwaysEdible().damage(CrockPotDamageSource.POW_CAKE, 1.0F).build());
    public static RegistryObject<Item> pumpkinCookie = ITEMS.register("pumpkin_cookie", () -> CrockPotFood.builder().hunger(8).saturation(0.6F).duration(FoodUseDuration.FAST).removePotion(Effects.HUNGER).build());
    public static RegistryObject<Item> ratatouille = ITEMS.register("ratatouille", () -> CrockPotFood.builder().hunger(6).saturation(0.3F).duration(FoodUseDuration.FAST).build());
    public static RegistryObject<Item> salsa = ITEMS.register("salsa", () -> CrockPotFood.builder().hunger(7).saturation(0.8F).duration(FoodUseDuration.FAST).effect(Effects.HASTE, 6 * 60 * 20).build());
    public static RegistryObject<Item> seafoodGumbo = ITEMS.register("seafood_gumbo", () -> CrockPotFood.builder().hunger(9).saturation(0.7F).duration(FoodUseDuration.FAST).effect(Effects.REGENERATION, 45 * 20, 1).build());
    public static RegistryObject<Item> surfNTurf = ITEMS.register("surf_n_turf", () -> CrockPotFood.builder().hunger(8).saturation(1.2F).setAlwaysEdible().effect(Effects.ABSORPTION, 2 * 60 * 20).heal(8.0F).build());
    public static RegistryObject<Item> taffy = ITEMS.register("taffy", () -> CrockPotFood.builder().hunger(5).saturation(0.4F).duration(FoodUseDuration.FAST).effect(Effects.LUCK, 2 * 60 * 20).damage(CrockPotDamageSource.TAFFY, 1.0F).removePotion(Effects.POISON).build());
    public static RegistryObject<Item> tea = ITEMS.register("tea", () -> CrockPotFood.builder().hunger(3).saturation(0.6F).duration(FoodUseDuration.FAST).setAlwaysEdible().setDrink().effect(Effects.SPEED, 10 * 60 * 20, 1).effect(Effects.HASTE, 5 * 60 * 20, 1).build());
    public static RegistryObject<Item> tropicalBouillabaisse = ITEMS.register("tropical_bouillabaisse", () -> CrockPotFood.builder().hunger(7).saturation(0.6F).duration(FoodUseDuration.FAST).setAlwaysEdible().effect(Effects.SPEED, 5 * 60 * 20).effect(Effects.DOLPHINS_GRACE, 5 * 60 * 20).effect(Effects.WATER_BREATHING, 5 * 60 * 20).cooldown(20).build());
    public static RegistryObject<Item> turkeyDinner = ITEMS.register("turkey_dinner", () -> CrockPotFood.builder().hunger(12).saturation(0.8F).effect(Effects.RESISTANCE, 3 * 60 * 20).build());
    public static RegistryObject<Item> vegStinger = ITEMS.register("veg_stinger", () -> CrockPotFood.builder().hunger(6).saturation(0.3F).duration(FoodUseDuration.FAST).setAlwaysEdible().setDrink().effect(Effects.NIGHT_VISION, 6 * 60 * 20).build());
    public static RegistryObject<Item> watermelonIcle = ITEMS.register("watermelon_icle", () -> CrockPotFood.builder().hunger(5).saturation(0.4F).duration(FoodUseDuration.FAST).effect(Effects.SPEED, 3 * 60 * 20).effect(Effects.JUMP_BOOST, 3 * 60 * 20).removePotion(Effects.SLOWNESS).build());
    public static RegistryObject<Item> wetGoop = ITEMS.register("wet_goop", () -> CrockPotFood.builder().hunger(0).saturation(0.0F).duration(FoodUseDuration.SUPER_SLOW).setAlwaysEdible().effect(Effects.NAUSEA, 10 * 20).tooltip("wet_goop", TextFormatting.ITALIC, TextFormatting.GRAY).build());

    // Food Categories
    public static RegistryObject<Item> foodCategoryMeat = ITEMS.register("food_category_meat", CrockPotFoodCategoryItem::new);
    public static RegistryObject<Item> foodCategoryMonster = ITEMS.register("food_category_monster", CrockPotFoodCategoryItem::new);
    public static RegistryObject<Item> foodCategoryFish = ITEMS.register("food_category_fish", CrockPotFoodCategoryItem::new);
    public static RegistryObject<Item> foodCategoryEgg = ITEMS.register("food_category_egg", CrockPotFoodCategoryItem::new);
    public static RegistryObject<Item> foodCategoryFruit = ITEMS.register("food_category_fruit", CrockPotFoodCategoryItem::new);
    public static RegistryObject<Item> foodCategoryVeggie = ITEMS.register("food_category_veggie", CrockPotFoodCategoryItem::new);
    public static RegistryObject<Item> foodCategoryDairy = ITEMS.register("food_category_dairy", CrockPotFoodCategoryItem::new);
    public static RegistryObject<Item> foodCategorySweetener = ITEMS.register("food_category_sweetener", CrockPotFoodCategoryItem::new);
    public static RegistryObject<Item> foodCategoryFrozen = ITEMS.register("food_category_frozen", CrockPotFoodCategoryItem::new);
    public static RegistryObject<Item> foodCategoryInedible = ITEMS.register("food_category_inedible", CrockPotFoodCategoryItem::new);
}