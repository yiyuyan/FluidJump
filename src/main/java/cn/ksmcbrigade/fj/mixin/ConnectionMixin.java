package cn.ksmcbrigade.fj.mixin;

import cn.ksmcbrigade.fj.FluidJump;
import net.minecraft.client.Minecraft;
import net.minecraft.network.Connection;
import net.minecraft.network.PacketSendListener;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket;
import net.minecraft.world.level.block.LiquidBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Connection.class)
public abstract class ConnectionMixin {

    @Shadow public abstract void send(Packet<?> p_129513_);

    @Shadow public abstract boolean isConnecting();

    @Unique
    public Minecraft MC = Minecraft.getInstance();

    @Unique
    public boolean instancePacket = false;

    @Inject(method = "doSendPacket",at = @At("HEAD"),cancellable = true)
    public void send(Packet<?> p_243260_, PacketSendListener p_243290_, boolean p_299937_, CallbackInfo ci){
        if(!FluidJump.waterJump.enabled && !FluidJump.lavaJump.enabled && !FluidJump.fluidJump.enabled) return;
        if(MC.player==null) return;
        if(p_243260_ instanceof ServerboundMovePlayerPacket packet){

            boolean cancel = false;

            if(!(packet instanceof ServerboundMovePlayerPacket.Pos)) return;
            if(this.instancePacket) return;
            if(MC.player.isInWater()) return;
            if(MC.player.fallDistance>3) return;
            if(!isOverLiquid()) return;

            if(MC.player.input==null){
                cancel = true;
                return;
            }

            FluidJump.tick++;
            if(FluidJump.tick<4){
                return;
            }

            cancel = true;

            double x = packet.getX(0);
            double y = packet.getY(0);
            double z = packet.getZ(0);

            if(MC.player.tickCount % 2 == 0)
                y -= 0.05;
            else
                y += 0.05;

            Packet<?> newPacket;
            if(packet instanceof ServerboundMovePlayerPacket.Pos)
                newPacket =
                        new ServerboundMovePlayerPacket.Pos(x, y, z, true);
            else
                newPacket = new ServerboundMovePlayerPacket.PosRot(x, y, z, packet.getYRot(0),
                        packet.getXRot(0), true);

            this.instancePacket = true;
            this.send(newPacket);
            new Thread(() -> {
                while (this.isConnecting()){
                    try {
                        Thread.sleep(0);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                this.instancePacket = false;
            });

            ci.cancel();
        }
    }

    public boolean isOverLiquid()
    {
        if(MC.player==null) return false;
        return MC.player.getBlockStateOn().getBlock() instanceof LiquidBlock;
    }
}
