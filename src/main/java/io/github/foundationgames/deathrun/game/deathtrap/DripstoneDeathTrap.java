package io.github.foundationgames.deathrun.game.deathtrap;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.enums.Thickness;
import net.minecraft.entity.FallingBlockEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import xyz.nucleoid.map_templates.BlockBounds;

public class DripstoneDeathTrap extends DeathTrap {
    private static final BlockState[] dripstoneStates = {
            dripstoneState(Thickness.TIP),
            dripstoneState(Thickness.FRUSTUM),
            dripstoneState(Thickness.MIDDLE)
    };

    private static final int length = 2;

    protected DripstoneDeathTrap() {}

    @Override
    public void trigger(ServerWorld world, BlockBounds zone) {
        for (BlockPos pos : zone) {
            var state = world.getBlockState(pos);
            if (state.isOf(Blocks.DRIPSTONE_BLOCK)) {
                var dripstonePos = Vec3d.ofBottomCenter(pos.down().down(length - 1));
                float off = world.random.nextFloat();
                for (int i = 0; i < length; i++) {
                    var dState = dripstoneStates[Math.min(i, dripstoneStates.length - 1)];
                    var dripstone = new FallingBlockEntity(world, dripstonePos.x, dripstonePos.y + i - off, dripstonePos.z, dState);
                    dripstone.timeFalling = 1;
                    dripstone.dropItem = false;
                    world.spawnEntity(dripstone);
                }
            }
        }
    }

    private static BlockState dripstoneState(Thickness thickness) {
        return Blocks.POINTED_DRIPSTONE.getDefaultState()
                .with(Properties.VERTICAL_DIRECTION, Direction.DOWN)
                .with(Properties.THICKNESS, thickness);
    }

    public static void init() {
        DeathTrap.add("dripstone", new DripstoneDeathTrap());
    }
}
