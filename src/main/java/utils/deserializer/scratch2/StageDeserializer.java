package utils.deserializer.scratch2;

import com.fasterxml.jackson.databind.JsonNode;
import scratch.data.*;
import scratch.structure.Stage;

import java.util.List;

/**
 * More information about the JSON Scratch 2 file format and its JSON arrays and nodes:
 * https://en.scratch-wiki.info/wiki/Scratch_File_Format
 */
public class StageDeserializer {

    /**
     * Deserialize the JSON String and creating a Stage object
     * @param rootNode the JsonNode to deserialize
     * @return a Stage object
     */
    public static Stage deserialize(JsonNode rootNode) {
        String name = rootNode.get("objName").asText();
        List<Script> scripts = ScriptDeserializer.deserialize(rootNode);
        List<Comment> comments = CommentDeserializer.deserialize(rootNode);
        List<ScVariable> variables = VariableListDeserializer.deserialize(rootNode);
        List<ScList> lists = ListDeserializer.deserialize(rootNode);
        List<Costume> costumes = CostumeDeserializer.deserialize(rootNode);
        List<Sound> sounds = SoundDeserializer.deserialize(rootNode);
        int initCostume = rootNode.get("currentCostumeIndex").asInt();

        return new Stage(name, scripts, comments, variables, lists, costumes, sounds, initCostume, rootNode);
    }
}
