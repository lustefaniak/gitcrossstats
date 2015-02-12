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


    val totalWidthAvailable = 1100


    val monthChart = dc.barChart("#monthGraph")
      .width(totalWidthAvailable).height(120)
      .dimension(byMonthDimmension).group(byMonthDimmension.group())
      .x(d3.scale.linear().domain(js.Array(1, 12)))
      .y(d3.scale.log())
      .elasticY(true)
      .xAxisLabel(I18n.Month)
      .turnOnControls(true)


    monthChart.xAxis().ticks(10)


    val dayOfMonthChart = dc.barChart("#dayOfMonthGraph")
      .width(totalWidthAvailable).height(120)
      .dimension(byDayOfMonthDimension).group(byDayOfMonthDimension.group())
      .x(d3.scale.linear().domain(js.Array(1, 32)))
      .elasticY(true)
      .y(d3.scale.log())
      .xAxisLabel(I18n.DayOfMonth)

    var hourChart = dc.barChart("#hourGraph")
      .width(totalWidthAvailable).height(120)
      .dimension(byHourDimension).group(byHourDimension.group())
      .x(d3.scale.linear().domain(js.Array(0, 24)))
      .elasticY(true)
      .xAxisLabel(I18n.HourOfDay)


    val authorsSize = totalWidthAvailable * 2 / 3
    val radius = (authorsSize / 2) * 0.95

    val authorsPieChart = dc.pieChart("#authorGraph")
      .width(authorsSize).height(authorsSize)
      .dimension(byAuthorDimmension).group(byAuthorDimmension.group())
      .radius(radius)
      .innerRadius(radius * 0.8)


    val dayOfWeekChart = dc.rowChart("#dayOfWeekGraph")
      .width(totalWidthAvailable - authorsSize)
      .height(160)
      .margins(js.Dynamic.literal(top = 20, left = 10, right = 10, bottom = 20).asInstanceOf[IMarginObj])
      .dimension(byDayOfWeekDimension)
      .group(byDayOfWeekDimension.group())
      .label((l: Any) => {
      l.asInstanceOf[js.Dynamic].key.asInstanceOf[String].drop(2)
    })
      .elasticX(true).xAxis().ticks(4)

    val yearsSize = totalWidthAvailable - authorsSize
    val yearsPieChart = dc.pieChart("#yearGraph")
      .width(yearsSize).height(yearsSize)
      .dimension(byYearDimmension).group(byYearDimmension.group())
      .radius(yearsSize / 2 * 0.95)
      .innerRadius(yearsSize / 2 * 0.95 * 0.2)


    val commitsTable = dc.dataTable("#commits")
      .dimension(byDateDimension)
      .group((a: Any) => "<b>" + a.asInstanceOf[CommitExtended].date.toLocaleDateString() + "</b>")
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
