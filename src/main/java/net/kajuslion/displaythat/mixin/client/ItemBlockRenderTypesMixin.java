package net.kajuslion.displaythat.mixin.client;

import net.kajuslion.displaythat.ShowcaseItemFeature;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static net.minecraft.client.renderer.ItemBlockRenderTypes.getChunkRenderType;

@Mixin(ItemBlockRenderTypes.class)
public class ItemBlockRenderTypesMixin {
    @Inject(method = "getRenderType", at = @At("HEAD"), cancellable = true)
    private static void overrideRenderType(BlockState state, boolean useShaderTransparency, CallbackInfoReturnable<RenderType> cir) {
        if (ShowcaseItemFeature.alphaValue != 1.0F) {
            if (!useShaderTransparency) {
                cir.setReturnValue(Sheets.cutoutBlockSheet());
            } else {
                RenderType rendertype = getChunkRenderType(state);
                cir.setReturnValue(rendertype == RenderType.translucent() ? Sheets.translucentItemSheet() : Sheets.cutoutBlockSheet());
            }
        }
    }
}