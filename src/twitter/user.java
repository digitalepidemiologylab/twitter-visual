/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package twitter;

import toddSon.JSonObject;

/**
 *
 * @author toddbodnar
 */
public class user {
    public user(String input)
    {
        JSonObject full = new JSonObject(input);
        
        is_translator = full.get("is_translator").equals("true");
        show_all_inline_media = full.get("show_all_inline_media").equals("true");
        geo_enabled = full.get("geo_enabled").equals("true");
        profile_use_background_image = full.get("profile_use_background_image").equals("true");
        
        listed_count = Integer.parseInt(full.get("listed_count"));
        followers_count = Integer.parseInt(full.get("followers_count"));
        favourites_count = Integer.parseInt(full.get("favourites_count"));
        friends_count = Integer.parseInt(full.get("friends_count"));
        
        id_str = full.get("id_str");
        profile_image_url = full.get("profile_image_url");
        profile_link_color = full.get("profile_link_color");
        profile_sidebar_border_color = full.get("profile_sidebar_border_color");
        profile_text_color = full.get("profile_text_color");
        profile_sidebar_fill_color = full.get("profile_sidebar_fill_color");
        
        description = full.get("description");
        name = full.get("name");
        screen_name = full.get("screen_name");
        location = full.get("location");
        lang = full.get("lang");
        
        
    }
    
    public String toString()
    {
        return id_str+" "+name+" "+screen_name+" "+lang+" "+is_translator+"  " +description;
    }
    public boolean is_translator, show_all_inline_media, geo_enabled, profile_use_background_image;
    public int listed_count, followers_count, favourites_count, friends_count;
    public String id_str, profile_image_url, profile_link_color, profile_sidebar_border_color, profile_text_color, profile_sidebar_fill_color;
    public String description, name, screen_name, location, lang;
}
