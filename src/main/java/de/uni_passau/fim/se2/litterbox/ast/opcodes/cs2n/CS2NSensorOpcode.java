package de.uni_passau.fim.se2.litterbox.ast.opcodes.cs2n;

import de.uni_passau.fim.se2.litterbox.ast.opcodes.Opcode;

public enum CS2NSensorOpcode implements Opcode {

    spike_sensor_is_pressed,
    spike_sensor_force,
    spike_sensor_is_distance,
    spike_sensor_distance,
    spike_sensor_reflected_light_intensity,
    spike_sensor_is_reflected_light,
    spike_sensor_is_color,
    spike_sensor_color,
    spike_sensor_reset_yaw,
    spike_sensor_angle,
    spike_sensor_is_orientation,
    spike_sensor_is_moved,
    spike_sensor_is_button,
    spike_sensor_timer,
    spike_sensor_reset_timer;

    public static boolean contains(String opcode) {
        for (CS2NSensorOpcode value : CS2NSensorOpcode.values()) {
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
