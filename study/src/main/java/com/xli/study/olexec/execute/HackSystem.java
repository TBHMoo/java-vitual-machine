/*
 * Copyright (c) 1994, 2013, Oracle and/or its affiliates. All rights reserved.
 * ORACLE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 */
package com.xli.study.olexec.execute;

import sun.reflect.CallerSensitive;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.channels.Channel;
import java.util.Properties;

public final class HackSystem {



    /** Don't let anyone instantiate this class */
    private HackSystem() {
    }


    public final static InputStream in = null;
    public final static PrintStream out = new HackPrintStream();
    public final static PrintStream err = out;

    public static String getBufferString(){
        return out.toString();
    }

    public static void closeBuffer(){
        out.close();
//        in.close();
    }


    public static void setIn(InputStream in) {
//        ctsEtInkIO();
//        setIn0(in);
        throw new SecurityException("Use hazardous method: System.setIn().");
    }

    public static void setOut(PrintStream out) {
//        ctsEtOutkIO();
//        setOut0(out);
        throw new SecurityException("Use hazardous method: System.setIn().");
    }

    public static void setErr(PrintStream err) {
//        ctsEtErrkIO();
//        setErr0(err);
        throw new SecurityException("Use hazardous method: System.setIn().");
    }


    public static Channel inheritedChannel() throws IOException {
        throw new SecurityException("Use hazardous method: System.inheritedChannel().");
    }



//    private static native void setIn0(InputStream in);
//    private static native void setOut0(PrintStream out);
//    private static native void setErr0(PrintStream err);


    public static SecurityManager getSecurityManager() {
        throw new SecurityException("Use hazardous method: System.setGetSecurityManager().");
    }



    public static long currentTimeMillis() {
        return System.currentTimeMillis();
    }

    public static long nanoTime() {
        return System.nanoTime();
    }

    public static void arraycopy(Object src, int srcPos, Object dest, int destPos, int length) {
        System.arraycopy(src, srcPos, dest, destPos, length);
    }

    public static int identityHashCode(Object x) {
        return System.identityHashCode(x);
    }

    public static Properties getProperties() {
        throw new SecurityException("Use hazardous method: System.getProperties().");
    }

    public static String lineSeparator() {
        throw new SecurityException("Use hazardous method: System.setLineSeparator().");
    }

    private static String lineSeparator;

    public static void setProperties(Properties props) {
        throw new SecurityException("Use hazardous method: System.setSetProperties().");
    }


    public static String getProperty(String key) {
        throw new SecurityException("Use hazardous method: System.setGetProperty().");
    }

    public static String getProperty(String key, String def) {
        throw new SecurityException("Use hazardous method: System.setGetProperty().");
    }

    public static String setProperty(String key, String value) {
        throw new SecurityException("Use hazardous method: System.setSetProperty().");
    }

    public static String clearProperty(String key) {
        throw new SecurityException("Use hazardous method: System.setClearProperty().");
    }

    private static void checkKey(String key) {
        throw new SecurityException("Use hazardous method: System.setIn().");
    }


    public static String getenv(String name) {
        throw new SecurityException("Use hazardous method: System.setGetenv().");
    }


    public static java.util.Map<String,String> getenv() {
        throw new SecurityException("Use hazardous method: System.setUtil().");
    }


    public static void exit(int status) {
        throw new SecurityException("Use hazardous method: System.setExit().");
    }

    public static void gc() {
        throw new SecurityException("Use hazardous method: System.setGc().");
    }

    public static void runFinalization() {
        throw new SecurityException("Use hazardous method: System.setRunFinalization().");
    }

    @Deprecated
    public static void runFinalizersOnExit(boolean value) {
        throw new SecurityException("Use hazardous method: System.setRunFinalizersOnExit().");
    }


    @CallerSensitive
    public static void load(String filename) {
        throw new SecurityException("Use hazardous method: System.setLoad().");
    }


    @CallerSensitive
    public static void loadLibrary(String libname) {
        throw new SecurityException("Use hazardous method: System.setLoadLibrary().");
    }


    public static  String mapLibraryName(String libname){
        throw new SecurityException("Use hazardous method: System.setMapLibraryName().");
    }





}
