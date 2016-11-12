package com.hw3.Patterns

import akka.actor.Status.Success
import akka.http.scaladsl.model.HttpResponse
import com.hw3.Patterns.Messages.CloneRepo
import org.jgrapht.graph.{DefaultEdge, SimpleDirectedGraph}
import org.json4s.JsonAST.JValue

import scala.concurrent.Future

/**
  * Created by krbalmryde on 11/5/16.
  * The following are a series of Messages which will be passed
  * back and forth between Actors
  */
// Greetings
object Messages {

    case object Empty
    case object Hello
    case object WakeUp
    case object SayHello

    // Languages
    case object Java
    case object Python

    /**  REQUESTS **/
    // Github API 'Request' patterns
    case class GitSearch(lang:String)  // Searches for Repositories containing the given language
    case class GitCommit(fullName:String)  // Requests Content of a given Repository and Owner (mostly to get the SHA)
    case class GitTree(fullName:String)
    case class GitIssues(fullName:String)

    /**  RESPONSE **/
    // Content Replies containing results of requests
    case class JsonResult(json:JValue)
    case class DepGraphResult(graph:SimpleDirectedGraph[String, DefaultEdge])
    case class FinalOutput(result:String)  // Should be used

    /**  PROCESS RESULTS **/
    // For use with Processes
    case class CloneResult(success:Int)
    case class UDBResult(success:Int, id:String, name:String)
    case class CommitResult(success:Int)
    case class CleanRepoResult(success: Int)
    case class CleanUDBResult(success: Int)
    case class PatchResult(success: Int)

    /** OPERATIONS **/
    // Simple instruction messages defining operations
    case class CloneRepo(id:String, name:String, url:String)  // In order to clone a Repo, send this request message
    case class CleanRepo(id:String) // When I want to remove the repository
    case class CleanUDB(id:String, name:String)  // When I want to remoce the .udb file
    case class GenUDB(id:String, name:String, language:String) // When Its time to generate the .UDB file, send this message
    case class GenPatch(id:String, name:String)
    case class DepGraph(id:String, name:String) // In order to make the dependency graph we want this
    case class RepoDetails(id:String, name:String, url:String, lang:String) // Simple example


    /**  DEBUGGING **/
    case class Done(m:String)
    case class MyMessage( m:String )
    case class Language(name:String)
    case class Greeting(from:String)
    case class Received(from:String)
    // Placeholder
    case class Foo(id:String, name:String, language:String, foo:String)

    /**  MISC **/
    case class SHA(sha:String)
    case class Single(x:JValue)
    case class Double(x:JValue, y:JValue)
    case class Composed(x:JValue, y:JValue, z:JValue)




}
