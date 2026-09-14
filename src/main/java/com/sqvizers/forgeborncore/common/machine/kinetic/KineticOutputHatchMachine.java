// This code is provided by Astrocore and is subject to the MIT License.
package com.sqvizers.forgeborncore.common.machine.kinetic;

import com.gregtechceu.gtceu.api.blockentity.BlockEntityCreationInfo;
import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.machine.multiblock.MultiblockControllerMachine;
import com.gregtechceu.gtceu.api.machine.trait.recipe.RecipeLogic;

import net.minecraft.core.Direction;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class KineticOutputHatchMachine extends KineticHatchPartMachine {

    public KineticOutputHatchMachine(BlockEntityCreationInfo info) {
        super(info, IO.OUT);
    }

    //
    public void setOutputSU(float su, float capacityPerRPM) {
        var kineticInterface = getKineticInterface();
        if (kineticInterface == null) return;

        if (isWorkingEnabled()) {
            kineticInterface.setOutputSU(su, capacityPerRPM);
        } else {
            kineticInterface.stopOutput();
        }
    }

    public void stopOutput() {
        var kineticInterface = getKineticInterface();
        if (kineticInterface != null) kineticInterface.stopOutput();
    }

    @Override
    public void onRotated(Direction oldFacing, Direction newFacing) {
        var oldInterface = getKineticInterface(oldFacing);
        if (oldInterface != null) oldInterface.stopOutput();
        super.onRotated(oldFacing, newFacing);
    }

    @Override
    public void recipeLogicStatusChanged(RecipeLogic.Status oldStatus, RecipeLogic.Status newStatus) {
        if (newStatus != RecipeLogic.Status.WORKING) stopOutput();
        super.recipeLogicStatusChanged(oldStatus, newStatus);
    }

    @Override
    public void removedFromController(MultiblockControllerMachine controller) {
        stopOutput();
        super.removedFromController(controller);
    }

    @Override
    public void setWorkingEnabled(boolean workingEnabled) {
        if (!workingEnabled) stopOutput();
        super.setWorkingEnabled(workingEnabled);
    }

    @Override
    public void onUnload() {
        stopOutput();
        super.onUnload();
    }
}
