package com.example.planer.util

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.planer.database.entity.Task
import com.example.planer.ui.plan.TaskForPlanTimeString
import com.example.planer.ui.plan.TasksForPlan
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

class MySharePreferences(context: Context) {
    companion object
    {
        const val FILE_NAME: String = "SHARED_PREF_FILE"
        const val ALL_INFO: String = "ALL_INFO"
        const val NAME: String = "NAME"
        const val PEAK_BEGIN: String = "PEAK_BEGIN"
        const val PEAK_END: String = "PEAK_END"
        const val POMODORO_WORK: String = "POMODORO_WORK"
        const val POMODORO_BREAK: String = "POMODORO_BREAK"
        const val POMODORO_BIG_BREAK_F: String = "POMODORO_BIG_BREAK_F"
        const val POMODORO_BIG_BREAK: String = "POMODORO_BIG_BREAK"
        const val WAKEUP: String = "WAKEUP"
        const val SLEEP: String = "SLEEP"
        const val BREAKFAST: String = "BREAKFAST"
        const val LUNCH: String = "LUNCH"
        const val DINER: String = "DINER"
        const val BREAKFAST_END: String = "BREAKFAST_END"
        const val LUNCH_END: String = "LUNCH_END"
        const val DINER_END: String = "DINER_END"
        const val MONDAY_BEGIN: String = "MONDAY_BEGIN"
        const val MONDAY_WORK: String = "MONDAY_WORK"
        const val TUESDAY_BEGIN: String = "TUESDAY_BEGIN"
        const val TUESDAY_WORK: String = "TUESDAY_WORK"
        const val WEDNESDAY_BEGIN: String = "WEDNESDAY_BEGIN"
        const val WEDNESDAY_WORK: String = "WEDNESDAY_WORK"
        const val THURSDAY_BEGIN: String = "THURSDAY_BEGIN"
        const val THURSDAY_WORK: String = "THURSDAY_WORK"
        const val FRIDAY_BEGIN: String = "FRIDAY_BEGIN"
        const val FRIDAY_WORK: String = "FRIDAY_WORK"
        const val SATURDAY_BEGIN: String = "SATURDAY_BEGIN"
        const val SATURDAY_WORK: String = "SATURDAY_WORK"
        const val SUNDAY_BEGIN: String = "SUNDAY_BEGIN"
        const val SUNDAY_WORK: String = "SUNDAY_WORK"
        const val AUTO_PLAN: String = "AUTO_PLAN"
        const val THEME: String = "THEME"
        const val AUTO_FINISH_TASK: String = "AUTO_FINISH_TASK"
        const val PLAN_FOR_DAY: String = "PLAN_FOR_DAY"
        const val PLAN: String = "PLAN"
        const val FIRST_TASKS: String = "FIRST_TASKS"
        const val TODAY: String = "TODAY"
    }

    private val mySharedPreferences: SharedPreferences = context.getSharedPreferences(FILE_NAME, AppCompatActivity.MODE_PRIVATE)
    private val myEditor: SharedPreferences.Editor = mySharedPreferences.edit()

//    fun <T> setPlan(list: List<T>?) {
//        val gson = Gson()
//        val json = gson.toJson(list)
//        myEditor.putString(PLAN, json)
//        myEditor.commit()
//    }
//
//    fun getPlan():List<TasksForPlan>? {
//        var arrayItems: List<TasksForPlan>? = listOf()
//        val serializedObject: String? = mySharedPreferences.getString(PLAN, null)
//        if (serializedObject != null) {
//            val gson = Gson()
//            val type: Type = object : TypeToken<List<TasksForPlan?>?>() {}.type
//            arrayItems = gson.fromJson<List<TasksForPlan>>(serializedObject, type)
//        }
//        return arrayItems
//    }

    fun setToday(date: String){
        myEditor.putString(TODAY, date)
        myEditor.apply()
    }

    fun getToday(): String? {
        return mySharedPreferences.getString(TODAY, "2020-10-10")
    }

    fun setFirstTasks(list: List<TasksForPlan>){
        val gson = Gson()
        val json = gson.toJson(list)
        myEditor.putString(FIRST_TASKS, json)
        myEditor.commit()
    }

    fun getFirstTasks():List<TasksForPlan>?{
        val gson = Gson()
        val json = mySharedPreferences.getString(FIRST_TASKS, null)
        val type = object : TypeToken<List<TasksForPlan>>(){}.type
        return gson.fromJson(json, type)
    }

    fun setPlan(list: MutableList<TasksForPlan>?){
        val gson = Gson()
        val tasks: MutableList<TaskForPlanTimeString>? = mutableListOf()
        list?.forEach {
            tasks?.add(TaskForPlanTimeString(it.begin.toString(), it.end.toString(), it.time, it.task))
        }
        val json = gson.toJson(tasks)
        myEditor.putString(PLAN, json)
        myEditor.commit()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getPlan():MutableList<TasksForPlan>?{
        val gson = Gson()
        val list: MutableList<TasksForPlan>? = mutableListOf()
        val json = mySharedPreferences.getString(PLAN, null)
        val type = object : TypeToken<List<TaskForPlanTimeString>>(){}.type
        val tasks: MutableList<TaskForPlanTimeString>? = gson.fromJson(json, type)
        tasks?.forEach {
            list?.add(TasksForPlan(LocalTime.parse(it.begin, DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)),
                LocalTime.parse(it.end, DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)), it.time, it.task))
        }
        return list
    }

    fun setPlanForDay(flag: Boolean){
        myEditor.putBoolean(PLAN_FOR_DAY, flag)
        myEditor.apply()
    }

    fun getPlanForDay(): Boolean {
        return mySharedPreferences.getBoolean(PLAN_FOR_DAY, false)
    }

    fun setAllInfo(flag: Boolean){
        myEditor.putBoolean(ALL_INFO, flag)
        myEditor.apply()
    }

    fun getAllInfo(): Boolean {
        return mySharedPreferences.getBoolean(ALL_INFO, false)
    }

    fun setName(name: String){
        myEditor.putString(NAME, name)
        myEditor.apply()
    }

    fun getName(): String?{
        return mySharedPreferences.getString(NAME, "")
    }

    fun setPeakBegin(time: String){
        myEditor.putString(PEAK_BEGIN, time)
        myEditor.apply()
    }

    fun getPeakBegin(): String? {
        return mySharedPreferences.getString(PEAK_BEGIN, "00:00")
    }

    fun setPeakEnd(time: String){
        myEditor.putString(PEAK_END, time)
        myEditor.apply()
    }

    fun getPeakEnd(): String? {
        return mySharedPreferences.getString(PEAK_END, "00:00")
    }

    fun setPomodoroWork(time: Int){
        myEditor.putInt(POMODORO_WORK, time)
        myEditor.apply()
    }

    fun getPomodoroWork(): Int {
        return mySharedPreferences.getInt(POMODORO_WORK, 25)
    }

    fun setPomodoroBreak(time: Int){
        myEditor.putInt(POMODORO_BREAK, time)
        myEditor.apply()
    }

    fun getPomodoroBreak(): Int {
        return mySharedPreferences.getInt(POMODORO_BREAK, 5)
    }

    fun setPomodoroBigBreakF(flag: Boolean){
        myEditor.putBoolean(POMODORO_BIG_BREAK_F, flag)
        myEditor.apply()
    }

    fun getPomodoroBigBreakF(): Boolean {
        return mySharedPreferences.getBoolean(POMODORO_BIG_BREAK_F, true)
    }

    fun setPomodoroBigBreak(time: Int){
        myEditor.putInt(POMODORO_BIG_BREAK, time)
        myEditor.apply()
    }

    fun getPomodoroBigBreak(): Int {
        return mySharedPreferences.getInt(POMODORO_BIG_BREAK, 15)
    }

    fun setWakeup(time: String){
        myEditor.putString(WAKEUP, time)
        myEditor.apply()
    }

    fun getWakeup(): String? {
        return mySharedPreferences.getString(WAKEUP, "00:00")
    }

    fun setSleep(time: String){
        myEditor.putString(SLEEP, time)
        myEditor.apply()
    }

    fun getSleep(): String? {
        return mySharedPreferences.getString(SLEEP, "00:00")
    }

    fun setBreakfast(time: String){
        myEditor.putString(BREAKFAST, time)
        myEditor.apply()
    }

    fun getBreakfast(): String? {
        return mySharedPreferences.getString(BREAKFAST, "00:00")
    }

    fun setBreakfastEnd(time: String){
        myEditor.putString(BREAKFAST_END, time)
        myEditor.apply()
    }

    fun getBreakfastEnd(): String? {
        return mySharedPreferences.getString(BREAKFAST_END, "00:00")
    }

    fun setLunch(time: String){
        myEditor.putString(LUNCH, time)
        myEditor.apply()
    }

    fun getLunch(): String? {
        return mySharedPreferences.getString(LUNCH, "00:00")
    }

    fun setLunchEnd(time: String){
        myEditor.putString(LUNCH_END, time)
        myEditor.apply()
    }

    fun getLunchEnd(): String? {
        return mySharedPreferences.getString(LUNCH_END, "00:00")
    }

    fun setDiner(time: String){
        myEditor.putString(DINER, time)
        myEditor.apply()
    }

    fun getDiner(): String? {
        return mySharedPreferences.getString(DINER, "00:00")
    }

    fun setDinerEnd(time: String){
        myEditor.putString(DINER_END, time)
        myEditor.apply()
    }

    fun getDinerEnd(): String? {
        return mySharedPreferences.getString(DINER_END, "00:00")
    }

    fun setMondayBegin(time: String){
        myEditor.putString(MONDAY_BEGIN, time)
        myEditor.apply()
    }

    fun getMondayBegin(): String? {
        return mySharedPreferences.getString(MONDAY_BEGIN, "00:00")
    }

    fun setTuesdayBegin(time: String){
        myEditor.putString(TUESDAY_BEGIN, time)
        myEditor.apply()
    }

    fun getTuesdayBegin(): String? {
        return mySharedPreferences.getString(TUESDAY_BEGIN, "00:00")
    }

    fun setWednesdayBegin(time: String){
        myEditor.putString(WEDNESDAY_BEGIN, time)
        myEditor.apply()
    }

    fun getWednesdayBegin(): String? {
        return mySharedPreferences.getString(WEDNESDAY_BEGIN, "00:00")
    }

    fun setThursdayBegin(time: String){
        myEditor.putString(THURSDAY_BEGIN, time)
        myEditor.apply()
    }

    fun getThursdayBegin(): String? {
        return mySharedPreferences.getString(THURSDAY_BEGIN, "00:00")
    }

    fun setFridayBegin(time: String){
        myEditor.putString(FRIDAY_BEGIN, time)
        myEditor.apply()
    }

    fun getFridayBegin(): String? {
        return mySharedPreferences.getString(FRIDAY_BEGIN, "00:00")
    }

    fun setSaturdayBegin(time: String){
        myEditor.putString(SATURDAY_BEGIN, time)
        myEditor.apply()
    }

    fun getSaturdayBegin(): String? {
        return mySharedPreferences.getString(SATURDAY_BEGIN, "00:00")
    }

    fun setSundayBegin(time: String){
        myEditor.putString(SUNDAY_BEGIN, time)
        myEditor.apply()
    }

    fun getSundayBegin(): String? {
        return mySharedPreferences.getString(SUNDAY_BEGIN, "00:00")
    }

    ////
    fun setMondayWork(time: String){
        myEditor.putString(MONDAY_WORK, time)
        myEditor.apply()
    }

    fun getMondayWork(): String? {
        return mySharedPreferences.getString(MONDAY_WORK, "00:00")
    }

    fun setTuesdayWork(time: String){
        myEditor.putString(TUESDAY_WORK, time)
        myEditor.apply()
    }

    fun getTuesdayWork(): String? {
        return mySharedPreferences.getString(TUESDAY_WORK, "00:00")
    }

    fun setWednesdayWork(time: String){
        myEditor.putString(WEDNESDAY_WORK, time)
        myEditor.apply()
    }

    fun getWednesdayWork(): String? {
        return mySharedPreferences.getString(WEDNESDAY_WORK, "00:00")
    }

    fun setThursdayWork(time: String){
        myEditor.putString(THURSDAY_WORK, time)
        myEditor.apply()
    }

    fun getThursdayWork(): String? {
        return mySharedPreferences.getString(THURSDAY_WORK, "00:00")
    }

    fun setFridayWork(time: String){
        myEditor.putString(FRIDAY_WORK, time)
        myEditor.apply()
    }

    fun getFridayWork(): String? {
        return mySharedPreferences.getString(FRIDAY_WORK, "00:00")
    }

    fun setSaturdayWork(time: String){
        myEditor.putString(SATURDAY_WORK, time)
        myEditor.apply()
    }

    fun getSaturdayWork(): String? {
        return mySharedPreferences.getString(SATURDAY_WORK, "00:00")
    }

    fun setSundayWork(time: String){
        myEditor.putString(SUNDAY_WORK, time)
        myEditor.apply()
    }

    fun getSundayWork(): String? {
        return mySharedPreferences.getString(SUNDAY_WORK, "00:00")
    }

    fun setAutoPlan(flag: Boolean){
        myEditor.putBoolean(AUTO_PLAN, flag)
        myEditor.apply()
    }

    fun getAutoPlan(): Boolean {
        return mySharedPreferences.getBoolean(AUTO_PLAN, true)
    }

    fun setTheme(flag: Boolean){
        myEditor.putBoolean(THEME, flag)
        myEditor.apply()
    }

    fun getTheme(): Boolean {
        return mySharedPreferences.getBoolean(THEME, true)
    }

    fun setAutoFinishTask(flag: Boolean){
        myEditor.putBoolean(AUTO_FINISH_TASK, flag)
        myEditor.apply()
    }

    fun getAutoFinishTask(): Boolean {
        return mySharedPreferences.getBoolean(AUTO_FINISH_TASK, false)
    }
}