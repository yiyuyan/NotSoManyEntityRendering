package cn.ksmcbrigade.nsmer;

import cn.ksmcbrigade.nsmer.gui.ConfiguredScreen;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.commons.io.FileUtils;
import org.lwjgl.glfw.GLFW;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

@Mod("nsmer")
@Mod.EventBusSubscriber(value = Dist.CLIENT,modid = "nsmer")
public class NotSoManyEntityRendering {

    public static File file = new File("config/nsmer-config.json");

    public static ArrayList<String> blackList = new ArrayList<>();

    public static KeyMapping key = new KeyMapping("key.nsmer.configured_screen",GLFW.GLFW_KEY_O,KeyMapping.CATEGORY_MISC);

    public NotSoManyEntityRendering() throws IOException {
        new File("config").mkdirs();
        if(!file.exists()){
            FileUtils.writeStringToFile(file,"[]");
        }
        for(JsonElement entity:JsonParser.parseString(FileUtils.readFileToString(file)).getAsJsonArray()){
            blackList.add(entity.getAsString());
        }
    }

    public static void save() throws IOException {
        JsonArray array = new JsonArray();
        blackList.forEach(array::add);
        FileUtils.write(file,array.toString());
    }

    public static String getRegisterName(Entity entity){
        ResourceLocation entityType = ForgeRegistries.ENTITY_TYPES.getKey(entity.getType());
        return entityType.toString();
    }

    public static String getRegisterName(EntityType<?> entity){
        ResourceLocation entityType = ForgeRegistries.ENTITY_TYPES.getKey(entity);
        return entityType.toString();
    }

    @SubscribeEvent
    public static void regKey(RegisterKeyMappingsEvent event){
        event.register(key);
    }

    @SubscribeEvent
    public static void inputKey(InputEvent.Key event) {
        if(key.isDown()){
            Minecraft.getInstance().setScreen(new ConfiguredScreen());
        }
    }
}
