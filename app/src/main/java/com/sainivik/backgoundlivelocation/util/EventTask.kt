package com.sainivik.backgoundlivelocation.util


class EventTask<T> private constructor(
    val task: Task,
    val status: Status,
    val data: T?,
    val msg: String?
)

{

    companion object {

        fun <T> success(data: T, task: Task): EventTask<T> {
            return EventTask(task, Status.SUCCESS, data, null)
        }

        fun <T> error(msg: String, status: Status, task: Task): EventTask<T> {
            return EventTask(task, status, null, msg)
        }

        fun <T> perform(data: T?, task: Task): EventTask<T> {
            return EventTask(task, Status.PERFORM, data, null)
        }

        fun <T> loading(task: Task): EventTask<T> {
            return EventTask(task, Status.LOADING, null, null)
        }
    }


}
