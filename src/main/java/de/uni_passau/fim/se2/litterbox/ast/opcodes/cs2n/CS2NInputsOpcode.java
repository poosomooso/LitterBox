package de.uni_passau.fim.se2.litterbox.ast.opcodes.cs2n;

import de.uni_passau.fim.se2.litterbox.ast.opcodes.Opcode;

public enum CS2NInputsOpcode implements Opcode {

    spike_sensor_motor_menu,
    spike_direction_picker,
    spike_movement_direction_picker,
    spike_heading_input,
    spike_sensor_port_menu,
    spike_sensor_color_menu;

    public static boolean contains(String opcode) {
        for (CS2NInputsOpcode value : CS2NInputsOpcode.values()) {
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
