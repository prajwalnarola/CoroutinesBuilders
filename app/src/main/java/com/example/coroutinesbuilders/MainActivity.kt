package com.example.coroutinesbuilders

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    lateinit var viewModel: MainActivityViewModel

    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProvider(this)[MainActivityViewModel::class.java]

        //LifecycleScope attached with activity / fragment lifecycle
        //coroutines in this scope will be cancelled when lifecycle is destroyed
        lifecycleScope.launch {
            delay(2000)
            startActivity(Intent(this@MainActivity, MainActivity2::class.java))
            finish()
        }

        CoroutineScope(Dispatchers.Main).launch {
            task1()
        }

        CoroutineScope(Dispatchers.Main).launch {
            task2()
        }

        CoroutineScope(Dispatchers.IO).launch {
            getExactNumberUsingLaunch()
        }

        CoroutineScope(Dispatchers.IO).launch {
            getExactNumberUsingAsync()
        }

        CoroutineScope(Dispatchers.IO).launch {
            getExactNumberUsingAsync()
        }

        GlobalScope.launch {
            executeWithContext()
        }

        runBlocking {
           launch {
               delay(5000)
               Log.d( "runBlocking", "World")
           }
            // as per name suggest run blocking, it blocks or holds  threads until inside coroutines complete their execution
            Log.d( "runBlocking", "Hello")
        }

    }

    private suspend fun executeWithContext(){
        Log.d( "WithContext", "Before")
        withContext(Dispatchers.IO){
            delay(2000)
            Log.d( "WithContext", "Inside")
        }
        // by using withContext we can suspend coroutine which hold execution until coroutine complete its execution and the execute another line of code
        Log.d( "WithContext", "After")
    }

    private suspend fun getExactNumberUsingAsync() {
        CoroutineScope(Dispatchers.IO).launch {
            val num1 = async {
                getNumberForAsync1()
            }
            val num2 = async {
                getNumberForAsync2()
            }
            // Parallel executed num1 and num 2 using async and then after completion execution of code move ahead
            Log.d("Async", "Num1: ${num1.await()} - Num2: ${num2.await()}")
        }
    }

    private suspend fun getExactNumberUsingLaunch() {
        var number = 0
        val job = CoroutineScope(Dispatchers.IO).launch {
            number = getNumberForLaunch()
        }
        job.join() // Used to hold execution until coroutine complete its execution and then execute next task
        Log.d("JOB", number.toString())
    }

    private suspend fun task1(){
        Log.d("TAG", "starting task1")
        delay(2000)
        Log.d(TAG, "ending task1")
    }

    private suspend fun task2(){
        Log.d("TAG", "starting task2")
        delay(2000)
        Log.d(TAG, "ending task2")
    }

    private suspend fun getNumberForLaunch(): Int {
        delay(2000)
        return 45
    }

    private suspend fun getNumberForAsync1(): Int {
        delay(2000)
        return 18
    }

    private suspend fun getNumberForAsync2(): Int {
        delay(2000)
        return 7
    }
}
