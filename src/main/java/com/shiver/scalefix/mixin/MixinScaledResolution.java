package com.shiver.scalefix.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ScaledResolution.class)
public abstract class MixinScaledResolution {

    /**
     * In Minecraft 1.12.2, ScaledResolution forces odd scaleFactors > 1
     * (specifically 3, which corresponds to GUI scale "Large" / "大")
     * to decrement by 1 when isUnicode() is true (e.g. in Chinese or when
     * Force Unicode Font is enabled):
     * <pre>
     * boolean flag = minecraftClient.isUnicode();
     * ...
     * if (flag && this.scaleFactor % 2 != 0 && this.scaleFactor != 1) {
     *     --this.scaleFactor;
     * }
     * </pre>
     * By redirecting mc.isUnicode() to return false in the ScaledResolution constructor,
     * this decrement check is bypassed, allowing "Large" (3x) and other odd scale factors
     * to work normally in Chinese/Unicode mode.
     */
    @Redirect(
        method = "<init>",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/Minecraft;isUnicode()Z"
        )
    )
    private boolean scalefix$isUnicode(Minecraft minecraft) {
        return false;
    }

}

