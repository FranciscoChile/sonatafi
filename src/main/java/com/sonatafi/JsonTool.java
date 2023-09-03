package com.sonatafi;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonTool {
    
    ObjectMapper objectMapper = new ObjectMapper();

    public List<ChangeType> parserJson (String json) throws Exception {

        List<ChangeType> list = new ArrayList<>();
        DiffTool diffTool = new DiffTool();
        JsonValidatorSchema jsonValidatorSchema = new JsonValidatorSchema();

        int errorsDiff = jsonValidatorSchema.validator(json, "/schemaDiff.json");

        if (errorsDiff == 0) {            
            PropertyUpdate p = objectMapper.readValue(json, PropertyUpdate.class);
            
                if (p.getProperty().indexOf(".") != -1) {
                    String listName = p.getProperty().substring(0, p.getProperty().indexOf("."));
                    String[] id = listName.split("[\\[\\]]", -1);

                    if (id.length > 1) {
                        if (id[1].equals("id") || id[1].equals("@AuditKey")) {
                            List<ChangeType> a = diffTool.diff(p.getCurrent(), p.getPrevious());
                            if (a.size() > 0) {
                                PropertyUpdate propertyUpdate = (PropertyUpdate)a.get(0);
                                propertyUpdate.setProperty(p.getProperty());
                                list.add(propertyUpdate);
                            }
                        }
                        else {
                            throw new Exception("Audit system lacks the information it needs to determine what has changed.");
                        }
                    } else {
                        List<ChangeType> a = diffTool.diff(p.getCurrent(), p.getPrevious());
                        if (a.size() > 0) {
                            PropertyUpdate propertyUpdate = (PropertyUpdate)a.get(0);
                            propertyUpdate.setProperty(p.getProperty());
                            list.add(propertyUpdate);
                        }
                    }
                } else {
                    List<ChangeType> a = diffTool.diff(p.getCurrent(), p.getPrevious());
                    if (a.size() > 0) {
                        PropertyUpdate propertyUpdate = (PropertyUpdate)a.get(0);
                        propertyUpdate.setProperty(p.getProperty());
                        list.add(propertyUpdate);
                    }
                }
                        
        }
        
        int errorsDiffList = jsonValidatorSchema.validator(json, "/schemaDiffList.json");

        if (errorsDiffList == 0) {
            ListUpdate l = objectMapper.readValue(json, ListUpdate.class);
            list.add(l);
        }


        return list;
    }


}
