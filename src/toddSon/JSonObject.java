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
        
        StringBuffer item = new StringBuffer();
        StringBuffer key = new StringBuffer();
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
                               key.append(current);
                            }
                            else
                               item.append(current);
                         
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
                            item.delete(0, item.length());
                            inKey = false;
                        }
                        else 
                            item.append(current);
                        break;
                        
                    case ',':

                            elements.put(key.toString(), item.toString());
                            key.setLength(0);
                            inKey = true;
                        break;
                        
                    case '{': case '[':

                            lefthanded ++;
                        break;
                        
                    default:
                        if(inKey)
                        {
                             key.append(current);
                        }
                        else
                             item.append(current);
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
                    elements.put(key.toString(), item.toString());
                    key.setLength(0);
                    inKey = true;
                }
                else if(lefthanded != 0)
                {
                    item.append(current);
                }
            }
            //System.out.println(index+"  "+key+"  "+item);
            index++;
        }
        elements.put(key.toString(), item.toString());
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
        JSonObject inside = new JSonObject(jso.get("Sub\\\"set"));
        System.out.println(inside);
    }
}
