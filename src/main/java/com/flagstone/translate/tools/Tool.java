/*
 * Tool.java
 * Translate AS1
 *
 * A compiler for ActionScript 1.x
 * Copyright (c) 2003-2006 Flagstone Software Ltd. All rights reserved.
 *
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later 
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place, Suite 330, Boston, MA 02111-1307 USA
 * 
 * For more information please contact:
 * 
 * Stuart MacKay
 * Flagstone Software Ltd
 * 92 High Street
 * Wick, Caithness KW1 4LY
 * Scotland
 * 
 * www.flagstonesoftware.com
 */
package com.flagstone.translate.tools;

import java.io.File;
import java.io.FilenameFilter;
import java.util.*;

public abstract class Tool implements FilenameFilter
{
    protected HashMap options = new HashMap();
    
    protected String extension = null; 
    
    public Tool(String[] args)
    {
        getOptions(args);
    }
    public void getOptions(String[] args)
    {
        String optionName = null;
        String optionValue = null;
        
        for (int i=0; i<args.length; i++)
        {
            if (args[i].length() >= 2 && args[i].substring(0, 2).equals("--"))
            {
                optionName = args[i].substring(2);
                optionValue = "";
                
                if (i+1 < args.length)
                    optionValue = args[i+1];
                    
                options.put(optionName, optionValue);
            }
        }
    }
    public String getOption(String name, String defaultValue)
    {
        String value = defaultValue;
        
        if (options.containsKey(name))
            value = (String)options.get(name);
    
        return value;
    }
    public int getOption(String name, int defaultValue)
    {
        int value = defaultValue;
        
        if (options.containsKey(name))
        {
            try {
                value = new Integer((String)options.get(name)).intValue();
            }
            catch (NumberFormatException e) {
            }
        }
        return value;
    }
    public String[] getFiles(String dirname)
    {
        File dir = new File(dirname);
        String filenames[] = null;
         
        if (dir.isDirectory())
            filenames = dir.list(this);

        return filenames;
    }
    public boolean accept(File dir, String name)
    {
        return name.endsWith(extension);
    }
}