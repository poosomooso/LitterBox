package de.uni_passau.fim.se2.litterbox.ast.opcodes.cs2n;

import de.uni_passau.fim.se2.litterbox.ast.opcodes.Opcode;

public enum CS2NMovementOpcode implements Opcode {

    spike_movemenet_direction_for_duration,
    spike_movemenet_direction,
    spike_movement_moveHeadingForUnits,
    spike_movement_startMoving,
    spike_movement_stopMoving,
    spike_movement_moveHeadingForUnitAtSpeed,
    spike_movement_moveForUnitsAtSpeeds,
    spike_movement_setMovementSpeed,
    spike_movement_startMovingHeadingAtSpeed,
    spike_movement_startMovingAtSpeeds;

    public static boolean contains(String opcode) {
        for (CS2NMovementOpcode value : CS2NMovementOpcode.values()) {
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
