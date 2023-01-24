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

package com.ridanisaurus.emendatusenigmatica.items;

import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class ToolTier implements Tier {
	private final int level;
	private final int uses;
	private final float efficiency;
	private final float attackDamage;
	private final int enchantability;
	@Nullable
	private final TagKey<Block> tag;
	@NotNull
	private final Supplier<Ingredient> repairIngredient;

	public ToolTier(int level, int uses, float efficiency, float attackDamage, int enchantability, @Nullable TagKey<Block> tag, @NotNull Supplier<Ingredient> repairIngredient) {
		this.level = level;
		this.uses = uses;
		this.efficiency = efficiency;
		this.attackDamage = attackDamage;
		this.enchantability = enchantability;
		this.tag = tag;
		this.repairIngredient = repairIngredient;
	}

	public ToolTier(int level, int uses, float speed, float attackDamageBonus, int enchantmentValue, @NotNull Supplier<Ingredient> repairIngredient) {
		this.level = level;
		this.uses = uses;
		this.efficiency = speed;
		this.attackDamage = attackDamageBonus;
		this.enchantability = enchantmentValue;
		this.tag = Tier.super.getTag();
		this.repairIngredient = repairIngredient;
	}

	@Override
	public int getUses() {
		return this.uses;
	}

	@Override
	public float getSpeed() {
		return this.efficiency;
	}

	@Override
	public float getAttackDamageBonus() {
		return this.attackDamage;
	}

	@Override
	public int getLevel() {
		return this.level;
	}

	@Override
	public int getEnchantmentValue() {
		return this.enchantability;
	}

	@Override
	public Ingredient getRepairIngredient() {
		return this.repairIngredient.get();
	}

	@Override
	public @Nullable TagKey<Block> getTag() {
		return this.tag;
	}

	@Override
	public String toString() {
		return "ForgeTier[" +
				"level=" + level + ", " +
				"uses=" + uses + ", " +
				"speed=" + efficiency + ", " +
				"attackDamageBonus=" + attackDamage + ", " +
				"enchantmentValue=" + enchantability + ", " +
				"tag=" + tag + ", " +
				"repairIngredient=" + repairIngredient + ']';
	}
}