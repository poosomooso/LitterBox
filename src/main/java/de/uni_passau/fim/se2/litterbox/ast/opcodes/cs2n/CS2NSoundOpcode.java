package de.uni_passau.fim.se2.litterbox.ast.opcodes.cs2n;

import de.uni_passau.fim.se2.litterbox.ast.opcodes.Opcode;

public enum CS2NSoundOpcode implements Opcode {

    spike_sound_playuntildone,
    spike_sound_startsound,
    spike_play_beep,
    spike_start_playing_beep,
    spike_stop_all_sounds,
    spike_set_volume;

    public static boolean contains(String opcode) {
        for (CS2NSoundOpcode value : CS2NSoundOpcode.values()) {
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
