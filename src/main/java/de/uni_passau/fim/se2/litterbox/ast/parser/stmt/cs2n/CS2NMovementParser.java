package de.uni_passau.fim.se2.litterbox.ast.parser.stmt.cs2n;

import com.fasterxml.jackson.databind.JsonNode;
import de.uni_passau.fim.se2.litterbox.ast.Constants;
import de.uni_passau.fim.se2.litterbox.ast.ParsingException;
import de.uni_passau.fim.se2.litterbox.ast.model.expression.num.NumExpr;
import de.uni_passau.fim.se2.litterbox.ast.model.extensions.cs2n.expression.inputs.spindirection.*;
import de.uni_passau.fim.se2.litterbox.ast.model.extensions.cs2n.expression.unit.*;
import de.uni_passau.fim.se2.litterbox.ast.model.extensions.cs2n.statements.movement.CS2NMovementStmt;
import de.uni_passau.fim.se2.litterbox.ast.model.extensions.cs2n.statements.movement.SpikeMovementDirectionForDuration;
import de.uni_passau.fim.se2.litterbox.ast.model.literals.NumberLiteral;
import de.uni_passau.fim.se2.litterbox.ast.model.metadata.block.BlockMetadata;
import de.uni_passau.fim.se2.litterbox.ast.opcodes.cs2n.CS2NMovementOpcode;
import de.uni_passau.fim.se2.litterbox.ast.parser.ProgramParserState;
import de.uni_passau.fim.se2.litterbox.ast.parser.metadata.BlockMetadataParser;
import de.uni_passau.fim.se2.litterbox.utils.Preconditions;

import static de.uni_passau.fim.se2.litterbox.ast.Constants.*;

public class CS2NMovementParser{

    public static CS2NMovementStmt parse(final ProgramParserState state, String identifier, JsonNode current,
                                              JsonNode allBlocks) throws ParsingException {
        Preconditions.checkNotNull(current);
        Preconditions.checkNotNull(allBlocks);

        final String opcodeString = current.get(OPCODE_KEY).asText();
        Preconditions
                .checkArgument(CS2NMovementOpcode.contains(opcodeString),
                        "Given blockID does not point to a CS2N Movement block.");

        BlockMetadata metadata = BlockMetadataParser.parse(identifier, current);

        final CS2NMovementOpcode opcode = CS2NMovementOpcode.valueOf(opcodeString);

        switch (opcode) {
            case spike_movemenet_direction_for_duration:
                SpikeMovementDirectionPicker direction = parseDirectionFromInput(allBlocks, getInput(current, DIRECTION_KEY_CAP));
                NumExpr rate = new NumberLiteral(getInput(current, RATE_KEY_CAP).get(POS_INPUT_VALUE).get(POS_INPUT_VALUE).asDouble());
                Unit unit = parseUnit(current);
                return new SpikeMovementDirectionForDuration(direction, rate, unit, metadata);
            case spike_movemenet_direction:
                break;
            case spike_movement_moveHeadingForUnits:
                break;
            case spike_movement_startMoving:
                break;
            case spike_movement_stopMoving:
                break;
            case spike_movement_moveHeadingForUnitAtSpeed:
                break;
            case spike_movement_moveForUnitsAtSpeeds:
                break;
            case spike_movement_setMovementSpeed:
                break;
            case spike_movement_startMovingHeadingAtSpeed:
                break;
            case spike_movement_startMovingAtSpeeds:
                break;
        }
        return null;
    }

    private static Unit parseUnit(JsonNode current) throws ParsingException {
        UnitOptions option = UnitOptions.valueOf(getField(current, UNITS_KEY_CAPS).get(FIELD_VALUE).asText());
        switch (option) {
            case cm:
                return new Cm();
            case in:
                return new Inches();
            case rotations:
                return new Rotations();
            case degrees:
                return new Degrees();
            case seconds:
                return new Seconds();
        }
        throw new ParsingException("No unit option for " + option);
    }

    public static JsonNode getInput(JsonNode current, String inputName) throws ParsingException {
        JsonNode inputs = current.get(Constants.INPUTS_KEY);
        if (inputs.has(inputName)) {
            return inputs.get(inputName);
        }
        throw new ParsingException("Input " + inputName + " doesn't exist for " + current);
    }

    public static JsonNode getField(JsonNode current, String fieldName) throws ParsingException {
        JsonNode fields = current.get(FIELDS_KEY);
        if (fields.has(fieldName)) {
            return fields.get(fieldName);
        }
        throw new ParsingException("Field " + fieldName + " doesn't exist for " + current);
    }

    public static SpikeMovementDirectionPicker parseDirectionFromInput(JsonNode allBlocks, JsonNode directionInput) throws ParsingException {
        String pickerId = directionInput.get(Constants.POS_INPUT_VALUE).asText();
        JsonNode picker = allBlocks.get(pickerId);
        BlockMetadata metadata = BlockMetadataParser.parse(pickerId, picker);
        DirectionOption dir = DirectionOption.valueOf(
                getField(picker, SPIN_DIRECTIONS_KEY_CAPS).get(FIELD_VALUE).asText());
        switch (dir) {

            case forward:
                return new Forward(metadata);
            case reverse:
                return new Reverse(metadata);
            case left:
                return new Left(metadata);
            case right:
                return new Right(metadata);
        }
        throw new ParsingException("No direction option for " + picker);
    }

    private enum DirectionOption {
        forward, reverse, left, right;
    }

    private enum UnitOptions {
        cm, in, rotations, degrees, seconds;
    }
}
