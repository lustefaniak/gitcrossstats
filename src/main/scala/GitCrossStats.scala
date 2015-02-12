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

trait CommitExtended extends Commit {
  var simplifiedAuthor: String = js.native
  var date: js.Date = js.native
  var year: Int = js.native
  var month: Int = js.native
  var dayOfMonth: Int = js.native
  var dayOfWeek: Int = js.native
  var hour: Int = js.native
}


@JSExport("GitCrossStats")
object GitCrossStats {

  var gitLog = js.Array[Commit]()

  var processedGitLog: js.Array[CommitExtended] = _


  @JSExport
  def main(baseUrl: String = "http://192.168.59.103/repos/gitcrossstats/git/commits/"): Unit = {
    console.log("Starting GitCrossStats")

    d3.json(baseUrl, { (a: js.Any, b: js.Any) => {
      console.log("commits loaded")
      gitLog = b.asInstanceOf[js.Array[Commit]]
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

    gitLog.foreach {
      entry =>
        val author = cleanupAuthor(entry.author)
        val extended = entry.asInstanceOf[CommitExtended]
        extended.simplifiedAuthor = author
        val date = new Date(entry.author.date)
        extended.date = date
        extended.year = date.getFullYear()
        extended.month = date.getUTCMonth() + 1
        extended.dayOfMonth = date.getDate() + 1
        extended.dayOfWeek = date.getUTCDay()
        extended.hour = date.getUTCHours() + 1
    }

    processedGitLog = gitLog.asInstanceOf[js.Array[CommitExtended]]

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

    val byCommitDimmension = ndx.dimension({ (log: CommitExtended) => log.sha})
    val byYearDimmension = ndx.dimension({ (log: CommitExtended) => log.year})
    val byMonthDimmension = ndx.dimension({ (log: CommitExtended) => log.month})
    val byAuthorDimmension = ndx.dimension({ (log: CommitExtended) => log.simplifiedAuthor})
    val byDayOfMonthDimension = ndx.dimension({ (log: CommitExtended) => log.dayOfMonth})

    val byDayOfWeekDimension = ndx.dimension({ (log: CommitExtended) => s"${log.dayOfWeek}.${I18n.dayNames(log.dayOfWeek)}"})
    val byHourDimension = ndx.dimension({ (log: CommitExtended) => log.date.getUTCHours() + 1})
    val byDateDimension = ndx.dimension({ (log: CommitExtended) => log.date.getTime()})


    var authorsPieChart = dc.pieChart("#authorGraph")
      .dimension(byAuthorDimmension).group(byAuthorDimmension.group())
      .width(400)
      .height(400)
      .radius(190)
      .innerRadius(120)

    var yearsPieChart = dc.pieChart("#yearGraph")
      .dimension(byYearDimmension).group(byYearDimmension.group())
      .width(180)
      .height(180)
      .radius(80)
      .innerRadius(30)

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
      .sortBy((d: Any) => d.asInstanceOf[CommitExtended].date.getTime())
      .group((a: Any) => a.asInstanceOf[CommitExtended].date.toLocaleDateString())
      .size(100)
      .columns(
        js.Array(
          (rowinfo: Any) => rowinfo.asInstanceOf[CommitExtended].date.toLocaleTimeString(),
          (rowinfo: Any) => rowinfo.asInstanceOf[CommitExtended].sha.take(8),
          (rowinfo: Any) => rowinfo.asInstanceOf[CommitExtended].simplifiedAuthor,
          (rowinfo: Any) => rowinfo.asInstanceOf[CommitExtended].message
        )
      )

    dc.renderAll()

  }

}
