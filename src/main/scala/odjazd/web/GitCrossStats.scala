package pl.relationsystems


import org.scalajs.dom._

import scala.scalajs.js
import scala.scalajs.js._
import scala.scalajs.js.annotation.JSExport

import D3._
import DC._
import CrossFilter._

trait GitLogEntry extends js.Any {
  val commit: String
  val author: String
  val date: String
  val message: String
}

trait GitChangeInformation {
  val insertions: String
  val deletions: String
  val path: String
}

case class GitLogForDisplay(commit: String, author: String, date: js.Date, year: Int, month: Int, dayOfMonth: Int, dayOfWeek: Int, insertions: Int, deletions: Int, changedPaths: Int)

@JSExport("GitCrossStats")
object GitCrossStats {

  var gitLog = js.Array[GitLogEntry]()

  var commitInformation = js.Dictionary[js.Array[GitChangeInformation]]()

  var processedGitLog: js.Array[GitLogForDisplay] = _

  @JSExport
  def main(args: Seq[String] = Seq()): Unit = {
    console.log("Starting GitCrossStats")

    d3.json("log.json", { (a: js.Any, b: js.Any) => {
      console.log("log.json loaded")
      gitLog = b.asInstanceOf[js.Array[GitLogEntry]]
      checkIfDataComplete
    }
    })

    d3.json("stats.json", { (a: js.Any, b: js.Any) => {
      console.log("stats.json loaded")
      commitInformation = b.asInstanceOf[js.Dictionary[js.Array[GitChangeInformation]]]
      checkIfDataComplete
    }
    })

  }

  def checkIfDataComplete: Unit = {
    if (gitLog.isEmpty || commitInformation.isEmpty) {
      console.log("Still waiting for all data")
    } else {
      console.log("Data complete")
      preprocessData()
    }
  }

  def preprocessData(): Unit = {

    processedGitLog = gitLog.map {
      entry =>
        val insertions = 0
        val deletions = 0
        val changedPaths = 0
        val author = cleanupAuthor(entry.author)
        val date = new Date(entry.date)

        val year = date.getFullYear()
        val month = date.getUTCMonth() + 1
        val dayOfMonth = date.getDate() + 1
        val dayOfWeek = date.getUTCDay()

        val result = new GitLogForDisplay(entry.commit, author, date, year, month, dayOfMonth, dayOfWeek, insertions, deletions, changedPaths)
        result
    }

    //FIXME: do it async, as it takes already very long time

    setupData()

  }

  val authorRegexp = """(.*?)<([^>]+)>""".r

  def cleanupAuthor(gitAuthor: String): String = {
    gitAuthor match {
      case authorRegexp(name, email, _*) =>
        email.substring(0, email.lastIndexOf('@'))
      case _ => gitAuthor
    }
  }

  def setupData(): Unit = {

    val ndx = crossfilter(processedGitLog)
    val all = ndx.groupAll()

    val byCommitDimmension = ndx.dimension({ (log: GitLogForDisplay) => log.commit})
    val byYearDimmension = ndx.dimension({ (log: GitLogForDisplay) => log.year})
    val byMonthDimmension = ndx.dimension({ (log: GitLogForDisplay) => log.month})
    val byAuthorDimmension = ndx.dimension({ (log: GitLogForDisplay) => log.author})
    val byDayOfMonthDimension = ndx.dimension({ (log: GitLogForDisplay) => log.dayOfMonth})
    val dayNames = js.Array("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")
    val byDayOfWeekDimension = ndx.dimension({ (log: GitLogForDisplay) => s"${log.dayOfWeek}.${dayNames(log.dayOfWeek)}"})
    val byDateDimension = ndx.dimension({ (log: GitLogForDisplay) => log.date.getTime()})


    var authorsPieChart = dc.pieChart("#authorGraph")
    authorsPieChart.dimension(byAuthorDimmension).group(byAuthorDimmension.group())
    authorsPieChart.width(400)
    authorsPieChart.height(400)

    var yearsPieChart = dc.pieChart("#yearGraph")
    yearsPieChart.dimension(byYearDimmension).group(byYearDimmension.group())

    var monthChart = dc.barChart("#monthGraph")
    monthChart.dimension(byMonthDimmension).group(byMonthDimmension.group())
    monthChart.x(d3.scale.linear().domain(js.Array(1, 12)))
    monthChart.elasticY(true)
    monthChart.width(500)

    var dayOfMonthChart = dc.barChart("#dayOfMonthGraph")
    dayOfMonthChart.dimension(byDayOfMonthDimension).group(byDayOfMonthDimension.group())
    dayOfMonthChart.x(d3.scale.linear().domain(js.Array(1, 32)))
    dayOfMonthChart.elasticY(true)
    dayOfMonthChart.width(700)

    var dayOfWeekChart = dc.pieChart("#dayOfWeekGraph")
    dayOfWeekChart.dimension(byDayOfWeekDimension).group(byDayOfWeekDimension.group())
    dayOfWeekChart.label((l: Any) => {
      l.asInstanceOf[js.Dynamic].data.key.asInstanceOf[String].drop(2)
    })

    var commitsTable = dc.dataTable("#commits")
    commitsTable.dimension(byDateDimension)
    commitsTable.sortBy({ (d: Any) => {
      d.asInstanceOf[GitLogForDisplay].date
    }
    })
    commitsTable.group((a: Any) => {
      a.asInstanceOf[GitLogForDisplay].date.toLocaleDateString()
    })
    commitsTable.size(100)
    commitsTable.columns(
      js.Array(
        (rowinfo: Any) => rowinfo.asInstanceOf[GitLogForDisplay].date.toLocaleTimeString(),
        (rowinfo: Any) => rowinfo.asInstanceOf[GitLogForDisplay].commit,
        (rowinfo: Any) => rowinfo.asInstanceOf[GitLogForDisplay].author
      )
    )

    commitsTable.order(d3.ascending _)

    dc.renderAll()

  }

}
