/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
//package tools;
/*
 *  StandAloneAnnie.java
 *
 *
 * Copyright (c) 2000-2001, The University of Sheffield.
 *
 * This file is part of GATE (see http://gate.ac.uk/), and is free
 * software, licenced under the GNU Library General Public License,
 * Version 2, June1991.
 *
 * A copy of this licence is included in the distribution in the file
 * licence.html, and is also available at http://gate.ac.uk/gate/licence.html.
 *
 *  hamish, 29/1/2002
 *
 *  $Id: StandAloneAnnie.java,v 1.6 2006/01/09 16:43:22 ian Exp $
 */

import java.util.*;
import java.io.*;
import java.net.*;

import gate.*;
import gate.creole.*;
import gate.util.*;


/**
 * This class illustrates how to use ANNIE as a sausage machine
 * in another application - put ingredients in one end (URLs pointing
 * to documents) and get sausages (e.g. Named Entities) out the
 * other end.
 * <P><B>NOTE:</B><BR>
 * For simplicity's sake, we don't do any exception handling.
 */
public class NEtagger {

     static  NEtagger annie;
     static Corpus corpus ;
    /** The Corpus Pipeline application to contain ANNIE */
    private SerialAnalyserController annieController;

    /**
     * Initialise the ANNIE system. This creates a "corpus pipeline"
     * application that can be used to run sets of documents through
     * the extraction system.
     */
    public void initAnnie() throws GateException {
        Out.prln("Initialising ANNIE...");

        // create a serial analyser controller to run ANNIE with
        annieController =
                (SerialAnalyserController) Factory.createResource(
                "gate.creole.SerialAnalyserController", Factory.newFeatureMap(),
                Factory.newFeatureMap(), "ANNIE_" + Gate.genSym());

        // load each PR as defined in ANNIEConstants
        for (int i = 0; i < ANNIEConstants.PR_NAMES.length; i++) {
            FeatureMap params = Factory.newFeatureMap(); // use default parameters
            ProcessingResource pr = (ProcessingResource) Factory.createResource(ANNIEConstants.PR_NAMES[i], params);

            // add the PR to the pipeline controller
            annieController.add(pr);

        } // for each ANNIE PR

        Out.prln("...ANNIE loaded");
    } // initAnnie()

    /** Tell ANNIE's controller about the corpus you want to run on */
    public void setCorpus(Corpus corpus) {
        annieController.setCorpus(corpus);
    }
     public void setDoc(Document doc) {
        annieController.setDocument(doc);
    }// setCorpus

    /** Run ANNIE */
    public void execute() throws GateException {
       // Out.prln("Running ANNIE...");
        annieController.execute();
       // Out.prln("...ANNIE complete");
    } // execute()

    /**
     * Run from the command-line, with a list of URLs as argument.
     * <P><B>NOTE:</B><BR>
     * This code will run with all the documents in memory - if you
     * want to unload each from memory after use, add code to store
     * the corpus in a DataStore.
     */
    public static void GateIntialize()
            throws GateException, IOException {
        // initialise the GATE library
        Out.prln("Initialising GATE...");
        Gate.init();

        // Load ANNIE plugin
        File gateHome = Gate.getGateHome();
        File pluginsHome = new File(gateHome, "plugins");
        Gate.getCreoleRegister().registerDirectories(new File(pluginsHome, "ANNIE").toURL());
        Out.prln("...GATE initialised");

        // initialise ANNIE (this may take several minutes)
        annie = new NEtagger();
        annie.initAnnie();
        corpus = (Corpus) Factory.createResource("gate.corpora.CorpusImpl");
        annie.setCorpus(corpus);
    }


    public static String tagString(String str) throws GateException{
        // create a GATE corpus and add a document for each command-line
        // argument

        if(str.isEmpty() || str==null)return "";
        

       
        FeatureMap params = Factory.newFeatureMap();
        params.put("stringContent", str);
        params.put("preserveOriginalContent", new Boolean(false));
        params.put("collectRepositioningInfo", new Boolean(false));
        Document doc = (Document) Factory.createResource("gate.corpora.DocumentImpl", params);
        corpus.clear();
        corpus.cleanup();
        corpus.add(doc);

       
        annie.setDoc(doc);
        annie.execute();

        // for each document, get an XML document with the
        // person and location names added

        AnnotationSet defaultAnnotSet = ((Document)corpus.get(0)).getAnnotations();
        Set annotTypesRequired = new HashSet();
        annotTypesRequired.add("Person");
        annotTypesRequired.add("Location");
        annotTypesRequired.add("Organization");

        Set<Annotation> NEs = new HashSet<Annotation>(defaultAnnotSet.get(annotTypesRequired));

      return doc.toXml(NEs, false);

         
    }
}

 
