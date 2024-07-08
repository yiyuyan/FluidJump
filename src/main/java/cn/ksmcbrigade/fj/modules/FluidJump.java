package cn.ksmcbrigade.fj.modules;

import cn.ksmcbrigade.vmr.module.Module;
import cn.ksmcbrigade.vmr.uitls.ModuleUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.awt.event.KeyEvent;

public class FluidJump extends Module {

    public int tick = 10;

    public FluidJump() {
        super("hack.name.fjj",false, KeyEvent.VK_J);
    }

    @Override
    public void enabled(Minecraft MC) throws Exception {
        ModuleUtils.set("hack.name.wjj",false);
        ModuleUtils.set("hack.name.ljj",false);
    }

    @Override
    public void playerTick(Minecraft MC, @Nullable Player player) {
        if(MC.options.keyShift.isDown()) return;
        if(player==null) return;
        if(player.isInFluidType()){
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
