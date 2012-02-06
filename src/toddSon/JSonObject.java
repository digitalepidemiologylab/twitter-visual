/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package toddSon;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 *
 * @author toddbodnar
 */
public class JSonObject {
    public JSonObject(String input)
    {
        elements = new HashMap<String, String>();
        
        String item = "";
        String key = "";
        int index = 0;
        int lefthanded = 0; //the number of { and [ without matching } and ] in the file at the point
        boolean inQuote = false;
        boolean inKey = true;
        
        while(index < input.length())
        {
            char current = input.charAt(index);
            if(lefthanded == 0)
            {
                if(current == '"')
                {
                    if(inQuote)
                    {
                        inQuote = false;
                    }
                    else
                    {
                        inQuote = true;
                        
                    }
                }
                else if(current == ':' && inKey)
                {
                    item = "";
                    inKey = false;
                }
                else if(current == ',' && !inQuote)
                {
                    elements.put(key, item);
                    key = "";
                    inKey = true;
                }
                else if(current == '{' || current == '[')
                {
                    lefthanded ++;
                }
                else if(inKey)
                {
                    key = key+current;
                }
                else
                    item = item+current;
            }
            else
            {
                
                if(current == '{' || current == '[')
                {
                    lefthanded ++;
                }
                else if(current == '}' || current == ']')
                {
                    lefthanded --;
                }
                if(lefthanded != 0)
                {
                    item = item+current;
                }
            }
            //System.out.println(index+"  "+key+"  "+item);
            index++;
        }
        elements.put(key, item);
    }
    public String toString()
    {
        String s = "";
        for (String key: elements.keySet())
        {
            s += (key+"  :  "+elements.get(key)) + '\n';
        }
        return s;
    }
    public String get(String key)throws NoSuchElementException
    {
        if(elements.containsKey(key))
        {
            return elements.get(key);
        }
        else
        {
            //return "";
            throw new NoSuchElementException("Could not find element: "+key);
        }
    }
    public boolean contains(String key)
    {
        return elements.containsKey(key);
    }
    private Map<String,String> elements;
    
    public static void main(String args[])
    {
        JSonObject jso = new JSonObject("\"Test Key\":\"Test Value\",\"Subset\":{\"Key\":\"Value\"}");
        System.out.println(jso);
        JSonObject inside = new JSonObject(jso.get("Subset"));
        System.out.println(inside);
    }
}
