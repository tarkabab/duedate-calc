package hu.emarsys

import org.joda.time.{Period, DateTime}
import org.joda.time.DateTimeConstants._

object Util {
  def calculateDueDate(submitDate: String, turnaroundTime: Int): String = {

    val start: DateTime = DateTime.parse(submitDate)

    val workHourStart = 9
    require(workHourStart >= 0 && workHourStart < 24)
    val workHourEnd = 17
    require(workHourEnd >= 0 && workHourEnd < 24 && workHourEnd > workHourStart)
    val workHoursPerDay = workHourEnd - workHourStart

    val completeWorkingDays = turnaroundTime / workHoursPerDay
    val remainingWorkHours = turnaroundTime % workHoursPerDay

    def addCompleteWorkingDays(start: DateTime, days: Int) = {
      // todo: implement this
      start
    }
    def addRemainingWorkHours(start: DateTime, hours: Int): DateTime = {
      require(hours < workHoursPerDay)
      val endOfWork = start.withTime(workHourEnd,0,0,0)
      val end = start.plusHours(hours)
      if(end.isAfter(endOfWork)) {
        val excess = new Period(endOfWork, end)
        val startOfNextDay = start.withTime(workHourStart,0,0,0).plusDays(1)
        nextWorkingDay(startOfNextDay.plus(excess))
      }
      else {
        end
      }
    }
    def nextWorkingDay(day: DateTime): DateTime = day.getDayOfWeek match {
      case SATURDAY => day.plusDays(2)
      case SUNDAY => day.plusDays(1)
      case _ => day
    }


    val startPlusCompleteWorkingDays: DateTime = addCompleteWorkingDays(start, completeWorkingDays)
    val dueDate = addRemainingWorkHours(startPlusCompleteWorkingDays, remainingWorkHours)

    dueDate.toString
  }
}
