package com.sonatafi;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

public class Main {

    public static void main(String[] args) throws Exception {
        
        try {

            JsonTool diffTool = new JsonTool();

            Main main = new Main();
            InputStream is = main.getFileAsIOStream("properties.txt");
            
            try (InputStreamReader isr = new InputStreamReader(is); 
                BufferedReader br = new BufferedReader(isr);) 
            {
                String line;
                while ((line = br.readLine()) != null) {                    
                    List<ChangeType> list = diffTool.parserJson(line);

                    for (ChangeType a : list) {

                        if (a instanceof PropertyUpdate) {
                            System.out.println(
                                "Property: " + ((PropertyUpdate)a).getProperty() + 
                                " was updated from " + ((PropertyUpdate)a).getPrevious() + 
                                " to " + ((PropertyUpdate)a).getCurrent()
                            );
                        } 

                        if (a instanceof ListUpdate) {
                            System.out.println(
                                "Property: " + ((ListUpdate)a).getProperty() + 
                                " was updated from " + ((ListUpdate)a).getAdded() + 
                                " to " + ((ListUpdate)a).getRemoved()
                            );
                        } 

                    }
                }
                is.close();
            }
            
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }


    }

    private InputStream getFileAsIOStream(final String fileName) 
    {
        InputStream ioStream = this.getClass()
            .getClassLoader()
            .getResourceAsStream(fileName);
        
        if (ioStream == null) {
            throw new IllegalArgumentException(fileName + " is not found");
        }
        return ioStream;
    }

}