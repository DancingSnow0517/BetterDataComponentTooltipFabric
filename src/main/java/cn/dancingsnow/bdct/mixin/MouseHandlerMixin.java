package cn.dancingsnow.bdct.mixin;

import cn.dancingsnow.bdct.BetterDataComponentTooltip;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.MouseHandler;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(MouseHandler.class)
public class MouseHandlerMixin {


    @WrapOperation(method = "onScroll", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/Screen;mouseScrolled(DDDD)Z"))
    private boolean onScroll(Screen instance, double mouseX, double mouseY, double scrollDeltaX, double scrollDeltaY, Operation<Boolean> original) {
        if (!BetterDataComponentTooltip.INSTANCE.onMouseScroll(mouseX, mouseY, scrollDeltaX, scrollDeltaY)) {
            return original.call(instance, mouseX, mouseY,  scrollDeltaX, scrollDeltaY);
        }
        return false;
    }

}
