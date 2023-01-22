/*
 *  MIT License
 *
 *  Copyright (c) 2020 Ridanisaurus
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all
 *  copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *  SOFTWARE.
 */

package com.ridanisaurus.emendatusenigmatica.blocks;

import com.ridanisaurus.emendatusenigmatica.loader.parser.model.MaterialModel;
import com.ridanisaurus.emendatusenigmatica.loader.parser.model.StrataModel;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.DropExperienceBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;

public class GemOreBlock extends DropExperienceBlock implements IColorable {
	private final String localisedName;
	private final int minExp;
	private final int maxExp;
	public final int highlight2;
	public final int highlight1;
	public final int base;
	public final int shadow1;
	public final int shadow2;

	public GemOreBlock(StrataModel strata, MaterialModel material) {
		super(BlockBehaviour.Properties.of(Material.STONE)
				.strength(strata.getHardness(), strata.getResistance())
				.requiresCorrectToolForDrops());
		this.localisedName = material.getLocalizedName();
		this.minExp = material.getOreDrop().getMin();
		this.maxExp = material.getOreDrop().getMax();
		this.highlight2 = material.getColors().getHighlightColor(3);
		this.highlight1 = material.getColors().getHighlightColor(1);
		this.base = material.getColors().getMaterialColor();
		this.shadow1 = material.getColors().getShadowColor(1);
		this.shadow2 = material.getColors().getShadowColor(2);
	}

	@Override
	public MutableComponent getName() {
		return Component.translatable(localisedName);
	}

	protected int getExperience(RandomSource rand) {
		return Mth.nextInt(rand, minExp, maxExp);
	}

	@Override
	public int getExpDrop(BlockState state, LevelReader reader, RandomSource random, BlockPos pos, int fortune, int silktouch) {
		return silktouch == 0 ? this.getExperience(random) : 0;
	}

	@Override
	public int getHighlight2() {
		return highlight2;
	}

	@Override
	public int getHighlight1() {
		return highlight1;
	}

	@Override
	public int getBase() {
		return base;
	}

	@Override
	public int getShadow1() {
		return shadow1;
	}

	@Override
	public int getShadow2() {
		return shadow2;
	}
}