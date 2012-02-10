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
        boolean prevSlash = false;
        while(index < input.length())
        {
            char current = input.charAt(index);
            if(lefthanded == 0)
            {
                if(inQuote)
                {
                    
                    
                    if(current == '\\')
                        prevSlash = !prevSlash;
                    
                    
                    if(current == '"' && !prevSlash)
                        inQuote = false;
                    else
                    {
                         if(inKey)
                            {
                               key = key+current;
                            }
                            else
                               item = item+current;
                         
                    }
                    
                    if(prevSlash && current != '\\')
                        prevSlash = false;
                }
                else switch(current)
                {
                    case '"':
                        inQuote = !inQuote;
                        break;
                        
                    case ':':
                        if(inKey)
                        {
                            item = "";
                            inKey = false;
                        }
                        else 
                            item = item+current;
                        break;
                        
                    case ',':

                            elements.put(key, item);
                            key = "";
                            inKey = true;
                        break;
                        
                    case '{': case '[':

                            lefthanded ++;
                        break;
                        
                    default:
                        if(inKey)
                        {
                             key = key+current;
                        }
                        else
                             item = item+current;
                        break;
                        
                }
                
            }
            else
            {
                if(inQuote)
                {
                   /* if(inKey)
                    {
                        key = key+current;
                    }
                    else
                    item = item+current;*/
                }
                else
                {
                    switch(current)
                     {
                        case '{': case '[':
                            lefthanded ++;
                            break;
                        
                        case '}': case ']':
                            lefthanded --;
                            break;
                     }
                }
                
                if(current == '"')
                    inQuote = !inQuote;
                
                if(lefthanded == 0)
                {
                    elements.put(key, item);
                    key = "";
                    inKey = true;
                }
                else if(lefthanded != 0)
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
            return "null";
            //throw new NoSuchElementException("Could not find element: "+key);
        }
    }
    public boolean contains(String key)
    {
        return elements.containsKey(key);
    }
    private Map<String,String> elements;
    
    public static void main(String args[])
    {
        JSonObject jso = new JSonObject("\"Test Key\":\"Test Value\",\"Sub\\\"set\":{\"Ke:y\":\"Value\"}\"next\":\"item\"");
        System.out.println(jso);
        JSonObject inside = new JSonObject(jso.get("Subset"));
        System.out.println(inside);
    }
}
