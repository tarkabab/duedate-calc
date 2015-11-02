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

    val wholeDays = turnaroundTime / workHoursPerDay
    val remainingHours = turnaroundTime % workHoursPerDay

    def addWholeDays(start: DateTime, days: Int) = {
      val workingDaysPerWeek = 5
      val daysPerWeek = 7

      val startDay = start.getDayOfWeek
      val weeks = days / workingDaysPerWeek
      val remainingDays = days % workingDaysPerWeek
      val weekOverflow = if((startDay + remainingDays) < SATURDAY) 0 else 2
      val daysWithWeekends = weeks * daysPerWeek + remainingDays + weekOverflow

      start.plusDays(daysWithWeekends)
    }
    def addRemainingHours(start: DateTime, hours: Int): DateTime = {
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

    val startPlusWholeDays: DateTime = addWholeDays(start, wholeDays)
    val dueDate = addRemainingHours(startPlusWholeDays, remainingHours)

    dueDate.toString
  }
}
