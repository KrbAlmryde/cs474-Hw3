package com.hw3.Actors

import java.io.File

import akka.actor.{Actor, PoisonPill, Props}
import com.hw3.Patterns.Messages.{FinalOutput, GenPatch, PatchResult}
import com.hw3.Utils._

import scala.io.Source
/**
  * Created by krbalmryde on 11/12/16.
  */
class PatchActor extends Actor {

    var supervisor = self

    def receive: Receive = {


        case GenPatch(id, name) => {
            supervisor = sender
            val prActor = context.actorOf(Props[ProcessActor], name="patchMaker")
            prActor ! GenPatch(id, name)
        }


        // If weve made it this far it means weve managed to create some kind of patch file
        case PatchResult(0, id) => {

            val patchFile = s"$resourceDir/$id/1"

            if ( new File(patchFile).exists ) {
                val results = processPatch( Source.fromFile(patchFile).getLines )
                if (results.nonEmpty) {
                    val N = results.size
                    val finalReport = new StringBuilder
                        finalReport.append("\n**--------------------------------------------------------------**\n")
                        finalReport.append(s"\tFinal Report for Repo:$id\n")
                        finalReport.append(s"\t\t$N files (of substance) were changed\n")
                    println(s"results are: $results")
                    val top = results.toList.sortWith(_._2 > _._2)
                    val bottom = results.toList.sortWith(_._1 > _._1)

                    val W = top.head._3
                    val X = top.head._2
                    val Y = bottom.head._3
                    val Z = bottom.head._1

                    finalReport.append(s"\t\tSuggest developer retest Module $W due to $X additive changes made\n")
                    finalReport.append(s"\t\tSuggest developer retest Module $Y due to $Z subtractive changes made\n")
                    supervisor ! FinalOutput(finalReport.toString)

                } else {
                    val finalReport = new StringBuilder
                    finalReport.append("\n**--------------------------------------------------------------**\n")
                    finalReport.append(s"\tFinal Report for Repo:$id\n")
                    finalReport.append(s"\t\t0 files (of substance) were changed\n")

                    finalReport.append("\t\tSorry, not enough activity to suggest Action\n")
                    finalReport.append("\t\tMaybe the next commit? :-)\n")
                    supervisor ! FinalOutput(finalReport.toString)
                }
            } else
                println("File not Found! Bummer...")
        }

        case _ => sender ! PoisonPill
    }
    val languages = List("ada", "assembly", "c", "c++", "c#", "fortran", "java", "jovial", "delphi", "pascal", "pl", "m", "vhdl", "cobol", "php", "html", "css", "javascript", "python")

    def processPatch(contents: Iterator[String]): List[(Int, Int, String)] = {

        val data = {
            contents.filter(line => {
                val pattern = raw"""\d+\t\d+\t*(\w|/)+.(frag|vert|scala|java|cpp|c|cs|cxx|py|pl|m|html|js|php|css|f\d+|xml|pas|pp|cbl|CBL|cob|vhdl|dpk)""".r
                pattern.findFirstIn(line).isDefined
            }).map(line => {
                line.split("\t") match {
                    case xyz:Array[String] => {
                        val tupple = (xyz(0).toInt, xyz(1).toInt, xyz(2))
                        tupple
                    } // We want them to be tuples
                }
            }).toList
        }
        data

    }
}
