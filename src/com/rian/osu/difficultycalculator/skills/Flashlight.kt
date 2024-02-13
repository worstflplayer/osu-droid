package com.rian.osu.difficultycalculator.skills

import com.rian.osu.difficultycalculator.DifficultyHitObject
import com.rian.osu.difficultycalculator.evaluators.FlashlightEvaluator.evaluateDifficultyOf
import com.rian.osu.mods.Mod
import com.rian.osu.mods.ModHidden
import kotlin.math.pow

/**
 * Represents the skill required to memorize and hit every object in a beatmap with the Flashlight mod enabled.
 */
class Flashlight(
    /**
     * The [Mod]s that this skill processes.
     */
    mods: List<Mod>
) : StrainSkill(mods) {
    override val reducedSectionCount = 0
    override val reducedSectionBaseline = 1.0
    override val decayWeight = 1.0

    private var currentStrain = 0.0
    private val skillMultiplier = 0.052
    private val strainDecayBase = 0.15
    private val hasHidden = mods.any { it is ModHidden }

    override fun strainValueAt(current: DifficultyHitObject): Double {
        currentStrain *= strainDecay(current.deltaTime)
        currentStrain += evaluateDifficultyOf(current, hasHidden) * skillMultiplier

        return currentStrain
    }

    override fun calculateInitialStrain(time: Double, current: DifficultyHitObject) =
        currentStrain * strainDecay(time - current.previous(0)!!.startTime)

    override fun saveToHitObject(current: DifficultyHitObject) {
        current.flashlightStrain = currentStrain
    }

    private fun strainDecay(ms: Double) = strainDecayBase.pow(ms / 1000)
}
