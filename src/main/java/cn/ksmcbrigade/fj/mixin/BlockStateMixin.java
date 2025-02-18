package cn.ksmcbrigade.fj.mixin;

import cn.ksmcbrigade.fj.FluidJump;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockBehaviour.BlockStateBase.class)
public abstract class BlockStateMixin {
    @Shadow public abstract FluidState getFluidState();

    @Inject(method = "getCollisionShape(Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/phys/shapes/CollisionContext;)Lnet/minecraft/world/phys/shapes/VoxelShape;",at = @At("HEAD"),cancellable = true)
    public void shape(BlockGetter p_60743_, BlockPos p_60744_, CollisionContext p_60745_, CallbackInfoReturnable<VoxelShape> cir){
        if(getFluidState().isEmpty()) return;
        if(!FluidJump.shouldBeSolid(getFluidState())) return;
        Fluid fluid = getFluidState().getType();
        if((fluid.equals(Fluids.WATER) || (fluid.equals(Fluids.FLOWING_WATER)))){
            if(!FluidJump.waterJump.enabled && !FluidJump.fluidJump.enabled) return;
        }
        if((fluid.equals(Fluids.LAVA) || (fluid.equals(Fluids.FLOWING_LAVA)))){
            if(!FluidJump.lavaJump.enabled && !FluidJump.fluidJump.enabled) return;
        }
        cir.setReturnValue(Shapes.block());
        cir.cancel();
    }
}
