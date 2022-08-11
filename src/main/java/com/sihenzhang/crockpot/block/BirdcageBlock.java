package com.sihenzhang.crockpot.block;

import com.sihenzhang.crockpot.CrockPotRegistry;
import com.sihenzhang.crockpot.base.FoodCategory;
import com.sihenzhang.crockpot.block.entity.BirdcageBlockEntity;
import com.sihenzhang.crockpot.recipe.FoodValuesDefinition;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Parrot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;
import java.util.Optional;

public class BirdcageBlock extends BaseEntityBlock {
    public static final VoxelShape LOWER_SHAPE = Shapes.or(
            Block.box(0.0D, 0.0D, 0.0D, 16.0D, 2.0D, 16.0D),
            Block.box(4.0D, 2.0D, 4.0D, 12.0D, 7.0D, 12.0D),
            Block.box(0.0D, 7.0D, 0.0D, 16.0D, 16.0D, 16.0D)
    );
    public static final VoxelShape UPPER_SHAPE = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 14.0D, 16.0D);
    public static final EnumProperty<DoubleBlockHalf> HALF = BlockStateProperties.DOUBLE_BLOCK_HALF;
    public static final BooleanProperty HANGING = BlockStateProperties.HANGING;

    public BirdcageBlock() {
        super(Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(5.0F, 6.0F).sound(SoundType.LANTERN).noOcclusion());
        this.registerDefaultState(stateDefinition.any().setValue(HALF, DoubleBlockHalf.LOWER).setValue(HANGING, false));
    }

    @Override
    @SuppressWarnings("deprecation")
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if (this.getBlockEntity(pLevel, pPos, pState) instanceof BirdcageBlockEntity birdcageBlockEntity) {
            var lowerPos = pState.getValue(HALF) == DoubleBlockHalf.LOWER ? pPos : pPos.below();
            var parrots = pLevel.getEntitiesOfClass(Parrot.class, new AABB(lowerPos.getX(), lowerPos.getY(), lowerPos.getZ(), lowerPos.getX() + 1.0D, lowerPos.getY() + 2.0D, lowerPos.getZ() + 1.0D));
            var stackInHand = pPlayer.getItemInHand(pHand);

            if (parrots.isEmpty()) {
                // no Parrot in the Birdcage, so put the Parrot into the Birdcage
                if (pHand == InteractionHand.MAIN_HAND && stackInHand.isEmpty()) {
                    var leftShoulderEntity = pPlayer.getShoulderEntityLeft();
                    var rightShoulderEntity = pPlayer.getShoulderEntityRight();
                    if (!leftShoulderEntity.isEmpty() || !rightShoulderEntity.isEmpty()) {
                        var isLeftShoulder = !leftShoulderEntity.isEmpty();
                        var optionalParrot = EntityType.create(isLeftShoulder ? leftShoulderEntity : rightShoulderEntity, pLevel).filter(entity -> entity instanceof Parrot).map(Parrot.class::cast);
                        var optionalBirdcage = Optional.ofNullable(CrockPotRegistry.BIRDCAGE_ENTITY.get().create(pLevel));
                        if (optionalParrot.isPresent() && optionalBirdcage.isPresent()) {
                            var parrot = optionalParrot.get();
                            var birdcage = optionalBirdcage.get();
                            if (!pLevel.isClientSide() && birdcageBlockEntity.captureParrot(pLevel, lowerPos, pPlayer, parrot, birdcage, isLeftShoulder)) {
                                return InteractionResult.SUCCESS;
                            }
                            return InteractionResult.CONSUME;
                        }
                    }
                }
            } else {
                // if player is sneaking and its main hand is empty, release the Parrot
                if (pHand == InteractionHand.MAIN_HAND && stackInHand.isEmpty() && pPlayer.isSteppingCarefully()) {
                    for (var parrot : parrots) {
                        if (pPlayer.getUUID().equals(parrot.getOwnerUUID())) {
                            if (!pLevel.isClientSide() && parrot.setEntityOnShoulder((ServerPlayer) pPlayer)) {
                                return InteractionResult.SUCCESS;
                            }
                            return InteractionResult.CONSUME;
                        }
                    }
                }

                if (birdcageBlockEntity.hasCooldown()) {
                    var foodValues = FoodValuesDefinition.getFoodValues(stackInHand.getItem(), pLevel.getRecipeManager());
                    // if item in hand is Meat, Parrot will lay eggs
                    if (foodValues.has(FoodCategory.MEAT)) {

                    }
                }
            }
        }
        return InteractionResult.PASS;
    }

    @Override
    @SuppressWarnings("deprecation")
    public BlockState updateShape(BlockState pState, Direction pDirection, BlockState pNeighborState, LevelAccessor pLevel, BlockPos pCurrentPos, BlockPos pNeighborPos) {
        if (pDirection == getNeighborDirection(pState)) {
            return pNeighborState.is(this) && pNeighborState.getValue(HALF) != pState.getValue(HALF) ? pState : Blocks.AIR.defaultBlockState();
        }
        return super.updateShape(pState, pDirection, pNeighborState, pLevel, pCurrentPos, pNeighborPos);
    }

    private static Direction getNeighborDirection(BlockState pState) {
        return pState.getValue(HALF) == DoubleBlockHalf.LOWER ? Direction.UP : Direction.DOWN;
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        boolean isHanging = pContext.getClickedFace() == Direction.DOWN;
        var state = this.defaultBlockState().setValue(HANGING, isHanging);
        var level = pContext.getLevel();
        var clickedPos = pContext.getClickedPos();
        if (isHanging) {
            if (clickedPos.getY() > level.getMinBuildHeight() + 1 && level.getBlockState(clickedPos.below()).canBeReplaced(pContext)) {
                return state.setValue(HALF, DoubleBlockHalf.UPPER);
            }
        } else {
            if (clickedPos.getY() < level.getMaxBuildHeight() - 1 && level.getBlockState(clickedPos.above()).canBeReplaced(pContext)) {
                return state.setValue(HALF, DoubleBlockHalf.LOWER);
            }
        }
        return null;
    }

    @Override
    public void setPlacedBy(Level pLevel, BlockPos pPos, BlockState pState, @Nullable LivingEntity pPlacer, ItemStack pStack) {
        var isHanging = pState.hasProperty(HANGING) && pState.getValue(HANGING);
        pLevel.setBlockAndUpdate(isHanging ? pPos.below() : pPos.above(), pState.setValue(HALF, isHanging ? DoubleBlockHalf.LOWER : DoubleBlockHalf.UPPER));
    }

    @Override
    @SuppressWarnings("deprecation")
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return pState.getValue(HALF) == DoubleBlockHalf.UPPER ? UPPER_SHAPE : LOWER_SHAPE;
    }

    @Override
    @SuppressWarnings("deprecation")
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(HALF, HANGING);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return pState.getValue(HALF) == DoubleBlockHalf.LOWER ? new BirdcageBlockEntity(pPos, pState) : null;
    }

    public BlockEntity getBlockEntity(BlockGetter pLevel, BlockPos pPos, BlockState pState) {
        return pLevel.getBlockEntity(pState.getValue(HALF) == DoubleBlockHalf.LOWER ? pPos : pPos.below());
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return pLevel.isClientSide() ? null : createTickerHelper(pBlockEntityType, CrockPotRegistry.BIRDCAGE_BLOCK_ENTITY.get(), BirdcageBlockEntity::serverTick);
    }
}
