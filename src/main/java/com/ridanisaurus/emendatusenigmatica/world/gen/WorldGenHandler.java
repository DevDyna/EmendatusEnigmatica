/*
 * MIT License
 *
 * Copyright (c) 2020 Ridanisaurus
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.ridanisaurus.emendatusenigmatica.world.gen;

import com.google.common.collect.ImmutableTable;
import com.google.common.collect.Table;
import com.ridanisaurus.emendatusenigmatica.EmendatusEnigmatica;
import com.ridanisaurus.emendatusenigmatica.config.WorldGenConfig;
import com.ridanisaurus.emendatusenigmatica.config.WorldGenConfig.OreConfigs.BakedOreProps;
import com.ridanisaurus.emendatusenigmatica.registries.OreHandler;
import com.ridanisaurus.emendatusenigmatica.util.Materials;
import com.ridanisaurus.emendatusenigmatica.util.Strata;
import net.minecraft.block.BlockState;
import net.minecraft.data.IDataProvider;
import net.minecraft.loot.LootTableManager;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.feature.template.BlockMatchRuleTest;
import net.minecraft.world.gen.feature.template.RuleTest;
import net.minecraft.world.gen.placement.DepthAverageConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraftforge.common.world.BiomeGenerationSettingsBuilder;

import java.io.IOException;
import java.util.Collection;
import java.util.EnumSet;
import java.util.Optional;
import java.util.function.Supplier;

public class WorldGenHandler {

  //Ore Blocks
  private static Table<Strata, Materials, ConfiguredFeature<?, ?>> oreFeatures;
  //public static final Supplier<Table<Strata, Materials, ConfiguredFeature<?, ?>>> oreFeatureTable = () -> Optional.ofNullable(oreFeatures).orElse(ImmutableTable.of());

  public static void oreFeatures() {
    Collection<Strata> activeStrata = EnumSet.noneOf(Strata.class);
    Collection<Materials> activeOres = EnumSet.noneOf(Materials.class);

    ImmutableTable.Builder<Strata, Materials, ConfiguredFeature<?, ?>> builder = new ImmutableTable.Builder<>();
    for (Strata stratum : Strata.values()) {
      if (WorldGenConfig.COMMON.STRATA.get(stratum) && stratum.block.get() != null) {
        activeStrata.add(stratum);
        for (Materials material : Materials.values()) {
          if (material.oreBlock != null) {
            BakedOreProps p = WorldGenConfig.COMMON.ORES.get(material);
            if (p.ACTIVE) {
              activeOres.add(material);
              builder.put(stratum, material, getOreFeature(
                      p.COUNT_PER_CHUNK,
                      p.VEIN_SIZE,
                      p.MIN_Y,
                      p.MAX_Y, getFilter(stratum), getOreBlock(stratum, material)));
            }
          }
        }
      }
    }

    oreFeatures = builder.build();

    EmendatusEnigmatica.LOGGER.debug("Enabled Strata: {}", activeStrata);
    EmendatusEnigmatica.LOGGER.debug("Enabled Ores: {}", activeOres);
  }

  public static void addEEOres(BiomeGenerationSettingsBuilder builder) {
    oreFeatures.values().forEach(feature ->
            builder.withFeature(GenerationStage.Decoration.UNDERGROUND_ORES, feature));
  }

  private static RuleTest getFilter(Strata stratum) {
    return new BlockMatchRuleTest(stratum.block.get());
  }

  private static BlockState getOreBlock(Strata stratum, Materials material) {
    EmendatusEnigmatica.LOGGER.debug("Ores Recorded: {}", stratum + " " + material);
    return OreHandler.backingOreBlockTable.get(stratum, material).get().getDefaultState();
  }

  private static ConfiguredFeature<?, ?> getOreFeature(int count, int size, int minY, int maxY, RuleTest filler, BlockState state) {
    Feature<OreFeatureConfig> oreFeature = Feature.ORE;
    return oreFeature.withConfiguration(new OreFeatureConfig(filler, state, size))
            .withPlacement(Placement.DEPTH_AVERAGE.configure(new DepthAverageConfig(minY, maxY))) // min and max y using vanilla depth averages
            .square() // square vein
            .func_242731_b(count) // max count per chunk
            ;
  }
}