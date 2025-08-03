package cn.dancingsnow.bdct;

import dev.toma.configuration.config.Config;
import dev.toma.configuration.config.Configurable;

@Config(id = "bdct")
public class BetterDataComponentTooltipConfig {
    @Configurable
    @Configurable.Comment("Tooltip Count page showed")
    @Configurable.Range(min = 1)
    public int pageSize = 5;

    @Configurable
    @Configurable.Comment("Witch data component will not show in tooltip")
    public String[] componentBlacklist = new String[]{"minecraft:rarity"};

    @Configurable
    @Configurable.Comment("Show Original Text json")
    public boolean showOriginalText = false;
}
