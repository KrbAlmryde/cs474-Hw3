package com.hw3.Actors

import akka.actor.{Actor, PoisonPill, Props}
import com.hw3.Patterns.Messages._
import com.hw3.Utils._
import com.scitools.understand.{Database, Entity, Understand, UnderstandException}
import org.jgrapht.graph.{DefaultEdge, SimpleDirectedGraph}

/**
  * Created by krbalmryde on 10/29/16.
  *
  * UnderstandActor acts as more of a communciation hub than an actual worker itself.
  * It creates a Actor children to:
  *     generate the UDB Files (ProcessActor)
  *     produce the Dependency Graph (self)
  */
class UnderstandActor extends Actor {
    var supervisor = self
    var repo = RepoDetails("","","","")

    def receive = {

        // Entry point of Actor
        case GenUDB(id, name, language) => {
            supervisor = sender
            context.actorOf(Props[ProcessActor], name="udbProcess") ! GenUDB(id, name, language)
        }

        // Result of Generating the .udb file
        case UDBResult(0, id, name) => {
            // At confirmation that the process was successful, kill the Actor in charge of the task
            sender ! PoisonPill

            val inputFile = s"$resourceDir/$id/$name.udb"
            try {

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
                    graphEntity(dependencyGraph, null, fileEntity, "definein definein call create set init use partial call end endby", " ")

                })

                //  Give the RepositoryActor what it wants!
                context.actorSelection(s"../repo-${id}") ! DepGraphResult(dependencyGraph)

            } catch {
                case e:UnderstandException => {
                    println(s"${self.path.name} trouble: $inputFile")
                    supervisor ! FinalOutput("Failed")
                }
            }

        }

        /* ------------------------ *
          * Process Result Messages *
          * ----------------------- */


        case CleanRepoResult(0) => {
            // At confirmation that the result succeeded, kill the Actor in charge
            sender ! PoisonPill

//            context.actorOf(Props[DepGraphActor], name="depGraph") ! DepGraph(repo.id, repo.name)
        }


        case DepGraphResult(dependencyGraph) => {
            // At confirmation that the process was successful, kill the Actor in charge of the task
            sender ! PoisonPill
            context.actorOf(Props[ProcessActor], name="cleanUdb") ! CleanUDB(repo.id, repo.name)

            // Pass the results along to the Supervisor
            supervisor ! DepGraphResult(dependencyGraph)
        }

        case CleanUDBResult(0) => {
            // At confirmation that the process was successful, kill the Actor in charge of the task
            sender ! PoisonPill

            // now end yourself
            context.stop(self)
        }


        /* --------------------- *
         * In case Process fails *
         * --------------------- */
        case CloneResult(_, _) => {
            println("The repo failed to clone")
            //context.actorOf(Props[ProcessActor], name="cleanProcess") ! CleanRepo(repo.id, repo.name)
            context.stop(self)
        }

        case UDBResult(1,_,_) => {
            sender ! PoisonPill
            context.stop(self)
        }
    }

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


}
