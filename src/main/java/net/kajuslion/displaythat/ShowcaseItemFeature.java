package net.kajuslion.displaythat;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.*;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.fml.ModList;

public class ShowcaseItemFeature {
    public static float alphaValue = 1F;


    @OnlyIn(Dist.CLIENT)
    public static void renderItemForMessage(GuiGraphics guiGraphics, FormattedCharSequence sequence, float x, float y, int color) {

        Minecraft mc = Minecraft.getInstance();

        StringBuilder before = new StringBuilder();

        int halfSpace = mc.font.width(" ") / 2;

        sequence.accept((counter_, style, character) -> {
            String sofar = before.toString();
            if (sofar.endsWith("  ")) {
                render(mc, guiGraphics, sofar.substring(0, sofar.length() - 2), character == ' ' ? 0 : -halfSpace, x, y, style, color);
                return false;
            }
            before.append((char) character);
            return true;
        });
    }

    public static MutableComponent createStackComponent(ItemStack stack, MutableComponent component) {

        Style style = component.getStyle();
        int count = stack.getCount();
        if (count > 64) {
            ItemStack copyStack = stack.copy();
            copyStack.setCount(64);
            style = style.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_ITEM, new HoverEvent.ItemStackInfo(copyStack)));
            component.withStyle(style);
        }


//        System.out.println("Style: " + style);
//        System.out.println("Component: " + component);

        MutableComponent icon = Component.literal("   ");
        icon.setStyle(style);

        System.out.println(component);

        Style componentStyle = component.getStyle();
        Component componentCopy = component.copy();

        System.out.println(componentCopy.getContents());


        String modifiedText = componentCopy.getString().replaceAll("\\[|\\]", "");
        Component processedComponent = Component.literal(modifiedText).setStyle(style);

        System.out.println(processedComponent);


        MutableComponent formattedComponent = Component.literal("")
                .append(Component.literal("[").withStyle(ChatFormatting.DARK_GRAY));

        if (count > 1) {
            formattedComponent
                    .append(Component.literal("x" + count + " ").withStyle(Style.EMPTY.withColor(TextColor.fromRgb(0xFFFDA3))))
                    .append(Component.literal("|").withStyle(ChatFormatting.DARK_GRAY));
        }
        formattedComponent
                .append(Component.literal(" "))
                .append(icon)
                .append(processedComponent)
                .append(Component.literal("]").withStyle(ChatFormatting.DARK_GRAY));





        return formattedComponent;
    }




    @OnlyIn(Dist.CLIENT)
    private static void render(Minecraft mc, GuiGraphics guiGraphics, String before, float extraShift, float x, float y, Style style, int color) {
        float a = (color >> 24 & 255) / 255.0F;

        PoseStack pose = guiGraphics.pose();

        HoverEvent hoverEvent = style.getHoverEvent();
        if (hoverEvent != null && hoverEvent.getAction() == HoverEvent.Action.SHOW_ITEM) {
            HoverEvent.ItemStackInfo contents = hoverEvent.getValue(HoverEvent.Action.SHOW_ITEM);

            ItemStack stack = contents != null ? contents.getItemStack() : ItemStack.EMPTY;

            if (stack.isEmpty())
                stack = new ItemStack(Blocks.BARRIER); //For invalid icon

            float shift = mc.font.width(before) + extraShift;

            // Fix y-shift if overflowingbars is installed
            if (ModList.get().isLoaded("overflowingbars")) {
                y += Minecraft.getInstance().player.getAbsorptionAmount() > 10.0F ? 10 : 0;
                y += Minecraft.getInstance().player.getArmorValue() > 0.5F ? 10 : 0;
            }

            if (a > 0) {
                alphaValue = a;

                guiGraphics.pose().pushPose();

                guiGraphics.pose().mulPose(pose.last().pose());

                guiGraphics.pose().translate(shift + x, y, 0);
                guiGraphics.pose().scale(0.5f, 0.5f, 0.5f);
                guiGraphics.renderItem(stack, 0, 0);
                guiGraphics.pose().popPose();

                alphaValue = 1F;
            }
        }
    }
}