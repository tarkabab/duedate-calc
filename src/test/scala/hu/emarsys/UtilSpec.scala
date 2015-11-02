package hu.emarsys

import org.scalatest.FunSpec

import hu.emarsys.Util.calculateDueDate

class UtilSpec extends FunSpec {

  describe("The CalculateDueDate method") {
    it("should take the submit date and turnaround time as an input") {
      val submitDate = "2015-10-28T16:30:00.000+02:00"
      val turnaroundTime = 16
      calculateDueDate(submitDate, turnaroundTime)
    }
    it("should return the date and time when the issue is to be resolved") {
      val submitDate = "2015-10-28T16:30:00.000+02:00"
      val turnaroundTime = 16
      val dueDate = "2015-10-30T16:30:00.000+02:00"
      assert(calculateDueDate(submitDate, turnaroundTime) === dueDate)
    }
    it("should handle workinghour overflow correctly") {
      val submitDate = "2015-10-28T16:30:00.000+02:00"
      val turnaroundTime = 1
      val dueDate = "2015-10-29T09:30:00.000+02:00"
      assert(calculateDueDate(submitDate, turnaroundTime) === dueDate)
    }
    it("should handle workday overflow correctly") {
      val submitDate = "2015-10-28T16:30:00.000+02:00"
      val turnaroundTime = 17
      val dueDate = "2015-11-02T09:30:00.000+02:00"
      assert(calculateDueDate(submitDate, turnaroundTime) === dueDate)
    }
  }
}
