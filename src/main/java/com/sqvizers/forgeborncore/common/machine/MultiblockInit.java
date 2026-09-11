package com.sqvizers.forgeborncore.common.machine;

import com.sqvizers.forgeborncore.common.machine.multiblock.SpiritfireFurnace;
import com.sqvizers.forgeborncore.common.machine.multiblock.ward.SanctumWardEventHandler;
import com.sqvizers.forgeborncore.common.machine.multiblock.ward.SanctumWardMachines;

public class MultiblockInit {

    public static void init() {
        SpiritfireFurnace.init();
        SanctumWardMachines.init();
        SanctumWardEventHandler.register();
    }
}
