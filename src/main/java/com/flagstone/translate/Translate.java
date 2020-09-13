/*
 * Translate.java
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
package com.flagstone.translate;

/**
 * The %Translate class defines constants and methods used throughout the %Translate package. 
 */
public final class Translate 
{
    // Package information  
    static final boolean DEBUG = false;

    /** 
     * MAJOR is used to identify the current version of the framework. This
     * is incremented for each new version of Flash supported.
     */
    public static final int MAJOR = 2;
    /** 
     * MINOR is used to identify the current minor version of the framework. This
     * is incremented when new functionality is added or API changes are made.
     */
    public static final int MINOR = 0;
    /** 
     * The RELEASE number is used to differentiate between different releases. 
     * This number is incremented when an enhancement or bug fix has been made 
     * and the API is unchanged.
     */
    public static final int RELEASE = 2;

    /** 
     * The main method reports basic information about the package.
     */    
    public static void main(String args[]) 
    {
        String version = MAJOR + "." + MINOR + "." + RELEASE;

        System.out.println("/**");

        System.out.println(
            " * Translate for ActionScript 1.0, Version " + version);
        
        if (Translate.DEBUG)
            System.out.println(" * Debug Edition.");
                    
        System.out.println(" * ");
        System.out.println(" * Copyright Flagstone Software Limited, 2001-2004.");
        System.out.println(" * All Rights Reserved.");
        System.out.println(" * ");
        System.out.println(" * Use of this software is subject to the terms in the license");
        System.out.println(" * that accompanied the software.");
        System.out.println(" * ");
        System.out.println(" */");
    }
}