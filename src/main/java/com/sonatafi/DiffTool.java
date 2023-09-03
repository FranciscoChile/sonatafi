package com.sonatafi;

import java.util.ArrayList;
import java.util.List;

public class DiffTool {
    
    List<ChangeType> diff (String previous, String current) {

        List<ChangeType> list = new ArrayList<>();
        PropertyUpdate p = new PropertyUpdate();

        if (previous == null && current == null) return list;
        if (previous != null && current != null && previous.equals(current)) return list;

        p.setProperty(null);
        if (previous == null) p.setPrevious(previous);
        if (current == null) p.setCurrent(current);

        if (previous != null && current != null && !previous.equals(current)) {            
            p.setCurrent(current);
            p.setPrevious(previous);            
        }        
        list.add(p);

        return list;

    }
}
