package com.hw3.Actors

import com.hw3.Utils._
import akka.actor.Actor
import com.hw3.Patterns.Messages.{DepGraph, DepGraphResult}
import com.scitools.understand._
import org.jgrapht.graph.{DefaultEdge, SimpleDirectedGraph}


/**
  * Created by krbalmryde on 11/6/16.
  */
class DepGraphActor extends Actor {
    var supervisor = self
    val resourceDir = s"$pwd/src/main/resources"

    /**
      * A recursive function, extracts the references found within the provided entity based on the kindstring
      * @param graph a SimpleDirectedGraph[String, DefaultEdge]) object
      * @param parent an Entity object, can be null. Used to ensure we link the generated edges correctly
      * @param entity an Entity object, The entity we are performing queries on
      * @param kindString a String object, contains the search pattern we want to extract
      * @param indent a String object, used for debugging purposes only in order better understand the relationship
      */
    //
    def graphEntity(graph:SimpleDirectedGraph[String, DefaultEdge], parent:Entity, entity:Entity, kindString:String, indent:String): Unit ={

        val typedRefs = entity.refs(kindString, "~unknown ~unresolved", true)
        val entityName = entity.name

        if (graph.containsVertex(entityName)) return
        else graph.addVertex(entityName)

        if (parent != null) {
            val parentName = parent.name
            graph.addEdge(parentName, entityName)
        }
        //        println(indent+entity.kind.name+"++"+entityName)
        typedRefs.foreach( ref => {
            graphEntity(graph, entity, ref.ent, kindString, "  "+indent)
        })
    }

    def receive = {

        case DepGraph(id, name) =>{
            println (s"Generating Dependency Graph for $id/$name")

            val inputFile = s"$resourceDir/$id/$name.udb"

            // The Understand Database Resource file.
            val dataBase:Database = Understand.open( inputFile )

            // The jGrapht Dependency Graph object.
            var dependencyGraph: SimpleDirectedGraph[String, DefaultEdge] = new SimpleDirectedGraph[String, DefaultEdge](classOf[DefaultEdge])

            // First, get a list of all classes, interfaces, or packages
            val types:String = "method ~unknown ~unresolved"
            // Generate the dependency graph using all methods defined internally
            val pFiles:Array[Entity] = dataBase.ents(types)

            println(s"Generating Dependency Graph now for $name.udb. Please wait....")
            pFiles.foreach( fileEntity => {
                println(fileEntity)
                graphEntity(dependencyGraph, null, fileEntity, "callby", " ")
                println(dependencyGraph)

            })

            // Give the sender what it wants!
            sender ! DepGraphResult(dependencyGraph)

        }
    }

}
