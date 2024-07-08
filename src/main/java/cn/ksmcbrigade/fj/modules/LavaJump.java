package cn.ksmcbrigade.fj.modules;

import cn.ksmcbrigade.vmr.module.Module;
import cn.ksmcbrigade.vmr.uitls.ModuleUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class LavaJump extends Module {

    public int tick = 10;

    public LavaJump() {
        super("hack.name.ljj");
    }

    @Override
    public void enabled(Minecraft MC) throws Exception {
        ModuleUtils.set("hack.name.fjj",false);
    }

    @Override
    public void playerTick(Minecraft MC, @Nullable Player player) {
        if(MC.options.keyShift.isDown()) return;
        if(player==null) return;
        if(player.isInFluidType(Fluids.LAVA.getFluidType()) || player.isInFluidType(Fluids.FLOWING_LAVA.getFluidType())){
            Vec3 vec3 = player.getViewVector(0);
            player.setDeltaMovement(0,0.11,0);
            tick = 0;
            return;
        }
        Vec3 vec3 = player.getViewVector(0);
        if(tick==0){
            player.setDeltaMovement(0,0.3,0);
        }
        else if(tick==1){
            player.setDeltaMovement(0,0,0);
        }
        tick++;
    }
}
