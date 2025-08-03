package cn.dancingsnow.bdct.mixin;

import cn.dancingsnow.bdct.BetterDataComponentTooltip;
import cn.dancingsnow.bdct.TooltipUtilKt;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipPositioner;
import net.minecraft.client.gui.screens.inventory.tooltip.DefaultTooltipPositioner;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector2ic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Optional;

@Mixin(GuiGraphics.class)
public abstract class GuiGraphicsMixin {

    @Shadow
    public abstract int guiWidth();

    @Shadow
    public abstract int guiHeight();

    @Shadow protected abstract void renderTooltipInternal(Font font, List<ClientTooltipComponent> list, int i, int j, ClientTooltipPositioner clientTooltipPositioner, @Nullable ResourceLocation resourceLocation);

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    @Inject(
        method = "renderTooltip(Lnet/minecraft/client/gui/Font;Ljava/util/List;Ljava/util/Optional;IILnet/minecraft/resources/ResourceLocation;)V",
        at = @At("HEAD"),
        cancellable = true
    )
    private void warpRenderTooltip(Font font, List<Component> list, Optional<TooltipComponent> optional, int mouseX, int mouseY, @Nullable ResourceLocation resourceLocation, CallbackInfo ci) {
        List<@NotNull ClientTooltipComponent> tooltips = TooltipUtilKt.gatherTooltipComponents(list, optional, mouseX, this.guiWidth(), this.guiHeight(), font);
        renderTooltipInternal(font, tooltips, mouseX, mouseY, DefaultTooltipPositioner.INSTANCE, resourceLocation);
        ci.cancel();
    }

    @WrapOperation(
        method = "renderTooltipInternal",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/inventory/tooltip/ClientTooltipPositioner;positionTooltip(IIIIII)Lorg/joml/Vector2ic;")
    )
    private Vector2ic warpRenderTooltipInternal(ClientTooltipPositioner instance, int screenWidth, int screenHeight, int x, int y, int tooltipWidth, int tooltipHeight, Operation<Vector2ic> original) {
        Vector2ic vec = BetterDataComponentTooltip.INSTANCE.onRenderTooltip(screenWidth, screenHeight, tooltipWidth, tooltipHeight);
        if (vec != null) {
            return vec;
        }
        return original.call(instance, screenHeight, screenHeight, x, y, tooltipWidth, tooltipHeight);
    }
}
