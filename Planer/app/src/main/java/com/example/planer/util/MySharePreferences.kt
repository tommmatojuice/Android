package com.example.planer.util

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity

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
        const val POMODORO_BIG_BREAK: String = "POMODORO_BIG_BREAK"
        const val WAKEUP: String = "WAKEUP"
        const val SLEEP: String = "SLEEP"
        const val BREAKFAST: String = "BREAKFAST"
        const val LUNCH: String = "LUNCH"
        const val DINER: String = "DINER"
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
    }

    private val mySharedPreferences: SharedPreferences = context.getSharedPreferences(FILE_NAME, AppCompatActivity.MODE_PRIVATE)
    private val myEditor: SharedPreferences.Editor = mySharedPreferences.edit()

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

    fun getPomodoroWork(): Int? {
        return mySharedPreferences.getInt(POMODORO_WORK, 25)
    }

    fun setPomodoroBreak(time: Int){
        myEditor.putInt(POMODORO_BREAK, time)
        myEditor.apply()
    }

    fun getPomodoroBreak(): Int? {
        return mySharedPreferences.getInt(POMODORO_BREAK, 5)
    }

    fun setPomodoroBigBreak(flag: Boolean){
        myEditor.putBoolean(POMODORO_BIG_BREAK, flag)
        myEditor.apply()
    }

    fun getPomodoroBigBreak(): Boolean {
        return mySharedPreferences.getBoolean(POMODORO_BIG_BREAK, true)
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

    fun setLunch(time: String){
        myEditor.putString(LUNCH, time)
        myEditor.apply()
    }

    fun getLunch(): String? {
        return mySharedPreferences.getString(LUNCH, "00:00")
    }

    fun setDiner(time: String){
        myEditor.putString(DINER, time)
        myEditor.apply()
    }

    fun getDiner(): String? {
        return mySharedPreferences.getString(DINER, "00:00")
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