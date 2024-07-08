package cn.ksmcbrigade.fj;

import cn.ksmcbrigade.fj.modules.AutoSneak;
import cn.ksmcbrigade.fj.modules.LavaJump;
import cn.ksmcbrigade.fj.modules.WaterJump;
import cn.ksmcbrigade.vmr.uitls.ModuleUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.world.level.material.FluidState;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.io.IOException;

@Mod(FluidJump.MODID)
@Mod.EventBusSubscriber
public class FluidJump {

    public static final String MODID = "fj";

    public static WaterJump waterJump = new WaterJump();
    public static LavaJump lavaJump = new LavaJump();
    public static cn.ksmcbrigade.fj.modules.FluidJump fluidJump = new cn.ksmcbrigade.fj.modules.FluidJump();

    public static int tick = 0;

    public FluidJump() throws IOException {
        MinecraftForge.EVENT_BUS.register(this);
        ModuleUtils.add(waterJump);
        ModuleUtils.add(lavaJump);
        ModuleUtils.add(fluidJump);
        ModuleUtils.add(new AutoSneak());
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event){
        if(!FluidJump.waterJump.enabled && !FluidJump.lavaJump.enabled && !FluidJump.fluidJump.enabled) tick=0;
    }

    public static boolean shouldBeSolid(FluidState state)
    {
        Minecraft MC = Minecraft.getInstance();
        return (waterJump.enabled || lavaJump.enabled || fluidJump.enabled) && MC.player != null && MC.player.fallDistance <= 3
                && !MC.options.keyShift.isDown() && !MC.player.isInWater()
                && !MC.player.isInLava();
    }
}
