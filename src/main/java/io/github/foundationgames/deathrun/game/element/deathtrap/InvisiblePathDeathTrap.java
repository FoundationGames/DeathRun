package io.github.foundationgames.deathrun.game.element.deathtrap;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.foundationgames.deathrun.game.element.DeathTrap;
import io.github.foundationgames.deathrun.game.state.DRGame;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import xyz.nucleoid.map_templates.BlockBounds;

public class InvisiblePathDeathTrap extends ResettingDeathTrap {
    public static final MapCodec<InvisiblePathDeathTrap> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    BlockState.CODEC.optionalFieldOf("block", Blocks.AMETHYST_BLOCK.getDefaultState()).forGetter(trap -> trap.state),
                    Codec.INT.optionalFieldOf("down", 2).forGetter(trap -> trap.down)
            ).apply(instance, InvisiblePathDeathTrap::new)
    );

    private final BlockState state;
    private final int down;

    public InvisiblePathDeathTrap(BlockState state, int down) {
        this.state = state;
        this.down = down;
    }

    @Override
    public void trigger(DRGame game, ServerWorld world, BlockBounds zone) {
        for (BlockPos pos : zone) {
            var state = world.getBlockState(pos);
            if (state.isOf(Blocks.BARRIER)) {
                world.setBlockState(pos.down(down), Blocks.WATER.getDefaultState());
            }
        }
    }

    @Override
    public void reset(DRGame game, ServerWorld world, BlockBounds zone) {
        for (BlockPos pos : zone) {
            var state = world.getBlockState(pos);
            if (state.isOf(Blocks.BARRIER)) {
                world.setBlockState(pos.down(down), this.state);
            }
        }
    }

    @Override
    public MapCodec<? extends DeathTrap> getCodec() {
        return CODEC;
    }
}
