package cn.dancingsnow.bdct

import net.minecraft.network.chat.Component

object ComponentTooltipContext {
    var hash: Int = -1
    var page: Int = -1
    var size: Int = -1
    val cachedComponents: MutableList<Component> = mutableListOf()
}
