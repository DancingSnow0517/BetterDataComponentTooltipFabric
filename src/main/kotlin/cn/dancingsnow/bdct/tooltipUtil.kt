package cn.dancingsnow.bdct

import com.mojang.datafixers.util.Either
import net.minecraft.client.gui.Font
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent
import net.minecraft.locale.Language
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.FormattedText
import net.minecraft.util.FormattedCharSequence
import net.minecraft.world.inventory.tooltip.TooltipComponent
import java.util.*


fun gatherTooltipComponents(
    textElements: MutableList<out FormattedText>,
    itemComponent: Optional<TooltipComponent>,
    mouseX: Int,
    screenWidth: Int,
    screenHeight: Int,
    fallbackFont: Font
): MutableList<ClientTooltipComponent> {
    val elements = textElements.map { value ->
        Either.left<FormattedText, TooltipComponent>(value)
    }.toMutableList()
    itemComponent.ifPresent { c ->
        elements.add(
            1, Either.right(c)
        )
    }
    return gatherTooltipComponentsFromElements(
        elements, mouseX, screenWidth, screenHeight, fallbackFont
    )
}

fun gatherTooltipComponentsFromElements(
    elements: MutableList<Either<FormattedText, TooltipComponent>>,
    mouseX: Int,
    screenWidth: Int,
    screenHeight: Int,
    font: Font
): MutableList<ClientTooltipComponent> {

    // text wrapping
    var tooltipTextWidth: Int =
        elements.maxOfOrNull { either ->
            either.map(font::width) { 0 }
        } ?: 0

    var needsWrap = false

    var tooltipX = mouseX + 12
    if (tooltipX + tooltipTextWidth + 4 > screenWidth) {
        tooltipX = mouseX - 16 - tooltipTextWidth
        if (tooltipX < 4) {
            tooltipTextWidth = if (mouseX > screenWidth / 2) mouseX - 12 - 8 else screenWidth - 16 - mouseX
            needsWrap = true
        }
    }

    val tooltipTextWidthF = tooltipTextWidth
    if (needsWrap) {
        return elements.flatMap { either ->
            either.map(
                { splitLine(it, font, tooltipTextWidthF) },
                { mutableListOf(ClientTooltipComponent.create(it)) }
            )
        }.toMutableList()
    }
    return elements.map {
        it.map(
            { text ->
                ClientTooltipComponent.create(
                    if (text is Component) text.visualOrderText else Language.getInstance().getVisualOrder(text)
                )
            }, ClientTooltipComponent::create
        )
    }.toMutableList()
}

private fun splitLine(text: FormattedText, font: Font, maxWidth: Int): MutableList<ClientTooltipComponent> {
    if (text is Component && text.string.isEmpty()) {
        return mutableListOf(ClientTooltipComponent.create(text.visualOrderText))
    }
    return font.split(text, maxWidth).map { formattedCharSequence: FormattedCharSequence ->
        ClientTooltipComponent.create(formattedCharSequence)
    }.toMutableList()
}