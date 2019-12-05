/*
 * Copyright (C) 2019 LitterBox contributors
 *
 * This file is part of LitterBox.
 *
 * LitterBox is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at
 * your option) any later version.
 *
 * LitterBox is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with LitterBox. If not, see <http://www.gnu.org/licenses/>.
 */
package scratch.ast.parser.stmt;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import scratch.ast.Constants;
import scratch.ast.ParsingException;
import scratch.ast.model.elementchoice.ElementChoice;
import scratch.ast.model.elementchoice.WithId;
import scratch.ast.model.expression.string.StringExpr;
import scratch.ast.model.statement.actorsound.*;
import scratch.ast.model.variable.StrId;
import scratch.ast.opcodes.ActorSoundStmtOpcode;
import scratch.ast.parser.StringExprParser;
import utils.Preconditions;

import java.util.ArrayList;
import java.util.List;

import static scratch.ast.Constants.*;

public class ActorSoundStmtParser {

    private static final String SOUND_MENU = "SOUND_MENU";

    public static ActorSoundStmt parse(JsonNode current, JsonNode allBlocks) throws ParsingException {
        Preconditions.checkNotNull(current);
        Preconditions.checkNotNull(allBlocks);

        final String opCodeString = current.get(OPCODE_KEY).asText();

        Preconditions.checkArgument(ActorSoundStmtOpcode.contains(opCodeString), "Given block is not an "
            + "ActorStmtBlock");

        ElementChoice elementChoice;
        final ActorSoundStmtOpcode opcode = ActorSoundStmtOpcode.valueOf(opCodeString);
        switch (opcode) {
            case sound_playuntildone:
                elementChoice = getSoundElement(current, allBlocks);
                return new PlaySoundUntilDone(elementChoice);

            case sound_play:
                elementChoice = getSoundElement(current, allBlocks);
                return new StartSound(elementChoice);

            case sound_cleareffects:
                return new ClearSoundEffects();

            case sound_stopallsounds:
                return new StopAllSounds();

            default:
                throw new RuntimeException("Not implemented yet for opcode " + opCodeString);
        }
    }

    static ElementChoice getSoundElement(JsonNode current, JsonNode allBlocks) throws ParsingException {
        //Make a list of all elements in inputs
        List<JsonNode> inputsList = new ArrayList<>();
        current.get(Constants.INPUTS_KEY).elements().forEachRemaining(inputsList::add);

        if (getShadowIndicator((ArrayNode) inputsList.get(0)) == 1) {
            String soundMenuId = current.get(INPUTS_KEY).get(SOUND_MENU).get(Constants.POS_INPUT_VALUE).asText();
            JsonNode soundMenu = allBlocks.get(soundMenuId);
            String soundValue = soundMenu.get(FIELDS_KEY).get(SOUND_MENU).get(FIELD_VALUE).asText();
            return new WithId(new StrId(soundValue));
        } else {
            final StringExpr stringExpr = StringExprParser.parseStringExpr(current, 0, allBlocks);
            return new WithId(stringExpr);
        }
    }

    static int getShadowIndicator(ArrayNode exprArray) {
        return exprArray.get(Constants.POS_INPUT_SHADOW).asInt();
    }
}
