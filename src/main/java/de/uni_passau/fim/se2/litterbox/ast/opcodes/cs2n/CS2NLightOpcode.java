package de.uni_passau.fim.se2.litterbox.ast.opcodes.cs2n;

import de.uni_passau.fim.se2.litterbox.ast.opcodes.Opcode;

public enum CS2NLightOpcode implements Opcode {

    spike_light_turnOnForSeconds,
    spike_set_pixel_brightness,
    spike_write;

    public static boolean contains(String opcode) {
        for (CS2NLightOpcode value : CS2NLightOpcode.values()) {
            if (value.name().equals(opcode)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String getName() {
        return name();
    }
}
