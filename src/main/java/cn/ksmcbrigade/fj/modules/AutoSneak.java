package cn.ksmcbrigade.fj.modules;

import cn.ksmcbrigade.vmr.module.Config;
import cn.ksmcbrigade.vmr.module.Module;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static cn.ksmcbrigade.vmr.module.Config.configDir;

public class AutoSneak extends Module {

    public int tick = 10;

    public AutoSneak() throws IOException {
        super("hack.name.auto_sk", false, -1, new Config(new File("AutoSneak"),getData()), false);
    }

    @Override
    public void enabled(Minecraft MC) throws IOException {
        File pathFile = new File(configDir,getConfig().file.getPath()+".json");
        this.getConfig().setData(JsonParser.parseString(Files.readString(pathFile.toPath())).getAsJsonObject());
        this.tick = this.getConfig().get("tick").getAsInt();
    }

    @Override
    public void playerTick(Minecraft MC, @Nullable Player player) {
        if(tick!=0){
            tick--;
            return;
        }
        MC.options.keyShift.setDown(!MC.options.keyShift.isDown());
        try {
            tick = this.getConfig().get("tick").getAsInt();
        }
        catch (Exception e){
            tick = 10;
        }
    }

    public static JsonObject getData(){
        JsonObject object = new JsonObject();
        object.addProperty("tick",10);
        return object;
    }
}
