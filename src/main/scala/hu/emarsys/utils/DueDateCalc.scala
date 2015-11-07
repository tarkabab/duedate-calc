package hu.emarsys.utils

import org.joda.time.DateTimeConstants._
import org.joda.time.{DateTime, Period}

object DueDateCalc {

  val workHourStart = 9
  require(workHourStart >= 0 && workHourStart < 24)
  val workHourEnd = 17
  require(workHourEnd >= 0 && workHourEnd < 24 && workHourEnd > workHourStart)
  val workHoursPerDay = workHourEnd - workHourStart

  private[utils] def parseDate(date: String): DateTime = {
    DateTime.parse(date)
  }

  private[utils] def formatDate(date: DateTime): String = {
    date.toString
  }

  private[utils] def nextWorkingDay(day: DateTime): DateTime = day.getDayOfWeek match {
    case FRIDAY => day.plusDays(3)
    case SATURDAY => day.plusDays(2)
    case _ => day.plusDays(1)
  }

  private[utils] def addWorkingDays(start: DateTime, days: Int) = {
    val daysPerWeek = 7
    val workingDaysPerWeek = 5
    val weekendLength = daysPerWeek - workingDaysPerWeek

    val startDay = start.getDayOfWeek
    val weeks = days / workingDaysPerWeek
    val remainingDays = days % workingDaysPerWeek
    val weekOverflow = if((startDay + remainingDays) < SATURDAY) 0 else weekendLength

    val daysWithWeekends = weeks * daysPerWeek + remainingDays + weekOverflow

    start.plusDays(daysWithWeekends)
  }

  private[utils] def addWorkingHours(start: DateTime, hours: Int): DateTime = {
    require(hours < workHoursPerDay)
    val endOfWork = start.withTime(workHourEnd,0,0,0)
    val end = start.plusHours(hours)
    if(end.isAfter(endOfWork)) {
      val excessTime = new Period(endOfWork, end)
      nextWorkingDay(start.withTime(workHourStart,0,0,0).plus(excessTime))
    }
    else {
      end
    }
  }

  def calculateDueDate(submitDate: String, turnaroundTime: Int): String = {
    @throws[IllegalArgumentException]
    val start = parseDate(submitDate)

    val days = turnaroundTime / workHoursPerDay
    val hours = turnaroundTime % workHoursPerDay

    val startPlusWorkingDays = addWorkingDays(start, days)
    val dueDate = addWorkingHours(startPlusWorkingDays, hours)

    formatDate(dueDate)
  }
}
