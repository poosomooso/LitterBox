package scratch.structure.ast;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.NullNode;
import scratch.structure.ast.cblock.CBlock;
import scratch.structure.ast.cblock.RepeatBlock;
import scratch.structure.ast.inputs.Literal;
import scratch.structure.ast.inputs.Slot;
import scratch.structure.ast.inputs.SubstackSlot;
import scratch.structure.ast.reporter.OperatorAdd;
import scratch.structure.ast.reporter.ReporterBlock;
import scratch.structure.ast.stack.SingleIntInputBlock;
import scratch.structure.ast.transformers.Transformer;

import java.util.*;

import static scratch.structure.ast.Constants.*;

public class Ast {

    ScratchBlock root = null;

    public Map<String, ScratchBlock> getNodesIdMap() {
        return nodesIdMap;
    }

    Map<String, ScratchBlock> nodesIdMap = new HashMap();

    public void parseScript(JsonNode blocksNode) {
        buildBasicBlocks(blocksNode);
        connectAndFillSlots(blocksNode);
    }

    private void buildBasicBlocks(JsonNode blocksNode) {
        Iterator<String> it = blocksNode.fieldNames();

        while (it.hasNext()) {
            String nextId = it.next();
            JsonNode node = blocksNode.get(nextId);
            String opcode = node.get("opcode").toString().replaceAll("^\"|\"$", "");
            ScratchBlock block = Transformer.transformGeneric(Transformer.opCodeClassMapping.get(opcode), node, nextId);

            nodesIdMap.put(nextId, block);

            String parent = node.get("parent").toString();
            parent = parent.replaceAll("^\"|\"$", ""); //remove quotes around string
            if (parent.equals("null")) {
                root = block;
            }
        }
    }

    /**
     * Sets all parent and next blocks and fills the slots of blocks with inputs.
     * Currently filling the slots is only implemented for SingleIntInputBlocks.
     *
     * @param blocksNode The JsonNode containing all block information.
     */
    private void connectAndFillSlots(JsonNode blocksNode) {
        //Connect the blocks
        for (Map.Entry<String, ScratchBlock> blockIdAndBlock : nodesIdMap.entrySet()) {
            ScratchBlock block = blockIdAndBlock.getValue();
            // FIXME remove null check as soon as all blocks of the slottest fixture are implemented
            if (block instanceof Stackable && ((Stackable) block).getParent() == null) {
                JsonNode parentNode = blocksNode.get(blockIdAndBlock.getKey()).get("parent");
                if (parentNode != null && !(parentNode instanceof NullNode)) {
                    String parent = parentNode.toString();
                    parent = parent.replaceAll("^\"|\"$", ""); //remove quotes around string
                    ((Stackable) block).setParent((Extendable) nodesIdMap.get(parent));
                }
            }

            JsonNode nextNode = blocksNode.get(blockIdAndBlock.getKey()).get("next");
            // FIXME remove null check as soon as all blocks of the slottest fixture are implemented
            if (block instanceof Extendable && ((Extendable) block).getNext() == null && nextNode != null && !(nextNode instanceof NullNode)) {
                String next = nextNode.toString();
                next = next.replaceAll("^\"|\"$", ""); //remove quotes around string
                ((Extendable) block).setNext((Stackable) nodesIdMap.get(next));
            }
            if (block instanceof SingleIntInputBlock) {
                JsonNode inputs = blocksNode.get(blockIdAndBlock.getKey()).get("inputs");
                parseAndSetSingleIntInputs(inputs, (SingleIntInputBlock) block);
            } else if(block instanceof CBlock) {
                JsonNode inputs = blocksNode.get(blockIdAndBlock.getKey()).get("inputs");
                parseCBlockInputs(inputs, (CBlock) block);
            } else if (block instanceof ReporterBlock) {
                JsonNode inputs = blocksNode.get(blockIdAndBlock.getKey()).get("inputs");
                parseReporterBlockInputs(inputs, (ReporterBlock) block);
            }
        }
    }

    /**
     * Adds and fills in the slot of a ReporterBlock.
     *
     * @param inputs          The JsonNode containing the inputs of the block.
     * @param block           The CBlock of which the slot and substack is filled in.
     */
    private void parseReporterBlockInputs(JsonNode inputs, ReporterBlock block) {
        if (block instanceof OperatorAdd) {
            List<Map.Entry> slotEntries = new LinkedList<>();
            inputs.fields().forEachRemaining(slotEntries::add);
            Slot num1 = parseInputAtPos(slotEntries, 0);
            Slot num2 = parseInputAtPos(slotEntries, 1);
            ((OperatorAdd) block).setNum1(num1);
            ((OperatorAdd) block).setNum1(num2);
        }
    }

    /**
     * Adds and fills in the slot of a CBlock as well as its substacks.
     *
     * @param inputs          The JsonNode containing the inputs of the block.
     * @param block           The CBlock of which the slot and substack is filled in.
     */
    private void parseCBlockInputs(JsonNode inputs, CBlock block) {
       if (block instanceof RepeatBlock) {
           List<Map.Entry> slotEntries = new LinkedList<>();
           inputs.fields().forEachRemaining(slotEntries::add);
           Slot slot = parseInputAtPos(slotEntries, 0);
           ((RepeatBlock) block).setSlot(slot);

           ScriptBodyBlock subStackHead = parseSubstack(inputs);
           SubstackSlot substackSlot = new SubstackSlot("SUBSTACK", INPUT_BLOCK_NO_SHADOW, subStackHead);
           block.setSubstack(substackSlot);
       }
    }

    private ScriptBodyBlock parseSubstack(JsonNode node) {
        String subStackHeadID = node.get("SUBSTACK").get(1).asText();
        ScratchBlock substackHead = nodesIdMap.get(subStackHeadID);

        if (!(substackHead instanceof ScriptBodyBlock)) {
            throw new RuntimeException("Substack Block is not a ScriptBodyBlock. This should not be possible");
        }
        return (ScriptBodyBlock) substackHead;
    }

    /**
     * Extracts a single int input from an inputs array.
     */
    private Slot parseInputAtPos(List<Map.Entry> slotEntries, int pos) {
        Map.Entry slotEntry =  slotEntries.get(pos);
        String slotName = (String) slotEntry.getKey();
        ArrayNode inputArray = (ArrayNode) slotEntry.getValue();
        int shadowIndicator = inputArray.get(POS_INPUT_SHADOW).asInt();

        if (shadowIndicator == INPUT_DIFF_BLOCK_SHADOW) {
            String blockID;
            if (inputArray.get(POS_DATA_ARRAY).get(POS_INPUT_ID) instanceof ArrayNode) {
                // It is either a variable or a list primary input
                blockID = inputArray.get(POS_DATA_ARRAY).get(POS_INPUT_ID).toString().replaceAll("^\"|\"$", "");
            } else {
                // It is a block primary input
                blockID = inputArray.get(POS_DATA_ARRAY).asText(); //TODO add an own constant instead of using one with the same value
            }
            Input primary = (Input) nodesIdMap.get(blockID);
            int shadowType = inputArray.get(POS_SHADOW_ARRAY).get(POS_INPUT_TYPE).asInt();
            String shadowValue = inputArray.get(POS_SHADOW_ARRAY).get(POS_INPUT_VALUE).asText();
            Literal shadow = new Literal(shadowType, shadowValue);
            return new Slot(slotName, shadowIndicator, primary, shadow);

        } else if (shadowIndicator == INPUT_BLOCK_NO_SHADOW || shadowIndicator == INPUT_SAME_BLOCK_SHADOW) {
            //TODO find out what the difference between the meaning of these constants really is
            //There is no shadow, so create a new Literal and add it as primary.
            int type = inputArray.get(POS_DATA_ARRAY).get(POS_INPUT_TYPE).asInt();
            String value = inputArray.get(POS_DATA_ARRAY).get(POS_INPUT_VALUE).asText();
            Literal primary = new Literal(type, value);
            return new Slot(slotName, shadowIndicator, primary);
        }
        return null;
    }

    /**
     * Adds and fills in the slot of a SingleIntInputBlocks.
     *
     * @param inputs          The JsonNode containing the inputs of the block.
     * @param block           The SingleIntInputBlock of which the slot is filled in.
     */
    public void parseAndSetSingleIntInputs(JsonNode inputs, SingleIntInputBlock block) {
        List<Map.Entry> slotEntries = new LinkedList<>();
        inputs.fields().forEachRemaining(slotEntries::add);
        Slot slot = parseInputAtPos(slotEntries, 0);
        block.setSlot(slot);
    }

    public ScratchBlock getRoot() {
        return root;
    }
}
