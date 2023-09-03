package com.sonatafi;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;


public class JsonToolTest {
    

    @Test
    public void givenNotChangedJSON() throws Exception {
        JsonTool parserJsonTool = new JsonTool();
        List<ChangeType> list = parserJsonTool.parserJson("{\"property\": \"firstName\", \"previous\": \"James\", \"current\": \"James\"}");
        assertTrue(list.isEmpty());
    }

    @Test
    public void givenChangedJSON() throws Exception {
        JsonTool parserJsonTool = new JsonTool();
        List<ChangeType> list = parserJsonTool.parserJson("{\"property\": \"firstName\", \"previous\": \"James\", \"current\": \"Jim\"}");
        assertFalse(list.isEmpty());
    }

    @Test
    public void givenNotChangedNested() throws Exception {
        JsonTool parserJsonTool = new JsonTool();
        List<ChangeType> list = parserJsonTool.parserJson("{\"property\": \"subscription.status\", \"previous\": \"ACTIVE\", \"current\": \"ACTIVE\"}");
        assertTrue(list.isEmpty());
    }

    @Test
    public void givenChangedNested() throws Exception {
        JsonTool parserJsonTool = new JsonTool();
        List<ChangeType> list = parserJsonTool.parserJson("{\"property\": \"subscription.status\", \"previous\": \"ACTIVE\", \"current\": \"EXPIRED\"}");
        assertFalse(list.isEmpty());
    }


    @Test
    public void givenNotChangedIdBracket() throws Exception {
        JsonTool parserJsonTool = new JsonTool();
        List<ChangeType> list = parserJsonTool.parserJson("{\"property\": \"vehicles[id].displayName\", \"previous\": \"My Car\", \"current\": \"My Car\"}");
        assertTrue(list.isEmpty());
    }

    @Test
    public void givenChangedIdBracket() throws Exception {
        JsonTool parserJsonTool = new JsonTool();
        List<ChangeType> list = parserJsonTool.parserJson("{\"property\": \"vehicles[id].displayName\", \"previous\": \"My Car\", \"current\": \"23 Ferrari 296 GTS\"}");
        assertFalse(list.isEmpty());
    }

    @Test
    public void givenChangedBracketsWrongId() throws Exception {
        JsonTool parserJsonTool = new JsonTool();
        Exception exception = assertThrows(
            Exception.class, 
            () -> parserJsonTool.parserJson("{\"property\": \"vehicles[v_1].displayName\", \"previous\": \"My Car\", \"current\": \"23 Ferrari 296 GTS\"}"));
        assertEquals(
            "Audit system lacks the information it needs to determine what has changed.", 
            exception.getMessage());
    }

    @Test
    public void givenTrackedListJSON() throws Exception {
        JsonTool parserJsonTool = new JsonTool();
        List<ChangeType> list = parserJsonTool.parserJson("{\"property\": \"services\", \"added\": [\"Oil Change\", \"Fuel\"], \"removed\": [\"Interior/Exterior Wash\"]}");
        assertFalse(list.isEmpty());
    }


    @Test
    public void givenNullCurrent() throws Exception {
        JsonTool parserJsonTool = new JsonTool();
        List<ChangeType> list = parserJsonTool.parserJson("{\"property\": \"firstName\", \"previous\": \"James\", \"current\": null}");
        assertFalse(list.isEmpty());
    }

    @Test
    public void givenNullPrevious() throws Exception {
        JsonTool parserJsonTool = new JsonTool();
        List<ChangeType> list = parserJsonTool.parserJson("{\"property\": \"firstName\", \"previous\": null, \"current\": \"Jim\"}");
        assertFalse(list.isEmpty());
    }

    @Test
    public void givenNullBoth() throws Exception {
        JsonTool parserJsonTool = new JsonTool();
        List<ChangeType> list = parserJsonTool.parserJson("{\"property\": \"firstName\", \"previous\": null, \"current\": null}");
        assertTrue(list.isEmpty());
    }


}
