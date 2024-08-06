package cn.ksmcbrigade.nsmer.mixin;

import cn.ksmcbrigade.nsmer.NotSoManyEntityRendering;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LevelRenderer.class)
public class LevelRendererMixin {
    @Inject(method = "renderEntity",at = @At("HEAD"),cancellable = true)
    public void render(Entity p_109518_, double p_109519_, double p_109520_, double p_109521_, float p_109522_, PoseStack p_109523_, MultiBufferSource p_109524_, CallbackInfo ci){
        if(NotSoManyEntityRendering.blackList.contains(NotSoManyEntityRendering.getRegisterName(p_109518_))) ci.cancel();
    }
}
