package de.uni_passau.fim.se2.litterbox.ast.opcodes.cs2n;

import de.uni_passau.fim.se2.litterbox.ast.opcodes.Opcode;

public enum CS2NMotorOpcode implements Opcode {

    spike_motor_runForDirectionTimes,
    spike_motor_runDirection,
    spike_motor_stopMotor,
    spike_motor_position;

    public static boolean contains(String opcode) {
        for (CS2NMotorOpcode value : CS2NMotorOpcode.values()) {
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
