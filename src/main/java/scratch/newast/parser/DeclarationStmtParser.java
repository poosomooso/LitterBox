package scratch.newast.parser;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.google.common.base.Preconditions;

import java.util.*;
import java.util.logging.Logger;

import scratch.newast.model.DeclarationStmt;
import scratch.newast.model.Message;
import scratch.newast.model.SetStmtList;
import scratch.newast.model.expression.Expression;
import scratch.newast.model.expression.bool.Bool;
import scratch.newast.model.expression.bool.BoolExpr;
import scratch.newast.model.expression.list.ExpressionList;
import scratch.newast.model.expression.list.ExpressionListPlain;
import scratch.newast.model.expression.num.NumExpr;
import scratch.newast.model.expression.num.Number;
import scratch.newast.model.expression.string.Str;
import scratch.newast.model.expression.string.StringExpr;
import scratch.newast.model.statement.common.SetAttributeTo;
import scratch.newast.model.statement.common.SetStmt;
import scratch.newast.model.statement.common.SetVariableTo;
import scratch.newast.model.type.BooleanType;
import scratch.newast.model.type.ListType;
import scratch.newast.model.type.NumberType;
import scratch.newast.model.type.StringType;
import scratch.newast.model.variable.Identifier;
import scratch.newast.model.variable.Qualified;
import scratch.newast.parser.attributes.RotationStyle;

import static scratch.newast.Constants.*;
import static scratch.newast.Constants.DRAG_KEY;

public class DeclarationStmtParser {

    public static List<DeclarationStmt> parseVariables(JsonNode variableNode, String actorName, boolean isStage) {
        Preconditions.checkNotNull(variableNode);
        List<DeclarationStmt> parsedVariables = new ArrayList<>();
        Iterator<Map.Entry<String, JsonNode>> iter = variableNode.fields();
        while (iter.hasNext()) {
            Map.Entry<String, JsonNode> currentEntry = iter.next();
            Preconditions.checkArgument(currentEntry.getValue().isArray());
            ArrayNode arrNode = (ArrayNode) currentEntry.getValue();
            if (arrNode.get(DECLARATION_VARIABLE_VALUE_POS).isNumber()) {
                ProgramParser.symbolTable.addVariable(currentEntry.getKey(),
                        arrNode.get(DECLARATION_VARIABLE_NAME_POS).textValue(),
                        new NumberType(), isStage, actorName);
                parsedVariables.add(new DeclarationStmt(new Identifier(arrNode.get(DECLARATION_VARIABLE_NAME_POS).textValue()), new NumberType()));
            } else if (arrNode.get(DECLARATION_VARIABLE_VALUE_POS).isBoolean()) {
                ProgramParser.symbolTable.addVariable(currentEntry.getKey(),
                        arrNode.get(DECLARATION_VARIABLE_NAME_POS).textValue(),
                        new BooleanType(), isStage, actorName);
                parsedVariables.add(new DeclarationStmt(new Identifier(arrNode.get(DECLARATION_VARIABLE_NAME_POS).textValue()), new BooleanType()));
            } else {
                ProgramParser.symbolTable.addVariable(currentEntry.getKey(),
                        arrNode.get(DECLARATION_VARIABLE_NAME_POS).textValue(),
                        new StringType(), isStage, actorName);
                parsedVariables.add(new DeclarationStmt(new Identifier(arrNode.get(DECLARATION_VARIABLE_NAME_POS).textValue()), new StringType()));
            }
        }
        return parsedVariables;
    }

    public static List<SetStmt> parseVariableDeclarationSetStmts(JsonNode variableNode, String actorName) {
        Preconditions.checkNotNull(variableNode);
        List<SetStmt> parsedVariables = new ArrayList<>();
        Iterator<Map.Entry<String, JsonNode>> iter = variableNode.fields();
        while (iter.hasNext()) {
            Map.Entry<String, JsonNode> currentEntry = iter.next();
            Preconditions.checkArgument(currentEntry.getValue().isArray());
            ArrayNode arrNode = (ArrayNode) currentEntry.getValue();
            //TODO check is ExpressionParser should be used
            if (arrNode.get(DECLARATION_VARIABLE_VALUE_POS).isNumber()) {
                parsedVariables.add(new SetVariableTo(new Qualified(new Identifier(actorName),
                        new Identifier(arrNode.get(DECLARATION_VARIABLE_NAME_POS).textValue())),
                        new Number((float) arrNode.get(DECLARATION_VARIABLE_VALUE_POS).asDouble())));
            } else if (arrNode.get(DECLARATION_VARIABLE_VALUE_POS).isBoolean()) {
                parsedVariables.add(new SetVariableTo(new Qualified(new Identifier(actorName),
                        new Identifier(arrNode.get(DECLARATION_VARIABLE_NAME_POS).textValue())),
                        new Bool(arrNode.get(DECLARATION_VARIABLE_VALUE_POS).asBoolean())));
            } else {
                parsedVariables.add(new SetVariableTo(new Qualified(new Identifier(actorName),
                        new Identifier(arrNode.get(DECLARATION_VARIABLE_NAME_POS).textValue())),
                        new Str(arrNode.get(DECLARATION_VARIABLE_VALUE_POS).textValue())));
            }
        }
        return parsedVariables;
    }

    public static List<DeclarationStmt> parseLists(JsonNode listsNode, String actorName, boolean isStage) {
        Preconditions.checkNotNull(listsNode);
        List<DeclarationStmt> parsedLists = new ArrayList<>();
        Iterator<Map.Entry<String, JsonNode>> iter = listsNode.fields();
        while (iter.hasNext()) {
            Map.Entry<String, JsonNode> currentEntry = iter.next();
            Preconditions.checkArgument(currentEntry.getValue().isArray());
            ArrayNode arrNode = (ArrayNode) currentEntry.getValue();
            String listName = arrNode.get(DECLARATION_LIST_NAME_POS).textValue();
            JsonNode listValues = arrNode.get(DECLARATION_LIST_VALUES_POS);
            Preconditions.checkArgument(listValues.isArray());
            ExpressionList expressionList = new ExpressionList(makeExpressionListPlain((ArrayNode) listValues));
            ProgramParser.symbolTable.addExpressionListInfo(currentEntry.getKey(), listName, expressionList, isStage,
                    actorName);
            parsedLists.add(new DeclarationStmt(new Identifier(listName), new ListType()));
        }
        return parsedLists;
    }

    private static ExpressionListPlain makeExpressionListPlain(ArrayNode valuesArray) {
        List<Expression> expressions = new ArrayList<>();
        for (int i = 0; i < valuesArray.size(); i++) {
            //TODO  check if expressionParser should be used
            expressions.add(new Str(valuesArray.get(i).textValue()));
        }
        return new ExpressionListPlain(expressions);
    }

    public static List<SetStmt> parseListDeclarationSetStmts(JsonNode listNode, String actorName) {
        Preconditions.checkNotNull(listNode);
        List<SetStmt> parsedLists = new ArrayList<>();
        Iterator<Map.Entry<String, JsonNode>> iter = listNode.fields();
        while (iter.hasNext()) {
            Map.Entry<String, JsonNode> currentEntry = iter.next();
            Preconditions.checkArgument(currentEntry.getValue().isArray());
            ArrayNode arrNode = (ArrayNode) currentEntry.getValue();
            String listName = arrNode.get(DECLARATION_LIST_NAME_POS).textValue();
            JsonNode listValues = arrNode.get(DECLARATION_LIST_VALUES_POS);
            Preconditions.checkArgument(listValues.isArray());
            parsedLists.add(new SetVariableTo(new Qualified(new Identifier(actorName), new Identifier(listName)),
                    makeExpressionListPlain((ArrayNode) listValues)));
        }
        return parsedLists;
    }

    public static List<DeclarationStmt> parseBroadcasts(JsonNode broadcastsNode, String actorName,
                                                        boolean isStage) {
        Preconditions.checkNotNull(broadcastsNode);
        List<DeclarationStmt> parsedBroadcasts = new ArrayList<>();
        Iterator<Map.Entry<String, JsonNode>> iter = broadcastsNode.fields();
        while (iter.hasNext()) {
            Map.Entry<String, JsonNode> current = iter.next();
            ProgramParser.symbolTable.addMessage(current.getValue().textValue(),
                    new Message(current.getValue().textValue()), isStage, actorName);
            parsedBroadcasts.add(new DeclarationStmt(new Identifier(current.getValue().textValue()), new StringType()));
        }
        return parsedBroadcasts;
    }

    public static List<SetStmt> parseAttributeDeclarationSetStmts(JsonNode actorDefinitionNode, String actorName) {
        //String ttSLang = "textToSpeechLanguage"; // Ignored as this is an extension

        StringExpr keyExpr;
        double jsonDouble;
        String jsonString;
        boolean jsonBool;
        NumExpr numExpr;
        StringExpr stringExpr;
        BoolExpr boolExpr;
        SetStmt setStmt;

        List<SetStmt> list = new LinkedList<>();

        keyExpr = new Str(VOLUME_KEY);
        jsonDouble = actorDefinitionNode.get(VOLUME_KEY).asDouble();
        numExpr = new Number((float) jsonDouble);
        setStmt = new SetAttributeTo(keyExpr, numExpr);
        list.add(setStmt);

        keyExpr = new Str(LAYERORDER_KEY);
        jsonDouble = actorDefinitionNode.get(LAYERORDER_KEY).asDouble();
        numExpr = new Number((float) jsonDouble);
        setStmt = new SetAttributeTo(keyExpr, numExpr);
        list.add(setStmt);

        if (actorDefinitionNode.get("isStage").asBoolean()) {

            keyExpr = new Str(TEMPO_KEY);
            jsonDouble = actorDefinitionNode.get(TEMPO_KEY).asDouble();
            numExpr = new Number((float) jsonDouble);
            setStmt = new SetAttributeTo(keyExpr, numExpr);
            list.add(setStmt);

            keyExpr = new Str(VIDTRANSPARENCY_KEY);
            jsonDouble = actorDefinitionNode.get(VIDTRANSPARENCY_KEY).asDouble();
            numExpr = new Number((float) jsonDouble);
            setStmt = new SetAttributeTo(keyExpr, numExpr);
            list.add(setStmt);

            keyExpr = new Str(VIDSTATE_KEY);
            jsonString = actorDefinitionNode.get(VIDSTATE_KEY).asText();
            stringExpr = new Str(jsonString);
            setStmt = new SetAttributeTo(keyExpr, stringExpr);
            list.add(setStmt);

        } else {

            keyExpr = new Str(VISIBLE_KEY);
            jsonBool = actorDefinitionNode.get(VISIBLE_KEY).asBoolean();
            boolExpr = new Bool(jsonBool);
            setStmt = new SetAttributeTo(keyExpr, boolExpr);
            list.add(setStmt);

            keyExpr = new Str(X_KEY);
            jsonDouble = actorDefinitionNode.get(X_KEY).asDouble();
            numExpr = new Number((float) jsonDouble);
            setStmt = new SetAttributeTo(keyExpr, numExpr);
            list.add(setStmt);

            keyExpr = new Str(Y_KEY);
            jsonDouble = actorDefinitionNode.get(Y_KEY).asDouble();
            numExpr = new Number((float) jsonDouble);
            setStmt = new SetAttributeTo(keyExpr, numExpr);
            list.add(setStmt);

            keyExpr = new Str(SIZE_KEY);
            jsonDouble = actorDefinitionNode.get(SIZE_KEY).asDouble();
            numExpr = new Number((float) jsonDouble);
            setStmt = new SetAttributeTo(keyExpr, numExpr);
            list.add(setStmt);

            keyExpr = new Str(DIRECTION_KEY);
            jsonDouble = actorDefinitionNode.get(DIRECTION_KEY).asDouble();
            numExpr = new Number((float) jsonDouble);
            setStmt = new SetAttributeTo(keyExpr, numExpr);
            list.add(setStmt);

            keyExpr = new Str(DRAG_KEY);
            jsonBool = actorDefinitionNode.get(DRAG_KEY).asBoolean();
            boolExpr = new Bool(jsonBool);
            setStmt = new SetAttributeTo(keyExpr, boolExpr);
            list.add(setStmt);

            keyExpr = new Str(ROTATIONSTYLE_KEY);
            jsonString = actorDefinitionNode.get(ROTATIONSTYLE_KEY).textValue();
            stringExpr = new Str(jsonString);
            setStmt = new SetAttributeTo(keyExpr, stringExpr);
            list.add(setStmt);
        }
        return list;
    }
}
