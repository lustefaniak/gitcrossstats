import org.scalajs.dom._
import pl.relationsystems.CrossFilter._
import pl.relationsystems.D3._
import pl.relationsystems.DC.{IMarginObj, dc}

import scala.scalajs.js
import scala.scalajs.js._
import scala.scalajs.js.annotation.JSExport

trait Author extends js.Any {
  val date: String = js.native
  val name: String = js.native
  val email: String = js.native
}

trait Ref extends js.Any {
  var sha: String = js.native
  var url: String = js.native
}

trait Commit extends js.Any {
  var author: Author = js.native
  var committer: Author = js.native
  var sha: String = js.native
  var message: String = js.native
  var parents: js.Array[Ref] = js.native
  var tree: Ref = js.native
  var url: String = js.native
}

object I18n {
  val Month: String = "Month"

  val HourOfDay: String = "Hour of day"

  val DayOfMonth: String = "Day of month"

  val NumberOfCommits: String = "Number of commits"
  val dayNames = js.Array("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")


}


case class CommitEntry(commit: String, author: String, date: js.Date, year: Int, month: Int, dayOfMonth: Int, dayOfWeek: Int, hour: Int)


@JSExport("GitCrossStats")
object GitCrossStats {

  var gitLog = js.Array[Commit]()

  var processedGitLog: js.Array[CommitEntry] = _


  @JSExport
  def main(baseUrl: String = "http://192.168.59.103/repos/gitcrossstats/git/commits/"): Unit = {
    console.log("Starting GitCrossStats")

    d3.json(baseUrl, { (a: js.Any, b: js.Any) => {
      console.log("commits loaded")
      gitLog = b.asInstanceOf[js.Array[Commit]]

      console.log(b)

      checkIfDataComplete
    }
    })

  }

  def checkIfDataComplete: Unit = {
    if (gitLog.isEmpty) {
      console.log("Still waiting for all data")
    } else {
      console.log("Data complete")
      preprocessData()
    }
  }

  def preprocessData(): Unit = {

    processedGitLog = gitLog.map {
      entry =>
        val author = cleanupAuthor(entry.author)
        val date = new Date(entry.author.date)

        val year = date.getFullYear()
        val month = date.getUTCMonth() + 1
        val dayOfMonth = date.getDate() + 1
        val dayOfWeek = date.getUTCDay()

        val hour = date.getUTCHours() + 1

        val result = new CommitEntry(entry.sha, author, date, year, month, dayOfMonth, dayOfWeek, hour)
        result
    }

    //FIXME: do it async, as it takes already very long time

    setupData()

  }

  val authorRegexp = """(.*?)<([^>]+)>""".r

  def cleanupAuthor(author: Author): String = {
    author.email.substring(0, author.email.lastIndexOf('@'))
  }

  def setupData(): Unit = {

    val ndx = crossfilter(processedGitLog)
    val all = ndx.groupAll()

    val byCommitDimmension = ndx.dimension({ (log: CommitEntry) => log.commit})
    val byYearDimmension = ndx.dimension({ (log: CommitEntry) => log.year})
    val byMonthDimmension = ndx.dimension({ (log: CommitEntry) => log.month})
    val byAuthorDimmension = ndx.dimension({ (log: CommitEntry) => log.author})
    val byDayOfMonthDimension = ndx.dimension({ (log: CommitEntry) => log.dayOfMonth})

    val byDayOfWeekDimension = ndx.dimension({ (log: CommitEntry) => s"${log.dayOfWeek}.${I18n.dayNames(log.dayOfWeek)}"})
    val byHourDimension = ndx.dimension({ (log: CommitEntry) => log.date.getUTCHours() + 1})
    val byDateDimension = ndx.dimension({ (log: CommitEntry) => log.date.getTime()})


    var authorsPieChart = dc.pieChart("#authorGraph")
    .dimension(byAuthorDimmension).group(byAuthorDimmension.group())
    .width(400)
    .height(400)

    var yearsPieChart = dc.pieChart("#yearGraph")
    .dimension(byYearDimmension).group(byYearDimmension.group())

    var monthChart = dc.barChart("#monthGraph")
    .dimension(byMonthDimmension).group(byMonthDimmension.group())
    .x(d3.scale.linear().domain(js.Array(1, 12)))
    .elasticY(true)
    .width(500)
    .yAxisLabel(I18n.NumberOfCommits)
    .xAxisLabel(I18n.Month)

    var hourChart = dc.barChart("#hourGraph")
    .dimension(byHourDimension).group(byHourDimension.group())
    .x(d3.scale.linear().domain(js.Array(0, 24)))
    .elasticY(true)
    .width(500)
    .yAxisLabel(I18n.NumberOfCommits)
    .xAxisLabel(I18n.HourOfDay)


    var dayOfMonthChart = dc.barChart("#dayOfMonthGraph")
    .dimension(byDayOfMonthDimension).group(byDayOfMonthDimension.group())
    .x(d3.scale.linear().domain(js.Array(1, 32)))
    .elasticY(true)
    .width(700)
    .yAxisLabel(I18n.NumberOfCommits)
    .xAxisLabel(I18n.DayOfMonth)

    var dayOfWeekChart = dc.rowChart("#dayOfWeekGraph")
      .width(180)
      .height(180)
      .margins(js.Dynamic.literal(top = 20, left = 10, right = 10, bottom = 20).asInstanceOf[IMarginObj])
      .dimension(byDayOfWeekDimension)
      .group(byDayOfWeekDimension.group())
      .label((l: Any) => {
        l.asInstanceOf[js.Dynamic].key.asInstanceOf[String].drop(2)
      })
      .elasticX(true).xAxis().ticks(4)

    var commitsTable = dc.dataTable("#commits")
    .dimension(byDateDimension)
    .sortBy((d: Any) => d.asInstanceOf[CommitEntry].date.getTime())
    .group((a: Any) => {
      a.asInstanceOf[CommitEntry].date.toLocaleDateString()
    })
    .size(100)
    .columns(
      js.Array(
        (rowinfo: Any) => rowinfo.asInstanceOf[CommitEntry].date.toLocaleTimeString(),
        (rowinfo: Any) => rowinfo.asInstanceOf[CommitEntry].commit,
        (rowinfo: Any) => rowinfo.asInstanceOf[CommitEntry].author
      )
    )

    dc.renderAll()

  }

}
